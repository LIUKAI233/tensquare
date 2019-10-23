package com.tensquare.qa.service;

import com.tensquare.qa.dao.ProblemDao;
import com.tensquare.qa.pojo.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemService {
    @Autowired
    private ProblemDao problemDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询最新回答
     * @param labelId 标签id
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    public Page<Problem> newlist(String labelId, int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return problemDao.newlist(labelId,pageable);
    }

    /**
     * 查询最热回答
     * @param labelId 标签id
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    public Page<Problem> hotlist(String labelId, int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return problemDao.hotlist(labelId,pageable);
    }

    /**
     * 查询等待回答
     * @param labelId 标签id
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    public Page<Problem> waitlist(String labelId, int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return problemDao.waitlist(labelId,pageable);
    }

    /**
     * 增加问题
     * @param problem 增加问题的实体类
     */
    public void add(Problem problem){
        problem.setId(idWorker.nextId()+"");
        problemDao.save(problem);
    }

    /**
     * 查询问题列表
     * @return 查询结果
     */
    public List<Problem> findAll(){
        return problemDao.findAll();
    }

    /**
     * 根据id查询问题
     * @param id id
     * @return 查询结果
     */
    public Problem findById(String id){
        return problemDao.findById(id).get();
    }

    /**
     * 修改问题
     * @param problem 修改的问题
     */
    public void update(Problem problem){
        problemDao.save(problem);
    }

    /**
     * 通过id删除问题
     * @param id ID
     */
    public void deleteById(String id){
        problemDao.deleteById(id);
    }

    /**
     * 根据条件查询问题
     * @param problem 查询条件
     * @return 查询结果
     */
    public List<Problem> findSearch(Problem problem){
        return problemDao.findAll(createSpecification(problem));
    }

    /**
     * 根据条件分页查询
     * @param problem 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    public Page<Problem> findSearch(Problem problem, int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return problemDao.findAll(createSpecification(problem),pageable);
    }

    /**
     * 查询该标签下所有的问题
     * @param labelId 标签id
     * @param page 页码
     * @param size 页大小
     * @return 查询结果
     */
    public Page<Problem> findAllByLabelId(String labelId, int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return problemDao.findAllByLabelId(labelId,pageable);
    }

    /**
     * 封装查询条件
     * @param problem 查询条件
     * @return 封装后的查询条件
     */
    private Specification<Problem> createSpecification(Problem problem){
        return new Specification<Problem>() {
            @Override
            public Predicate toPredicate(Root<Problem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                ArrayList<Predicate> list = new ArrayList<>();
                //ID
                if(problem.getId() != null && !problem.getId().equals("")){
                    list.add(cb.like(root.get("id").as(String.class), problem.getId()));
                }
                //标题
                if(problem.getTitle() != null && !problem.getTitle().equals("")){
                    list.add(cb.like(root.get("title").as(String.class), "%"+problem.getTitle()+"%"));
                }
                //内容
                if(problem.getContent() != null && !problem.getContent().equals("")){
                    list.add(cb.like(root.get("content").as(String.class), "%" + problem.getContent()+ "%"));
                }
                //用户ID
                if(problem.getUserid() != null && !problem.getUserid().equals("")){
                    list.add(cb.like(root.get("userid").as(String.class), problem.getUserid()));
                }
                //昵称
                if(problem.getNickname() != null && !problem.getNickname().equals("")){
                    list.add(cb.like(root.get("nickname").as(String.class), "%" + problem.getNickname() + "%"));
                }
                //浏览量
                if(problem.getVisits() != null && !problem.getVisits().equals("")){
                    list.add(cb.like(root.get("visits").as(String.class), problem.getVisits()));
                }
                //点赞数
                if(problem.getThumbup() != null && !problem.getThumbup().equals("")){
                    list.add(cb.like(root.get("thumbup").as(String.class), problem.getThumbup()));
                }
                //回复数
                if(problem.getReply() != null && !problem.getReply().equals("")){
                    list.add(cb.like(root.get("reply").as(String.class), problem.getReply()));
                }
                //是否解决
                if(problem.getSolve() != null && !problem.getSolve().equals("")){
                    list.add(cb.like(root.get("solve").as(String.class), problem.getSolve()));
                }
                //回复人昵称
                if(problem.getReplyname() != null && !problem.getReplyname().equals("")){
                    list.add(cb.like(root.get("replyname").as(String.class), "%" + problem.getReplyname() + "%"));
                }
                return cb.and(list.toArray(new Predicate[list.size()]));
            }
        };
    }
}
