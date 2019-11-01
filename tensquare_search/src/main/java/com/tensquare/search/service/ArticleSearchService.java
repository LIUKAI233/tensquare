package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleSearchDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArticleSearchService {

    private ArticleSearchDao articleSearchDao;

    @Autowired
    public void setArticleSearchDao(ArticleSearchDao articleSearchDao){
        this.articleSearchDao = articleSearchDao;
    }


    /**
     * 添加文章
     * @param article 文章信息
     */
    public void add(Article article){
        articleSearchDao.save(article);
    }

    /**
     * 通过标签或者内容模糊查询文章
     * @param keyWords 查询内容
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    public Page<Article> findByTitleOrContent(String keyWords,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return articleSearchDao.findByTitleOrContentLike(keyWords,keyWords,pageable);
    }
}
