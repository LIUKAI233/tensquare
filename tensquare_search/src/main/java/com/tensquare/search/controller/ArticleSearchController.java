package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleSearchController {

    private ArticleSearchService articleSearchService;

    @Autowired
    public void setArticleSearchService(ArticleSearchService articleSearchService){
        this.articleSearchService = articleSearchService;
    }

    /**
     * 添加文章
     * @param article 文章信息
     * @return 处理结果
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Article article){
        articleSearchService.add(article);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @RequestMapping(value = "/search/{keywords}/{page}/{size}",method = RequestMethod.GET)
    public Result findByTitleOrContent(@PathVariable String keywords,@PathVariable int page,@PathVariable int size){
        Page<Article> pages = articleSearchService.findByTitleOrContent(keywords, page, size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pages.getTotalElements(),pages.getContent()));
    }
}
