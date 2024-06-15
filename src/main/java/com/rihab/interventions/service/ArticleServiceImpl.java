package com.rihab.interventions.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rihab.interventions.dto.ArticleDTO;
import com.rihab.interventions.dto.TicketDTO;
import com.rihab.interventions.entities.Article;
import com.rihab.interventions.entities.Ticket;
import com.rihab.interventions.repos.ArticleRepository;

import jakarta.transaction.Transactional;
@Service
public class ArticleServiceImpl implements ArticleService {
	
	@Autowired
	ArticleRepository articleRepository;



@Override
public Article saveArticle(Article cause)
{
return articleRepository.save(cause);

}

@Override
public Article updateArticle(Article cause) {
return articleRepository.save(cause);
}


@Override
public void deleteArticle(Article cause) {
	articleRepository.delete(cause);
}


@Override
public void deleteArticleByCodeArticle(long id) {
	articleRepository.deleteById(id);
}


@Override
public Article getArticle(long id) {
return articleRepository.findById(id).get();
}


@Override
public List<Article> getAllArticles() {
return articleRepository.findAll();
}
//  L'utilisation de transactions garantit que les modifications apportées à la base de données respectent les contraintes d'intégrité et de cohérence définies dans le schéma de la base de données. Les transactions assurent que les données restent cohérentes et valides à tout moment.
@Override
public Article updateQuantity(ArticleDTO dto) {
    Long articleId = dto.getCodeArticle();
    Double newQuantity = dto.getQteArticle();

    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new RuntimeException("Article not found with id: " + articleId));

    article.setQteArticle(newQuantity);
    return articleRepository.save(article);
}









}
