package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    public List<Label> findAll(){
        return labelDao.findAll();
    }

    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    public void save(Label label){
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    public void update(Label label){
        labelDao.save(label);
    }

    public void deleteById(String id){
        labelDao.deleteById(id);
    }

    public List<Label> findSearch(Label label) {
        return labelDao.findAll(createSpecification(label));
    }

    public Page queryPage(Label label,Integer page,Integer size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return labelDao.findAll(createSpecification(label),pageable);
    }

    private Specification<Label> createSpecification(Label label){
        return new Specification<Label>() {
            /**
             *
             * @param root  root根对象 也就是要把条件封装到那个对象中    eg: where 类名 = label.name
             * @param query 封装的查询关键字 eg: group by   order by 等
             * @param cb 用来封装条件对象的，如果返回nul，表示不需要你条件
             * @return 查询条件
             */
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //定义一个集合接收查询条件
                List<Predicate> list = new ArrayList<>();
                if (label.getLabelname() != null && !"".equals(label.getLabelname())){
                    Predicate predicate = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%"); // where labelname like '%PHP%'
                    //查询条件存储到集合中
                    list.add(predicate);
                }
                if (label.getState() != null && !"".equals(label.getState())){
                    Predicate predicate = cb.like(root.get("state").as(String.class),label.getState()); // where labelname like '%PHP%'
                    list.add(predicate);
                }
                //定义查询条件的数组
                Predicate[] predicates = new Predicate[list.size()];
                //将list中的数据，存储到数组中
                list.toArray(predicates);
                return cb.and(predicates);
            }
        };
    }
}
