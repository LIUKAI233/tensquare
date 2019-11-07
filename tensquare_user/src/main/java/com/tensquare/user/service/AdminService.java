package com.tensquare.user.service;

import com.tensquare.user.dao.AdminDao;
import com.tensquare.user.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class AdminService {

	@Autowired
	private AdminDao adminDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private BCryptPasswordEncoder encoder;

	/**
	 * 查询全部列表
	 * @return 查询结果
	 */
	public List<Admin> findAll() {
		return adminDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap 查询条件
	 * @param page 页码
	 * @param size 页大小
	 * @return 查询结果
	 */
	public Page<Admin> findSearch(Map whereMap, int page, int size) {
		Specification<Admin> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return adminDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap 查询条件
	 * @return 查询结果
	 */
	public List<Admin> findSearch(Map whereMap) {
		Specification<Admin> specification = createSpecification(whereMap);
		return adminDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id ID
	 * @return 查询结果
	 */
	public Admin findById(String id) {
		Admin admin =(Admin) redisTemplate.opsForValue().get("admin" + id);
		if (admin == null){
			admin = adminDao.findById(id).get();
			redisTemplate.opsForValue().set("admin" + id,admin);
		}
		return admin;
	}

	/**
	 * 增加
	 * @param admin 管理员信息
	 */
	public void add(Admin admin) {
		admin.setId( idWorker.nextId()+"" );
		//加密密码
		admin.setPassword(encoder.encode(admin.getPassword()));
		adminDao.save(admin);
	}

	/**
	 * 修改
	 * @param admin 修改后的管理员信息
	 */
	public void update(Admin admin) {
		redisTemplate.delete("admin"+admin.getId());
		adminDao.save(admin);
	}

	/**
	 * 删除
	 * @param id ID
	 */
	public void deleteById(String id) {
		redisTemplate.delete("admin"+id);
		adminDao.deleteById(id);
	}

	/**
	 * 登录操作
	 * @param loginname 登录名
	 * @param password 密码
	 * @return 查询结果
	 */
	public Admin login(String loginname, String password) {
		//先根据用户名查询
		Admin byLoginname = adminDao.findByLoginname(loginname);
		if (byLoginname != null && encoder.matches(password,byLoginname.getPassword())){
			return byLoginname;
		}
		return null;
	}

	/**
	 * 动态条件构建
	 * @param searchMap 查询条件
	 * @return 封装后的查询条件
	 */
	private Specification<Admin> createSpecification(Map searchMap) {

		return new Specification<Admin>() {

			@Override
			public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 登陆名称
                if (searchMap.get("loginname")!=null && !"".equals(searchMap.get("loginname"))) {
                	predicateList.add(cb.like(root.get("loginname").as(String.class), "%"+(String)searchMap.get("loginname")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 状态
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                	predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}
}
