package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询所有吐槽内容
     * @return 查询结果
     */
    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    /**
     * 通过id查询对应的吐槽内容
     * @param id ID
     * @return 吐槽内容
     */
    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    /**
     * 添加吐槽信息
     * @param spit 吐槽信息
     */
    public void add(Spit spit){
        spit.setPublishtime(new Date());
        spit.set_id(idWorker.nextId()+"");
        spitDao.save(spit);
    }

    /**
     * 修改吐槽信息
     * @param spit 修改的吐槽信息
     */
    public void update(Spit spit){
        spitDao.save(spit);
    }

    /**
     * 删除吐槽信息
     * @param id ID
     */
    public void deleteById(String id){
        spitDao.deleteById(id);
    }

    /**
     * 根据上级ID分页查询吐槽信息
     * @param id ID
     * @param page 页码
     * @param size 页大小
     */
    public Page findByParentid(String id, int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return spitDao.findByParentid(id, pageable);
    }

    /**
     * 吐槽点赞
     * @param id ID
     */
    public void thumbup(String id){
        //方法一  需要操作2次数据库
        /*Spit spit = spitDao.findById(id).get();
        spit.setThumbup((spit.getThumbup() !=null ? spit.getThumbup() : 1)+1);
        spitDao.save(spit);*/

        //方法二  db.spit.update({_id:1},{$inc:{}})
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");
    }
}
