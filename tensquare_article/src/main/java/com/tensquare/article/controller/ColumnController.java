package com.tensquare.article.controller;
import com.tensquare.article.pojo.Column;
import com.tensquare.article.service.ColumnService;
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
@RequestMapping("/column")
public class ColumnController {

	@Autowired
	private ColumnService columnService;
	
	
	/**
	 * 查询全部数据
	 * @return 查询结果
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",columnService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return 查询结果
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",columnService.findById(id));
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
		Page<Column> pageList = columnService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Column>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap 查询条件
     * @return 查询结果
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",columnService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param column 增加信息
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Column column  ){
		columnService.add(column);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param column 修改信息
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Column column, @PathVariable String id ){
		column.setId(id);
		columnService.update(column);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id ID
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		columnService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 申请专栏
	 * @param column 专栏信息
	 * @return 处理结果
	 */
	@RequestMapping(value = "/apply",method = RequestMethod.POST)
	public Result apply(@RequestBody Column column){
		columnService.add(column);
		return new Result(true,StatusCode.OK,"申请成功");
	}

	/**
	 * 申请审核
	 * @param columnId 专栏id
	 * @return 处理结果
	 */
	@RequestMapping(value = "/examine/{columnId}",method = RequestMethod.PUT)
	public Result examine(@PathVariable String columnId){
		columnService.updateStatus(columnId);
		return new Result(true,StatusCode.OK,"审核成功");
	}

	@RequestMapping(value = "/user/{userId}",method = RequestMethod.GET)
	public Result findByUserId(@PathVariable String userId){
		return new Result(true,StatusCode.OK,"查询成功",columnService.findByUserId(userId));
	}
}
