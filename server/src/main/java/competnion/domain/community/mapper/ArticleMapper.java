package competnion.domain.community.mapper;

import competnion.domain.community.dto.request.ArticleDto;
import competnion.domain.community.dto.response.ArticleResponseDto;
import competnion.domain.community.entity.Article;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
//    Article articlePostDtoToArticle(ArticleDto.ArticlePostDto articlePostDto);
    default Article articlePostDtoToArticle(ArticleDto.ArticlePostDto articlePostDto) {
        Article article = new Article();
        article.setAttendant(0);
        article.setPassed(false);
        article.setTitle(articlePostDto.getTitle());
        article.setBody(articlePostDto.getBody());
        /**
         * 유저에 대한 정보 필요
         */
        return article;
    }

    default ArticleResponseDto articleToArticleResponseDto(Article article){

        ArticleResponseDto articleResponseDto = new ArticleResponseDto();
        articleResponseDto.setArticleId(article.getArticleId());
        articleResponseDto.setTitle(article.getTitle());
        articleResponseDto.setBody(article.getBody());
        articleResponseDto.setCreatedAt(article.getCreatedAt());
        articleResponseDto.setModifiedAt(article.getModifiedAt());

        return articleResponseDto;
    }
    List<ArticleResponseDto> articlesToArticleResponseDtos(List <Article> articles);


}
