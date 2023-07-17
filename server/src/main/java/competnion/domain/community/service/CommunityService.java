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
import competnion.global.exception.ExceptionCode;
import competnion.global.util.CoordinateUtil;
import competnion.infra.s3.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        // TODO : 강아지 리스트를 보여줄때, 참여가능한 강아지들만 보여줄지????
        return getPetResponses(user);
    }
    
    // 산책 갈래요 작성
    public Long createArticle(
            final User user,
            final ArticlePostRequest request,
            final List<MultipartFile> images
    ) {
        checkPet(user, request.getPetIds());

        final List<String> imageUrlList = s3Util.uploadImageList(images);

        final Article article = saveArticle(user, request);
        saveAttends(user, article, request.getPetIds());
        saveImages(imageUrlList, article);
        return article.getId();
    }

    // 산책 갈래요 참여
    public void attend(final User user, final AttendRequest request) {
        final Article article = getArticleByIdOrThrow(request.getArticleId());

        checkMeetingTimeClosed(request);
        checkNotArticleOwner(user, article);
        checkUserAlreadyAttended(user, article);
        checkPet(user, request.getPetIds());
        checkSpaceForAttend(request);

        saveAttends(user, article, request.getPetIds());
    }

    @Transactional
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
        final long minutes = MINUTES.between(now, request.getDate());
        if (now.isAfter(request.getDate()) || minutes <= 30)
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

    private void checkUserAlreadyAttended(final User user, final Article article) {
        if (attendRepository.findByUserIdAndArticleId(user.getId(), article.getId()).isPresent())
            throw new BusinessLogicException(USER_ALREADY_ATTENDED);
    }

    private Article saveArticle(final User user, final ArticlePostRequest request) {
        return articleRepository.save(Article.CreateArticle()
                .user(user)
                .title(request.getTitle())
                .body(request.getBody())
                .location(request.getLocation())
                .point(coordinateUtil.coordinateToPoint(request.getLatitude(), request.getLongitude()))
                .attendant(request.getAttendant())
                .date(request.getDate())
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
}
