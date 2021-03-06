package com.tensquare.qa.controller;
import com.tensquare.qa.pojo.Reply;
import com.tensquare.qa.service.ReplyService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/reply")
public class ReplyController {

	@Autowired
	private ReplyService replyService;

	@Autowired
	private HttpServletRequest request;
	
	
	/**
	 * 查询全部数据
	 * @return 查询结果
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",replyService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return 查询结果
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",replyService.findById(id));
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
		Page<Reply> pageList = replyService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Reply>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap 查询条件
     * @return 查询结果
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",replyService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param reply 实体类
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Reply reply  ){
		//回答问题前，先进行权限验证
		String tokenUser = (String) request.getAttribute("token_user");
		if (tokenUser == null || "".equals(tokenUser)){
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		replyService.add(reply);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param reply 实体类
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Reply reply, @PathVariable String id ){
		reply.setId(id);
		replyService.update(reply);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id ID
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		replyService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
