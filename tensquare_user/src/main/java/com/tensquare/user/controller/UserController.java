package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import util.JWTUtil;

import java.util.HashMap;
import java.util.Map;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private JWTUtil jwtUtil;

	/**
	 * 更新用户关注数和朋友粉丝数
	 * @param userid 用户id
	 * @param friendid 粉丝id
	 * @param x 更改数量
	 */
	@RequestMapping(value = "/{userid}/{friendid}/{x}",method = RequestMethod.PUT)
	public void updatefollowcountandfanscount(@PathVariable String userid,@PathVariable String friendid,@PathVariable int x){
		userService.updatefollowcountandfanscount(userid,friendid,x);
	}

	/**
	 * 登录操作
	 * @param user 登录信息
	 * @return 登录结果
	 */
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public Result login(@RequestBody User user){
		user = userService.login(user.getMobile(), user.getPassword());
		if (user == null){
			return new Result(false,StatusCode.ERROR,"登录失败");
		}
		String token = jwtUtil.createJwt(user.getId(),user.getMobile(),"user");
		Map<String, Object> map = new HashMap<>();
		map.put("token",token);
		map.put("roles","user");
		return new Result(true,StatusCode.OK,"登录成功",map);
	}

	/**
	 * 发送验证码
	 * @param mobile 电话号码
	 * @return 处理结果
	 */
	@RequestMapping(value = "/sendsms/{mobile}",method = RequestMethod.POST)
	public Result sendsms(@PathVariable String mobile){
		userService.sendsms(mobile);
		return new Result(true,StatusCode.OK,"发送成功");
	}

	/**
	 * 注册用户
	 * @param code 验证码
	 * @param user 用户信息
	 * @return 注册结果
	 */
	@RequestMapping(value = "/register/{code}",method = RequestMethod.POST)
	public Result register(@PathVariable String code,@RequestBody User user){
		//从缓存中获取验证码
		String checkCode = (String)redisTemplate.opsForValue().get("checkcode_" + user.getMobile());
		if (checkCode == null || checkCode.equals("")){
			return new Result(false,StatusCode.ERROR,"该号码未接受到验证码");
		}
		if(!checkCode.equals(code)){
			return new Result(false,StatusCode.ERROR,"请输入正确的验证码");
		}
		userService.add(user);
		return new Result(true,StatusCode.OK,"注册成功");
	}
	
	/**
	 * 查询全部数据
	 * @return 查询结果
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return 查询结果
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
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
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap 查询条件
     * @return 查询结果
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user 用户信息
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user 修改的用户信息
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除 需要验证权限
	 * @param id 用户ID
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
