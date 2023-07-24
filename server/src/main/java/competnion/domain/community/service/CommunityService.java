package competnion.domain.community.service;

import competnion.domain.community.dto.request.ArticlePostRequest;
import competnion.domain.community.dto.request.AttendRequest;
import competnion.domain.community.dto.request.UpdateArticleRequest;
import competnion.domain.community.dto.response.ArticleResponseDto;
import competnion.domain.community.dto.response.WriterResponse;
import competnion.domain.community.entity.Article;
import competnion.domain.community.entity.ArticleImage;
import competnion.domain.community.entity.ArticleStatus;
import competnion.domain.community.entity.Attend;
import competnion.domain.community.mapper.ArticleMapper;
import competnion.domain.community.repository.ArticleImageRepository;
import competnion.domain.community.repository.ArticleRepository;
import competnion.domain.community.repository.AttendRepository;
import competnion.domain.community.response.MultiArticleResponse;
import competnion.domain.community.response.SingleArticleResponseDto;
import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.entity.Pet;
import competnion.domain.pet.repository.PetRepository;
import competnion.domain.pet.service.PetService;
import competnion.domain.user.entity.User;
import competnion.global.common.annotation.Lock;
import competnion.global.exception.BusinessLogicException;
import competnion.global.util.CoordinateUtil;
import competnion.global.util.ZonedDateTimeUtil;
import competnion.infra.s3.S3Util;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static competnion.domain.community.entity.ArticleStatus.CLOSED;
import static competnion.global.exception.ExceptionCode.*;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final PetRepository petRepository;
    private final AttendRepository attendRepository;
    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;

    private final S3Util s3Util;
    private final ZonedDateTimeUtil dateUtil;
    private final PetService petService;
    private final CoordinateUtil coordinateUtil;

    private final ArticleMapper mapper;

    @Transactional(readOnly = true)
    public WriterResponse getWriterInfo(final User user) {
        final List<PetResponse> petResponseList = getPetResponses(user);
        return WriterResponse.of(user, petResponseList);
    }

    // 산책 갈래요 작성
    @Transactional
    public Long createArticle(
            final User user,
            final ArticlePostRequest request,
            final List<MultipartFile> images
    ) {
        s3Util.checkImageCount(images);
        checkDuplicateAttendeeMeetingDate(user, request.getStartDate(),
                request.getStartDate().plusMinutes(parseLong(request.getEndDate())));

        checkPet(user, request.getPetIds());
        checkValidMeetingDate(request);
        checkDuplicateOwnersMeetingDate(user, request.getStartDate(), request.getEndDate());

        final List<String> imageUrlList = s3Util.uploadImageList(images);

        final Article article = saveArticle(user, request);
        saveAttends(user, article, request.getPetIds());
        saveImages(imageUrlList, article);
        return article.getId();
    }

    @Transactional(readOnly = true)
    public List<PetResponse> getAttendeePetInfo(final User user, final Long articleId) {
        final Article article = getArticleByIdOrThrow(articleId);

        checkSpaceForAttend(article.getId(), article.getAttendant());
        checkNotArticleOwner(user, article);
        checkUserAlreadyAttended(user, article);
        return getPetResponses(user);
    }

    // 산책 갈래요 참여
    public void attend(final User user, final AttendRequest request) {
        final Article article = getArticleByIdOrThrow(request.getArticleId());

        checkSpaceForAttend(article.getId(), article.getAttendant());
        checkUserAlreadyAttended(user, article);
        checkMeetingTimeClosed(request.getStartDate(), request.getEndDate());
        checkNotArticleOwner(user, article);
        checkPet(user, request.getPetIds());

        saveAttends(user, article, request.getPetIds());
    }

    // 산책 완료
    @Transactional
    public void close(final User user, final Long articleId) {
        Article article = getArticleByIdOrThrow(articleId);

        checkArticleOwner(user, article);
        checkAlreadyClose(article);

        if (dateUtil.getNow().isBefore(article.getStartDate().plusMinutes(30)))
            throw new BusinessLogicException(CAN_NOT_CLOSE, "산책시작 30분후 완료버튼을 눌러주세요❗");
        article.updateStatus(CLOSED);

        List<Pet> pets = petRepository.findAllByArticleId(articleId);
        pets.forEach(Pet::deleteArticle);
        article.getPets().clear();
    }

    public void closeScheduled() {
        List<Article> articles = articleRepository.findArticlesOpen();
        articles.forEach(article -> article.updateStatus(CLOSED));

        for (Article article : articles) {
            article.getPets().forEach(Pet::deleteArticle);
            article.getPets().clear();
        }
    }

    @Transactional
    public void updateArticle(
            final User user,
            final Long articleId,
            final UpdateArticleRequest request,
            final List<MultipartFile> images
    ) {
        long count = attendRepository.countByArticleId(articleId);
        if (count > 1) throw new BusinessLogicException(USER_ALREADY_ATTENDED, "참여한 유저가 있어 수정이 불가능합니다❗");

        s3Util.checkImageCount(images);

        Article findArticle = getArticleByIdOrThrow(articleId);
        List<Article> articles = articleRepository.findAllArticlesOpenWrittenByUser(user);

        articles.remove(findArticle);
        articles.forEach(article -> {
            ZonedDateTime requestStart = request.getStartDate();
            ZonedDateTime requestEnd = request.getStartDate().plusMinutes(parseLong(request.getEndDate()));
            ZonedDateTime articleStart = article.getStartDate();
            ZonedDateTime articleEnd = article.getEndDate();

            if (requestStart.isAfter(article.getStartDate()) || requestStart.equals(articleStart)
                    || requestEnd.isBefore(article.getEndDate()) || requestEnd.equals(articleEnd))
                throw new BusinessLogicException(DUPLICATE_MEETING_DATE, "겹치는 시간에 참여하시거나 작성하신 모임이 있습니다❗");
        });

        s3Util.isFileAnImageOrThrow(images);

        List<ArticleImage> articleImages = findArticle.getImages();
        articleImageRepository.deleteAll(articleImages);
        findArticle.getImages().clear();

        final List<String> imageUrlList = s3Util.uploadImageList(images);
        saveImages(imageUrlList, findArticle);

        ZonedDateTime endDate = request.getStartDate().plusMinutes(parseLong(request.getEndDate()));
        ofNullable(request.getTitle()).ifPresent(findArticle::updateTitle);
        ofNullable(request.getBody()).ifPresent(findArticle::updateBody);
        ofNullable(request.getStartDate()).ifPresent(findArticle::updateStartDate);
        of(endDate).ifPresent(findArticle::updateEndDate);
    }

    // 참여 취소
    public void cancelAttend(final User user, final Long articleId) {
        // TODO : 1. 한번 취소하면 재참여 불가능(redis 활용)
        Article article = getArticleByIdOrThrow(articleId);
        if (article.getStartDate().minusMinutes(20).isBefore(dateUtil.getNow()))
            throw new BusinessLogicException(CAN_NOT_CANCEL, "산책시작 20분전에는 취소할 수 없습니다❗");

        Attend attend = attendRepository.findByUserIdAndArticleId(user.getId(), articleId)
                .orElseThrow(() -> new BusinessLogicException(ATTEND_NOT_FOUND));

        attendRepository.delete(attend);

        List<Pet> pets = petRepository.findAllByArticleIdAndUserId(articleId, user.getId());
        pets.forEach(Pet::deleteArticle);
        article.getPets().clear();
    }

    // 게시글 삭제
    // TODO : 리팩토링 필요
    public void deleteArticle(final User user, final Long articleId) {
        Article article = getArticleByIdOrThrow(articleId);
        long count = attendRepository.countByArticleId(articleId);

        if (count > 1 || article.getArticleStatus().equals(CLOSED))
            throw new BusinessLogicException(USER_ALREADY_ATTENDED, "참여중인 유저가 있어 삭제가 불가능합니다.");

        Attend attend = attendRepository.findByUserIdAndArticleId(user.getId(), articleId)
                .orElseThrow(() -> new BusinessLogicException(USER_ALREADY_ATTENDED));
        List<Pet> pets = petRepository.findAllByArticleId(articleId);

        attendRepository.delete(attend);
        pets.forEach(Pet::deleteArticle);
        articleRepository.delete(article);
    }

    // 게시글 상세 조회
    public SingleArticleResponseDto findArticle(final Long articleId) {
        final Article article = getArticleByIdOrThrow(articleId);

        final List<String> images = getImgUrlsFromArticle(article);

        final List<User> attendees = extractUsersFromAttend(articleId);

        return mapper.articleToSingleArticleResponse(images,article,attendees);
    }

    // article 은 Qarticle 로 잡혀서 stream 안쪽은 post로 해둠
    @Transactional(readOnly = true)
    public MultiArticleResponse getAll(
            final User user,
            final String keyword,
            final Integer days,
            final Pageable pageable
    ) {
        Page<Article> articles =
                 articleRepository.findAllByKeywordAndDistanceAndDays(
                         user,
                         user.getPoint(),
                         keyword,
                         days,
                         3000.0,
                         pageable
        );

        Page<ArticleResponseDto.OfMultiResponse> responses =
                articles.map(
                        post -> ArticleResponseDto.OfMultiResponse
                                .getResponse(
                                        getImgUrlsFromArticle(post),
                                        post,
                                        countLefts(post),
                                        checkParticipation(post,user.getId())
                                )
                );

      return mapper.articleToMultiArticleResponses(responses.getContent(),user,responses);
    }

    private void checkNotArticleOwner(final User user, final Article article) {
        if (article.getUser() == user) throw new BusinessLogicException(CAN_NOT_ATTEND_IN_OWN_ARTICLE, "본인 게시글에는 참여 할 수 없습니다❗");
    }

    private void checkArticleOwner(final User user, final Article article) {
        if (article.getUser() != user) throw new BusinessLogicException(NOT_ARTICLE_OWNER, "본인 게시글이 아닙니다❗");
    }

    private void checkMeetingTimeClosed(final ZonedDateTime startDate, final ZonedDateTime endDate) {
        ZonedDateTime now = dateUtil.getNow();
        if (now.plusMinutes(30).isAfter(startDate) || now.plusMinutes(30).equals(startDate))
            throw new BusinessLogicException(MEETING_TIME_CLOSED, "이미 마감된 산책모임입니다❗");
    }

    @Transactional(readOnly = true)
    private void checkSpaceForAttend(final Long articleId, final int attendant) {
        final long count = attendRepository.countByArticleId(articleId);
        if (count >= attendant)
            throw new BusinessLogicException(NO_SPACE_FOR_ATTEND, "산책 참여 인원이 마감되었습니다❗");
    }

    @Transactional(readOnly = true)
    private void checkPet(final User user, final List<Long> petIds) {
        petService.checkUserHasPetOrThrow(user);

        final List<Pet> pets = petService.returnExistsPetsOrThrow(petIds);

        for (Pet pet : pets) {
            petService.checkPetMatchUser(user, pet);
            petService.checkValidPetOrThrow(pet);
        }
    }

    private void checkUserAlreadyAttended(final User user, final Article article) {
        if (attendRepository.findByUserIdAndArticleId(user.getId(), article.getId()).isPresent())
            throw new BusinessLogicException(USER_ALREADY_ATTENDED, "이미 참여하셨습니다❗");
    }

    private void checkDuplicateOwnersMeetingDate(User user, ZonedDateTime startDate, String endDate) {
        articleRepository.findDuplicateMeetingDate(
                user, startDate, startDate.plusMinutes(parseLong(endDate)));
    }

    private void checkDuplicateAttendeeMeetingDate(User user, ZonedDateTime startDate, ZonedDateTime endDate) {
        attendRepository.findAttendeeDuplicateMeetingDate(
                user, startDate, endDate);
    }

    private void checkAlreadyClose(Article article) {
        if (article.getArticleStatus().equals(CLOSED))
            throw new BusinessLogicException(ALREADY_CLOSED, "이미 종료된 모임입니다❗");
    }

    private void checkValidMeetingDate(ArticlePostRequest request) {
        final ZonedDateTime startDate = request.getStartDate().plusMinutes(29);
        final int endDate = parseInt(request.getEndDate());

        if (request.getStartDate().isBefore(dateUtil.getNow().plusMinutes(27)))
            throw new BusinessLogicException(NOT_VALID_START_DATE, "산책 시작시간을 확인해주세요❗");

        if (startDate.isAfter(startDate.plusMinutes(endDate)) || startDate.equals(startDate.plusMinutes(endDate)))
            throw new BusinessLogicException(NOT_VALID_MEETING_DATE, "산책 종료시간을 확인해주세요❗");
    }

    private Boolean checkParticipation (Article article,long userId) {
        return extractUsersFromAttend(article.getId()).stream()
                .map(User::getId)
                .anyMatch(attendee -> attendee.equals(userId));
    }

    private List<User> extractUsersFromAttend(final long articleId) {
        return attendRepository.findUsersFromAttendByArticleId(articleId);
    }

    private Article getArticleByIdOrThrow(final Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessLogicException(ARTICLE_NOT_FOUND, "게시글이 없습니다❗"));
    }

    private List<String> getImgUrlsFromArticle(final Article article) {
        final List<ArticleImage> articleImages = article.getImages();
        final List<String> imageUrls = new ArrayList<>();
        for (ArticleImage articleImage : articleImages) {
            imageUrls.add(articleImage.getImgUrl());
        }
        return imageUrls;
    }

    private List<PetResponse> getPetResponses(final User user) {
        return user.getPets().stream()
                .map(PetResponse::simple)
                .collect(Collectors.toList());
    }

    private Article saveArticle(final User user, final ArticlePostRequest request) {
        Point point = coordinateUtil.coordinateToPoint(request.getLongitude(), request.getLatitude());
        return articleRepository.save(Article.createArticle()
                .user(user)
                .title(request.getTitle())
                .body(request.getBody())
                .location(request.getLocation())
                .point(point)
                .attendant(request.getAttendant())
                .startDate(request.getStartDate())
                .endDate(request.getStartDate().plusMinutes(parseInt(request.getEndDate())))
                .build());
    }

    private void saveAttends(final User user, final Article article, final List<Long> petIds) {
        final Attend attend = Attend.CreateAttend()
                .article(article)
                .user(user)
                .build();
        attendRepository.save(attend);
        article.getAttends().add(attend);

        final List<Pet> pets = petRepository.findAllById(petIds);

        pets.forEach(pet -> article.getPets().add(pet));
        pets.forEach(pet -> pet.updateArticle(article));
    }

    private void saveImages(final List<String> imageUrlList, final Article article) {
        for (String imageUrl : imageUrlList) {
            final ArticleImage articleImage = ArticleImage.saveImage().imgUrl(imageUrl).article(article).build();
            articleImageRepository.save(articleImage);
            article.getImages().add(articleImage);
        }
    }

    private int countLefts (Article article) {
        return article.getAttendant() - extractUsersFromAttend(article.getId()).size();
    }
}
