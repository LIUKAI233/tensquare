package com.tensquare.article.dao;

import com.tensquare.article.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 文章相关操作
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{

    /**
     * 点赞
     * @param id 文章id
     */
    @Modifying
    @Query(value = "update tb_article set thumbup = thumbup + 1 where id = ?",nativeQuery = true)
    void updateThumbup(String id);

    /**
     * 审核文章
     * @param id 文章id
     */
    @Modifying
    @Query(value = "update tb_article set state = 1 where id = ?",nativeQuery = true)
    void examine(String id);

    /**
     * 根据频道ID获取文章列表
     * @param id 频道id
     * @param pageable 分页参数
     * @return 文章列表
     */
    Page<Article> findByChannelid(String id, Pageable pageable);

    /**
     * 根据专栏ID获取文章列表
     * @param id 专栏ID
     * @param pageable 分页参数
     * @return 文章列表
     */
    Page<Article> findByColumnid(String id,Pageable pageable);

    /**
     * 查询头条文章
     * @param top 头条文章标识符
     * @return 查询结果
     */
    List<Article> findByIstop(String top);
}
