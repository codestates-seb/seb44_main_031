package competnion.domain.community.service;

import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.community.dto.request.ArticleDto.ArticlePostRequest;
import competnion.domain.community.dto.request.AttendRequest;
import competnion.domain.community.dto.response.ArticleResponse;
import competnion.domain.community.dto.response.WriterResponse;
import competnion.domain.community.entity.Article;
import competnion.domain.community.entity.ArticleImage;
import competnion.domain.community.entity.Attend;
import competnion.domain.community.mapper.ArticleMapper;
import competnion.domain.community.repository.ArticleImageRepository;
import competnion.domain.community.repository.ArticleRepository;
import competnion.domain.community.repository.AttendRepository;
import competnion.domain.community.response.SingleArticleResponseDto;
import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.entity.Pet;
import competnion.domain.pet.repository.PetRepository;
import competnion.domain.pet.service.PetService;
import competnion.domain.user.entity.User;
import competnion.global.exception.BusinessLogicException;
import competnion.global.util.CoordinateUtil;
import competnion.infra.s3.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static competnion.domain.community.entity.ArticleStatus.CLOSED;
import static competnion.global.exception.ExceptionCode.*;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {
    private final PetRepository petRepository;
    private final AttendRepository attendRepository;
    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;

    private final S3Util s3Util;
    private final PetService petService;
    private final CoordinateUtil coordinateUtil;

    private final ArticleMapper mapper;

    @Transactional(readOnly = true)
    public WriterResponse getWriterInfo(final User user) {
        final List<PetResponse> petResponseList = getPetResponses(user);
        return WriterResponse.of(user, petResponseList);
    }

    @Transactional(readOnly = true)
    public List<PetResponse> getAttendeePetInfo(final User user, final Long articleId) {
        final Article article = getArticleByIdOrThrow(articleId);
        checkNotArticleOwner(user, article);
        checkUserAlreadyAttended(user, article);
        return getPetResponses(user);
    }
    
    // 산책 갈래요 작성
    public Long createArticle(
            final User user,
            final ArticlePostRequest request,
            final List<MultipartFile> images
    ) {
        checkPet(user, request.getPetIds());
        checkValidMeetingDate(request);
        checkDuplicateOwnersMeetingDate(user, request);

        final List<String> imageUrlList = s3Util.uploadImageList(images);

        final Article article = saveArticle(user, request);
        saveAttends(user, article, request.getPetIds());
        saveImages(imageUrlList, article);
        return article.getId();
    }

    // 산책 갈래요 참여
    public void attend(final User user, final AttendRequest request) {
        final Article article = getArticleByIdOrThrow(request.getArticleId());

        checkUserAlreadyAttended(user, article);
        checkDuplicateAttendeeMeetingDate(user, request);
        checkMeetingTimeClosed(request);
        checkNotArticleOwner(user, article);
        checkPet(user, request.getPetIds());
        checkSpaceForAttend(request);

        saveAttends(user, article, request.getPetIds());
    }

    /**
     * TODO : 1. 자 산책완료 버튼...
     *        2. startDate 기준 30분부터 버튼 클릭가능(호스트만)
     *        3. 호스트가 버튼을 안누를것을 대비하여 종료시간이 되면 30분 단위로 산책완료(스케줄러)
     *        4. attend 테이블은 지우지말고 유저 참여 검증할때 closed된 아티클은 제외하면 될듯?...모르겠다.
     */
    public void close(final User user, final Long articleId) {
        Article article = getArticleByIdOrThrow(articleId);
        if (LocalDateTime.now().isBefore(article.getStartDate().plusMinutes(30)))
            throw new BusinessLogicException(CAN_NOT_CLOSE);
        article.updateStatus(CLOSED);

        List<Pet> pets = petRepository.findAllByArticleId(articleId);
        pets.forEach(pet -> pet.updateArticle(null));
        article.getPets().clear();
    }

    public void closeScheduled() {
        List<Article> articles = articleRepository.findArticlesOpen();
        articles.forEach(article -> article.updateStatus(CLOSED));
//        articles.forEach(article -> ar);
    }

    public void cancelAttend(User user, Long articleId) {
        /**
         * TODO : 1. 한번 취소하면 재참여 불가능
         2. 취소할때 산책 모임 20분전에는 취소 불가능
         3.
         */
    }

    // 게시글 상세 조회
    public SingleArticleResponseDto findArticle(final Long articleId) {
        final Article article = getArticleByIdOrThrow(articleId);

        final List<String> images = getImgUrlsFromArticle(article);

        final List<User> attendees = extractUserFromAttend(articleId);

        return mapper.articleToSingleArticleResponse(images,article,attendees);
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getAll(
            final User user,
            final String keyword,
            final int days,
            final Pageable pageable
    ) {
        final List<ArticleQueryDto> articles = articleRepository.findAllByKeywordAndDistanceAndDays(
                user.getPoint(),
                keyword,
                days,
                3000.0,
                pageable.getPageNumber(),
                pageable.getPageSize());

        return articles.stream()
                .map(ArticleResponse::of)
                .collect(Collectors.toList());
    }

    private void checkNotArticleOwner(final User user, final Article article) {
        if (article.getUser() == user) throw new BusinessLogicException(CAN_NOT_ATTEND_IN_OWN_ARTICLE);
    }

    private void checkMeetingTimeClosed(final AttendRequest request) {
        final LocalDateTime now = LocalDateTime.now();
        final long minutes = MINUTES.between(now, request.getStartDate());
        if (now.isAfter(request.getEndDate()) || minutes <= 30)
            throw new BusinessLogicException(MEETING_TIME_CLOSED);
    }

    @Transactional(readOnly = true)
    private void checkSpaceForAttend(final AttendRequest request) {
        final long count = attendRepository.countByArticleId(request.getArticleId());
        if (count >= request.getAttendant())
            throw new BusinessLogicException(NO_SPACE_FOR_ATTEND);
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
            throw new BusinessLogicException(USER_ALREADY_ATTENDED);
    }

    private void checkDuplicateOwnersMeetingDate(User user, ArticlePostRequest request) {
        articleRepository.findDuplicateMeetingDate(user, request.getStartDate(), request.getEndDate());
    }

    private void checkDuplicateAttendeeMeetingDate(User user, AttendRequest request) {
        attendRepository.findAttendeeDuplicateMeetingDate(user, request.getStartDate(), request.getEndDate());
    }

    private void checkValidMeetingDate(ArticlePostRequest request) {
        LocalDateTime startDate = request.getStartDate().plusMinutes(29);
        LocalDateTime endDate = request.getEndDate();

        if (request.getStartDate().isBefore(LocalDateTime.now().plusMinutes(30)))
            throw new BusinessLogicException(NOT_VALID_START_DATE);

        if (startDate.isAfter(endDate) || startDate.equals(endDate))
            throw new BusinessLogicException(NOT_VALID_MEETING_DATE);
    }

    private List<User> extractUserFromAttend(final long articleId) {
        return attendRepository.findUsersFromAttendByArticleId(articleId);
    }

    private Article getArticleByIdOrThrow(final Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessLogicException(ARTICLE_NOT_FOUND));
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
        return articleRepository.save(Article.createArticle()
                .user(user)
                .title(request.getTitle())
                .body(request.getBody())
                .location(request.getLocation())
                .point(coordinateUtil.coordinateToPoint(request.getLatitude(), request.getLongitude()))
                .attendant(request.getAttendant())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build());
    }

    private void saveAttends(final User user, final Article article, final List<Long> petIds) {
        final Attend attend = Attend.CreateAttend()
                .article(article)
                .user(user)
                .build();
        attendRepository.save(attend);

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

    private List<Pet> extractJoiningPets(final Long userId, final Long articleId) {
        return petRepository.findParticipatingPetsByUserIdAndArticleId(userId,articleId);
    }

}
