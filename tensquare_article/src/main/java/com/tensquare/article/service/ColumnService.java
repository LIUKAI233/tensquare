package com.tensquare.article.service;

import com.tensquare.article.dao.ColumnDao;
import com.tensquare.article.pojo.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
 * 关于专栏的相关操作
 * @author Administrator
 *
 */
@Service
public class ColumnService {

	@Autowired
	private ColumnDao columnDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return 查询结果
	 */
	public List<Column> findAll() {
		return columnDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap 查询条件
	 * @param page 页码
	 * @param size 页大小
	 * @return 查询结果
	 */
	public Page<Column> findSearch(Map whereMap, int page, int size) {
		Specification<Column> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return columnDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap 查询条件
	 * @return 查询结果
	 */
	public List<Column> findSearch(Map whereMap) {
		Specification<Column> specification = createSpecification(whereMap);
		return columnDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id ID
	 * @return 查询结果
	 */
	public Column findById(String id) {
		return columnDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param column 增加信息
	 */
	public void add(Column column) {
		column.setId( idWorker.nextId()+"" );
		columnDao.save(column);
	}

	/**
	 * 修改
	 * @param column 修改信息
	 */
	public void update(Column column) {
		columnDao.save(column);
	}

	/**
	 * 删除
	 * @param id ID
	 */
	public void deleteById(String id) {
		columnDao.deleteById(id);
	}

	/**
	 * 通过用户id，查询专栏
	 * @param id 用户id
	 * @return 查询结果
	 */
	public Column findByUserId(String id){
		return columnDao.findByUserid(id);
	}

	/**
	 * 通过传入的专栏id，审核专栏
	 * @param id 专栏id
	 */
	public void updateStatus(String id){
		columnDao.updateStatus(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap 查询条件
	 * @return 封装的查询条件
	 */
	private Specification<Column> createSpecification(Map searchMap) {

		return new Specification<Column>() {

			@Override
			public Predicate toPredicate(Root<Column> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 专栏名称
                if (searchMap.get("name")!=null && !"".equals(searchMap.get("name"))) {
                	predicateList.add(cb.like(root.get("name").as(String.class), "%"+(String)searchMap.get("name")+"%"));
                }
                // 专栏简介
                if (searchMap.get("summary")!=null && !"".equals(searchMap.get("summary"))) {
                	predicateList.add(cb.like(root.get("summary").as(String.class), "%"+(String)searchMap.get("summary")+"%"));
                }
                // 用户ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
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
