package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import util.IdWorker;
import util.JWTUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JWTUtil jwtUtil;

	/**
	 * 查询全部列表
	 * @return 查询结果
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap 查询条件
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页查询结果
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap 查询条件
	 * @return 查询结果
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id ID
	 * @return 查询结果
	 */
	public User findById(String id) {
		//先从redis中获取用户信息
		User user = (User) redisTemplate.opsForValue().get("user" + id);
		if (user == null) {
			user = userDao.findById(id).get();
			//把用户信息存入redis缓存中s
			redisTemplate.opsForValue().set("user" + id,user);
		}
		return user;
	}

	/**
	 * 增加
	 * @param user 用户信息
	 */
	public void add(User user) {
		user.setId( idWorker.nextId()+"" );
		//添加默认信息
        user.setFollowcount(0);//关注数
        user.setFanscount(0);//粉丝数
        user.setOnline(0L);//在线时长
        user.setRegdate(new Date());//注册日期
        user.setUpdatedate(new Date());//更新日期
        user.setLastdate(new Date());//最后登陆日期
		//加密账号密码
		user.setPassword(encoder.encode(user.getPassword()));
		userDao.save(user);
	}

	/**
	 * 修改
	 * @param user 修改的用户信息
	 */
	public void update(User user) {
		//移除缓存信息
		redisTemplate.delete("user"+user.getId());
		if (user.getPassword() != null){
			//加密账号密码
			user.setPassword(encoder.encode(user.getPassword()));
		}
		userDao.save(user);
	}

	/**
	 * 删除
	 * @param id ID
	 */
	public void deleteById(String id) {
		//验证权限
		String tokenUser =(String) request.getAttribute("token_admin");
		if (tokenUser == null || tokenUser.equals("")){
			throw new RuntimeException("权限不足!");
		}
		//移除缓存信息
		redisTemplate.delete("user"+id);
		userDao.deleteById(id);
	}

	/**
	 * 发送验证码
	 * @param mobile 电话号码
	 */
	public void sendsms(String mobile) {
		if(userDao.findByMobile(mobile) != null){
			throw new RuntimeException("该手机号已被注册");
		}
		//生成6位数验证码
		String checkCode = RandomStringUtils.randomNumeric(6);
		//向缓存中放一份做验证
		redisTemplate.opsForValue().set("checkcode_"+mobile,checkCode,1, TimeUnit.HOURS);
		//给用户发一份
        Map<String, String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("checkCode",checkCode);
        rabbitTemplate.convertAndSend("sms",map);
		System.out.println("checkCode:"+checkCode);
	}

	/**
	 * 登录操作
	 * @param mobile 电话号码
	 * @param password 密码
	 * @return 查询结果
	 */
	public User login(String mobile, String password) {
		//先通过电话查询用户信息
		User byMobile = userDao.findByMobile(mobile);
		if (mobile != null && encoder.matches(password,byMobile.getPassword())){
			return byMobile;
		}
		return null;
	}

	/**
	 * 动态条件构建
	 * @param searchMap 查询条件
	 * @return 封装后的查询条件
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));
			}
		};

	}

}
