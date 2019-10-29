package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {

    private SpitService spitService;
    private RedisTemplate redisTemplate;

    @Autowired
    public void setSpitService(SpitService spitService,RedisTemplate redisTemplate){
        this.spitService = spitService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 查询全部吐槽信息
     * @return 查询结果
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findAll());
    }

    /**
     * 根据id查询
     * @param spitId ID
     * @return 查询结果
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId){
        return new Result(true,StatusCode.OK,"查询成功",spitService.findById(spitId));
    }

    /**
     * 添加吐槽信息
     * @param spit 吐槽信息
     * @return 处理结果
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Spit spit){
        spitService.add(spit);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /**
     * 修改吐槽信息
     * @param spitId ID
     * @param spit 修改的吐槽信息
     * @return 处理结果
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.PUT)
    public Result modify(@PathVariable String spitId,@RequestBody Spit spit){
        spit.set_id(spitId);
        spitService.update(spit);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除吐槽信息
     * @param spitId ID
     * @return 处理结果
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String spitId){
        spitService.deleteById(spitId);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 通过父id分页查询吐槽内容
     * @param parentid 上级id
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentid,@PathVariable int page,@PathVariable int size){
        Page pages = spitService.findByParentid(parentid, page, size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spit>(pages.getTotalElements(),pages.getContent()));
    }

    @RequestMapping(value = "/thumbup/{spitId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId){
        //先定义userId，以后从缓存中获取
        String userId = "123";
        //先从redis缓存中查询是否点赞
        if (redisTemplate.opsForValue().get("thumbup"+userId) != null){
            return new Result(false,StatusCode.REPERROR,"重复点赞");
        }
        //执行点赞操作
        spitService.thumbup(spitId);
        //把该点赞操作添加经缓存信息中
        redisTemplate.opsForValue().set("thumbup"+userId,1);
        return new Result(true,StatusCode.OK,"点赞成功");
    }
}
