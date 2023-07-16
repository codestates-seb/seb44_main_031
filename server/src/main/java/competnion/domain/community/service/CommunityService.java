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

    public WriterResponse getWriterInfo(final User user) {
        List<PetResponse> petResponseList = getPetResponses(user);
        return WriterResponse.of(user, petResponseList);
    }

    public List<PetResponse> getAttendeePetInfo(final User user, final Long articleId) {
        Article article = getArticleByIdOrThrow(articleId);
        checkNotArticleOwnerOrThrow(user, article);
        // TODO : 강아지 리스트를 보여줄때, 참여가능한 강아지들만 보여줄지????
        return getPetResponses(user);
    }
    
    // 산책 갈래요 작성
    @Transactional
    public Long createArticle(
            final User user,
            final ArticlePostRequest request,
            final List<MultipartFile> images
    ) {
        checkPet(user, request.getPetIds());

        List<String> imageUrlList = s3Util.uploadImageList(images);

        Article article = saveArticle(user, request);
        saveAttends(user, article, request.getPetIds());
        saveImages(imageUrlList, article);
        return article.getId();
    }

    // 산책 갈래요 참여
    @Transactional
    public void attend(final User user, final AttendRequest request) {
        Article article = getArticleByIdOrThrow(request.getArticleId());

        checkNotArticleOwnerOrThrow(user, article);
        checkPet(user, request.getPetIds());
        checkSpaceForAttendOrThrow(request);
        checkMeetingTimeClosedOrThrow(request);

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
    public SingleArticleResponseDto findArticle(Long articleId) {
        Article article = getArticleByIdOrThrow(articleId);

        List<String> images = getImgUrlsFromArticle(article);

        List<User> attendees = extractUserFromAttend(articleId);

        return mapper.articleToSingleArticleResponse(images,article,attendees);
    }

    public List<ArticleResponse> getAll(User user, String keyword, int days, Pageable pageable) {
        List<ArticleQueryDto> articles = articleRepository.findAllByKeywordAndDistance(
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

    private void checkNotArticleOwnerOrThrow(User user, Article article) {
        if (article.getUser() == user) throw new BusinessLogicException(CAN_NOT_ATTEND_IN_OWN_ARTICLE);
    }

    private void checkMeetingTimeClosedOrThrow(AttendRequest attendRequest) {
        LocalDateTime now = LocalDateTime.now();
        long minutes = MINUTES.between(now, attendRequest.getDate());
        if (now.isAfter(attendRequest.getDate()) || minutes <= 30)
            throw new BusinessLogicException(MEETING_TIME_CLOSED);
    }

    private void checkSpaceForAttendOrThrow(AttendRequest attendRequest) {
        long count = attendRepository.countByArticleId(attendRequest.getArticleId());
        if (count >= attendRequest.getAttendant())
            throw new BusinessLogicException(NO_SPACE_FOR_ATTEND);
    }

    private List<User> extractUserFromAttend(long articleId) {
        return attendRepository.findUsersFromAttendByArticleId(articleId);
    }

    private Article getArticleByIdOrThrow(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessLogicException(ARTICLE_NOT_FOUND));
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
        Attend attend = Attend.CreateAttend()
                .article(article)
                .user(user)
                .build();
        attendRepository.save(attend);

        List<Pet> pets = petRepository.findAllById(petIds);

        pets.forEach(pet -> article.getPets().add(pet));
        pets.forEach(pet -> pet.updateArticle(article));
    }

    private void saveImages(List<String> imageUrlList, Article article) {
        for (String imageUrl : imageUrlList) {
            ArticleImage articleImage = ArticleImage.saveImage().imgUrl(imageUrl).article(article).build();
            articleImageRepository.save(articleImage);
            article.getImages().add(articleImage);
        }
    }

    private List<String> getImgUrlsFromArticle(Article article) {
        List<ArticleImage> articleImages = article.getImages();
        List<String> imageUrls = new ArrayList<>();
        for (ArticleImage articleImage : articleImages) {
            imageUrls.add(articleImage.getImgUrl());
        }
        return imageUrls;
    }

    private List<PetResponse> getPetResponses(User user) {
        return user.getPets().stream()
                .map(PetResponse::simple)
                .collect(Collectors.toList());
    }

    private void checkPet(User user, List<Long> petIds) {
        petService.checkUserHasPetOrThrow(user);

        List<Pet> pets = petService.returnExistsPetsOrThrow(petIds);

        for (Pet pet : pets) {
            petService.checkPetMatchUser(user, pet);
            petService.checkValidPetOrThrow(pet);
        }
    }
}
