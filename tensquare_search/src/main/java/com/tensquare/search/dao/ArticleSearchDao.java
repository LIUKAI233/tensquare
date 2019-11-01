package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleSearchDao extends ElasticsearchRepository<Article,String> {

    /**
     * 根据标题或者内容模糊查询文章
     * @param title 标题
     * @param content 文章内容
     * @param pageable 分页信息
     * @return 查询结果
     */
    Page<Article> findByTitleOrContentLike(String title, String content, Pageable pageable);
}
