package com.tensquare.qa.controller;

import com.tensquare.qa.client.LabelClient;
import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private LabelClient labelClient;

    @RequestMapping(value = "/label/{labelId}",method = RequestMethod.GET)
    public Result findLabelById(@PathVariable String labelId){
        return labelClient.findById(labelId);
    }

    /**
     * 添加问题
     * @param problem 添加的问题信息
     * @return 处理结果
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Problem problem){
        problemService.add(problem);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /**
     * 查询所有问题
     * @return 处理结果
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Problem> list = problemService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",list);
    }

    /**
     * 通过id查询问题
     * @param problemId 问题id
     * @return 查询结果
     */
    @RequestMapping(value = "/{problemId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String problemId){
        return new Result(true, StatusCode.OK,"查询成功",problemService.findById(problemId));
    }

    /**
     * 修改问题
     * @param problem 修改后的问题信息
     * @param problemId 问题的id
     * @return 处理结果
     */
    @RequestMapping(value = "/{problemId}",method = RequestMethod.PUT)
    public Result modify(@RequestBody Problem problem,@PathVariable String problemId){
        problem.setId(problemId);
        problemService.update(problem);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    /**
     * 根据id删除问题信息
     * @param problemId 问题id
     * @return 处理结果
     */
    @RequestMapping(value = "/{problemId}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String problemId){
        problemService.deleteById(problemId);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据传入的条件查询问题信息
     * @param problem 查询条件
     * @return 处理结果
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Problem problem){
        List<Problem> list = problemService.findSearch(problem);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    /**
     * 按条件分页查询
     * @param problem 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Problem problem, @PathVariable int page, @PathVariable int size){
        Page<Problem> pages = problemService.findSearch(problem, page, size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Problem>(pages.getTotalElements(),pages.getContent()));
    }

    /**
     * 查询最新问答列表
     * @param labelId 标签id
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    @RequestMapping(value = "/newlist/{labelId}/{page}/{size}",method = RequestMethod.GET)
    public Result newlist(@PathVariable String labelId,@PathVariable int page,@PathVariable int size){
        Page<Problem> pages = problemService.newlist(labelId, page, size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Problem>(pages.getTotalElements(),pages.getContent()));
    }

    /**
     * 查询最热问答
     * @param labelId 标签id
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    @RequestMapping(value = "/hotlist/{labelId}/{page}/{size}",method = RequestMethod.GET)
    public Result hotlist(@PathVariable String labelId,@PathVariable int page,@PathVariable int size){
        Page<Problem> pages = problemService.hotlist(labelId, page, size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Problem>(pages.getTotalElements(),pages.getContent()));
    }

    /**
     * 查询等待回答
     * @param labelId 标签id
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    @RequestMapping(value = "/waitlist/{labelId}/{page}/{size}",method = RequestMethod.GET)
    public Result waitlist(@PathVariable String labelId,@PathVariable int page,@PathVariable int size){
        Page<Problem> pages = problemService.waitlist(labelId, page, size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Problem>(pages.getTotalElements(),pages.getContent()));
    }

    /**
     * 根据标签id查询所有问题
     * @param labelId 标签id
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    @RequestMapping(value = "/all/{label}/{page}/{size}",method = RequestMethod.POST)
    public Result findAllByLabelId(@PathVariable String labelId,@PathVariable int page,@PathVariable int size){
        Page<Problem> pages = problemService.findAllByLabelId(labelId, page, size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Problem>(pages.getTotalElements(),pages.getContent()));
    }
}
