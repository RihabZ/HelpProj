package com.rihab.interventions.service;

import java.util.List;

import com.rihab.interventions.dto.ArticleDTO;
import com.rihab.interventions.entities.Article;


		public interface ArticleService {
	

			Article saveArticle(Article article);
			Article updateArticle(Article article);
			
				Article getArticle(long code);
				List<Article> getAllArticles();
				void deleteArticle(Article cause);
				void deleteArticleByCodeArticle(long id);
				//Article updateQuantity(Long articleId, Double newQuantity);
				Article updateQuantity(ArticleDTO dto);
			
}
