package com.tensquare.article.controller;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	/**
	 * 文章点赞
	 * @param articleId 文章id
	 * @return 处理结果
	 */
	@RequestMapping(value = "/thumbup/{articleId}",method = RequestMethod.PUT)
	public Result updateThumbup(@PathVariable String articleId){
		articleService.updateThumbup(articleId);
		return new Result(true,StatusCode.OK,"点赞成功");
	}

	/**
	 * 根据频道ID获取文章列表
	 * @param channelId 频道ID
	 * @param page 页码
	 * @param size 页大小
	 * @return 查询结果
	 */
	@RequestMapping(value = "/channel/{channelId}/{page}/{size}",method = RequestMethod.POST)
	public Result findByChannelId(@PathVariable String channelId,@PathVariable int page,@PathVariable int size){
		Page<Article> pages = articleService.findAllByChannelId(channelId, page, size);
		return new Result(true,StatusCode.OK,"查询成功",new PageResult<Article>(pages.getTotalElements(),pages.getContent()));
	}

	/**
	 * 根据专栏ID获取文章列表
	 * @param columnId 专栏ID
	 * @param page 页码
	 * @param size 页大小
	 * @return 查询结果
	 */
	@RequestMapping(value = "/column/{columnId}/{page}/{size}",method = RequestMethod.POST)
	public Result findByColumnId(@PathVariable String columnId,@PathVariable int page,@PathVariable int size){
		Page<Article> pages = articleService.findAllByColumnId(columnId, page, size);
		return new Result(true,StatusCode.OK,"查询成功",new PageResult<Article>(pages.getTotalElements(),pages.getContent()));
	}

	/**
	 * 文章审核
	 * @param articleId 文章id
	 * @return 处理结果
	 */
	@RequestMapping(value = "/examine/{articleId}",method = RequestMethod.PUT)
	public Result examine(@PathVariable String articleId){
		articleService.examine(articleId);
		return new Result(true,StatusCode.OK,"审核成功");
	}

	@RequestMapping(value = "/top",method = RequestMethod.GET)
	public Result findTop(){
		return new Result(true,StatusCode.OK,"查询成功",articleService.findIsTop());
	}

	/**
	 * 查询全部数据
	 * @return 查询结果
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",articleService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return 查询结果
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",articleService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Article> pageList = articleService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Article>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap 查询条件
     * @return 查询结果
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",articleService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param article 增加信息
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Article article  ){
		articleService.add(article);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param article 修改信息
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Article article, @PathVariable String id ){
		article.setId(id);
		articleService.update(article);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id ID
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		articleService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
