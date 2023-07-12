package competnion.domain.community.service;

import competnion.domain.community.dto.request.ArticleDto.ArticlePostDto;
import competnion.domain.community.dto.response.ArticleResponseDto;
import competnion.domain.community.dto.response.WriterResponse;
import competnion.domain.community.entity.Article;
import competnion.domain.community.entity.ArticleImage;
import competnion.domain.community.entity.Attend;
import competnion.domain.community.repository.ArticleImageRepository;
import competnion.domain.community.repository.ArticleRepository;
import competnion.domain.community.repository.AttendRepository;
import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.entity.Pet;
import competnion.domain.pet.service.PetService;
import competnion.domain.user.entity.User;
import competnion.domain.user.service.UserService;
import competnion.global.exception.BusinessLogicException;
import competnion.global.exception.ExceptionCode;
import competnion.infra.s3.S3Util;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static competnion.global.exception.ExceptionCode.PET_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;
    private final AttendRepository attendRepository;
    private final UserService userService;
    private final PetService petService;
    private final S3Util s3Util;

    public WriterResponse getWriterInfo(final User user) {
        List<PetResponse> petResponseList = user.getPets().stream()
                .map(PetResponse::simple)
                .collect(Collectors.toList());
        return WriterResponse.of(user, petResponseList);
    }

    /**게시글 생성 메서드 **/
    @Transactional
    public ArticleResponseDto createArticle(
            final User user,
            final ArticlePostDto articlePostDto,
            final List<MultipartFile> images,
            final List<Long> petIds
    ) {
        /**
         * TODO : 1. 개설하기 버튼을 누를때부터 기준 30분 이상인지
                  2. 작성자의 주소 위치의 3km 반경
                  3. 작성자는 게시글 생성할때 데려갈 강아지 선택
         */

        checkWriterHasPet(user);

        Article article = saveArticle(user, articlePostDto);

        List<Attend> attends = new ArrayList<>();
        for (Long petId : petIds) {
            attends.add(Attend.saveAttend()
                    .article(article)
                    .user(user)
                    .pet(petService.checkExistsPetOrThrow(user, petId))
                    .build());
        }
        attendRepository.saveAll(attends);

        List<String> imageUrlList = s3Util.uploadImageList(images);
        saveImages(imageUrlList, article);
        List<String> imgUrlsFromArticle = getImgUrlsFromArticle(article);

        return ArticleResponseDto.of(article, imgUrlsFromArticle);
    }

    // 산책 갈래요 참여
    @Transactional
    public void attend() {

    }

    @Transactional
    /**게시글 수정 메서드 **/
    public void updateArticle(Long articleId, Long userId, Article article) {
        Article findArticle = findArticleById(articleId);
        /**추후 게시글을 작성자만 수정할 수 있는 로직 필요(해결) **/
        validateAuthorEqualUser(findArticle.getUser().getId(), userId);
        findArticle.update(article);
    }

    /**게시글 조회 메서드 **/
//    public ArticleResponseDto findById(Long articleId) {
//        Article article = findVerifiedArticle(articleId);
//        return new ArticleResponseDto(article);
//    }

    @Transactional(readOnly = true)
    /**게시글 전제 조회 메서드 **/
    public Page<Article> findNearbyArticles(final Long userId, Pageable pageable) {

        User findUser = userService.returnExistsUserByIdOrThrow(userId);
        Point userLocation = findUser.getPoint();

        return articleRepository.findNearbyArticles(userLocation, pageable);
    }

    /** 질문 삭제 메서드 **/
    public void deleteArticle(Long articleId, Long userId){

        Article findArticle = findVerifiedArticle(articleId);
        /** 작성자 여부 확인 필요(해결) */
        validateAuthorEqualUser(findArticle.getUser().getId(), userId);
        articleRepository.delete(findArticle);
    }

    public  Article findArticle(Long articleId){
        Article findArticle = findVerifiedArticle(articleId);

        articleRepository.save(findArticle);

        return findArticle;
    }
    /** 게시글이 등록된 게시글인지 확인하는 메서드 **/
    public Article findVerifiedArticle(Long articleId){

        Optional<Article> findArticle = articleRepository.findById(articleId);

        Article article = findArticle.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.INVALID_INPUT_VALUE));

        return article;
    }
    /** 게시글 찾는 메서드 **/
    public Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.INVALID_INPUT_VALUE));
    }

    public Article saveArticle(final User user, final ArticlePostDto articlePostDto) {
        return articleRepository.save(Article.CreateArticle()
                .user(user)
                .title(articlePostDto.getTitle())
                .body(articlePostDto.getBody())
                .location(articlePostDto.getLocation())
                .attendant(articlePostDto.getAttendant())
                .date(articlePostDto.getDate())
                .build());
    }

    public void saveImages(List<String> imageUrlList, Article article) {
        for (String imageUrl : imageUrlList) {
            ArticleImage articleImage = ArticleImage.saveImage().imgUrl(imageUrl).article(article).build();
            articleImageRepository.save(articleImage);
            article.getImages().add(articleImage);
        }
    }

    public List<String> getImgUrlsFromArticle(Article article) {
        List<ArticleImage> articleImages = article.getImages();
        List<String> imageUrls = new ArrayList<>();
        for (ArticleImage articleImage : articleImages) {
            imageUrls.add(articleImage.getImgUrl());
        }
        return imageUrls;
    }

    public void checkWriterHasPet(User user) {
        if (user.getPets().size() == 0) throw new BusinessLogicException(PET_NOT_FOUND);
    }

    /** 게시글을 수정할 때 작성자인지 확인하는 메서드 */
    private void validateAuthorEqualUser(Long authorId, Long userId) {
        if (!authorId.equals(userId)) {
            throw new BusinessLogicException(ExceptionCode.ACCESS_NOT_ALLOWED);
        }
    }
}
