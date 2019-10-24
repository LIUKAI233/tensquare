package com.tensquare.article.service;

import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
 * 关于文章的相关操作
 * @author Administrator
 *
 */
@Service
@Transactional
public class ArticleService {

	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisTemplate redisTemplate;

    /**
     * 文章点赞
     * @param id 文章id
     */
	public void updateThumbup(String id){
	    articleDao.updateThumbup(id);
    }

    /**
     * 根据频道ID获取文章列表
     * @param id 频道id
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    public Page<Article> findAllByChannelId(String id,int page,int size){
	    Pageable pageable = PageRequest.of(page,size);
	    return articleDao.findByChannelid(id,pageable);
    }

    /**
     * 根据专栏ID获取文章列表
     * @param id 专栏id
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    public Page<Article> findAllByColumnId(String id,int page,int size){
        Pageable pageable = PageRequest.of(page,size);
        return articleDao.findByColumnid(id,pageable);
    }

    /**
     * 查询头条文章
     * @return 查询结果
     */
    public List<Article> findIsTop(){
        return articleDao.findByIstop("1");
    }

    /**
     * 文章审核
     * @param id 文章id
     */
    public void examine(String id){
		articleDao.examine(id);
    }

	/**
	 * 查询全部列表
	 * @return 查询结果
	 */
	public List<Article> findAll() {
		return articleDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap 查询条件
	 * @param page 页码
	 * @param size 页大小
	 * @return 查询结果
	 */
	public Page<Article> findSearch(Map whereMap, int page, int size) {
		Specification<Article> specification = createSpecification(whereMap);
		Pageable pageable =  PageRequest.of(page-1, size);
		return articleDao.findAll(specification, pageable);
	}

	
	/**
	 * 条件查询
	 * @param whereMap 查询条件
	 * @return 查询结果
	 */
	public List<Article> findSearch(Map whereMap) {
		Specification<Article> specification = createSpecification(whereMap);
		return articleDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id ID
	 * @return 查询结果
	 */
	public Article findById(String id) {
		//先从redis中取出文章信息
		Article article =(Article) redisTemplate.opsForValue().get("article_" + id);
		if (article == null){
			//取不到数据，则从数据库中查询
			article = articleDao.findById(id).get();
			//存入到缓存中
			redisTemplate.opsForValue().set("article_"+id,article);
		}
		return article;
	}

	/**
	 * 增加
	 * @param article 增加的信息
	 */
	public void add(Article article) {
		article.setId( idWorker.nextId()+"" );
		articleDao.save(article);
	}

	/**
	 * 修改
	 * @param article 修改的信息
	 */
	public void update(Article article) {
		//删除缓存
		redisTemplate.delete("article_"+article.getId());
		articleDao.save(article);
	}

	/**
	 * 删除
	 * @param id ID
	 */
	public void deleteById(String id) {
		//删除缓存
		redisTemplate.delete("article_"+id);
		articleDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap 查询条件
	 * @return 封装后的查询条件
	 */
	private Specification<Article> createSpecification(Map searchMap) {

		return new Specification<Article>() {

			@Override
			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 专栏ID
                if (searchMap.get("columnid")!=null && !"".equals(searchMap.get("columnid"))) {
                	predicateList.add(cb.like(root.get("columnid").as(String.class), "%"+(String)searchMap.get("columnid")+"%"));
                }
                // 用户ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 标题
                if (searchMap.get("title")!=null && !"".equals(searchMap.get("title"))) {
                	predicateList.add(cb.like(root.get("title").as(String.class), "%"+(String)searchMap.get("title")+"%"));
                }
                // 文章正文
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                	predicateList.add(cb.like(root.get("content").as(String.class), "%"+(String)searchMap.get("content")+"%"));
                }
                // 文章封面
                if (searchMap.get("image")!=null && !"".equals(searchMap.get("image"))) {
                	predicateList.add(cb.like(root.get("image").as(String.class), "%"+(String)searchMap.get("image")+"%"));
                }
                // 是否公开
                if (searchMap.get("ispublic")!=null && !"".equals(searchMap.get("ispublic"))) {
                	predicateList.add(cb.like(root.get("ispublic").as(String.class), "%"+(String)searchMap.get("ispublic")+"%"));
                }
                // 是否置顶
                if (searchMap.get("istop")!=null && !"".equals(searchMap.get("istop"))) {
                	predicateList.add(cb.like(root.get("istop").as(String.class), "%"+(String)searchMap.get("istop")+"%"));
                }
                // 审核状态
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                	predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
                // 所属频道
                if (searchMap.get("channelid")!=null && !"".equals(searchMap.get("channelid"))) {
                	predicateList.add(cb.like(root.get("channelid").as(String.class), "%"+(String)searchMap.get("channelid")+"%"));
                }
                // URL
                if (searchMap.get("url")!=null && !"".equals(searchMap.get("url"))) {
                	predicateList.add(cb.like(root.get("url").as(String.class), "%"+(String)searchMap.get("url")+"%"));
                }
                // 类型
                if (searchMap.get("type")!=null && !"".equals(searchMap.get("type"))) {
                	predicateList.add(cb.like(root.get("type").as(String.class), "%"+(String)searchMap.get("type")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
