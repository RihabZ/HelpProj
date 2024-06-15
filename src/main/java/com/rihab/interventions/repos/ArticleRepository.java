package com.rihab.interventions.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rihab.interventions.entities.Article;
import com.rihab.interventions.entities.Ticket;

public interface ArticleRepository extends JpaRepository<Article, Long> {

	Article findByCodeArticle(Long codeArticle);

	Article getArticleByCodeArticle(Long codeArticle);

}
