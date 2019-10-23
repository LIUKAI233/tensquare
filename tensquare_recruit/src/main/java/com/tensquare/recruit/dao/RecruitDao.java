package com.tensquare.recruit.dao;

import com.tensquare.recruit.pojo.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 招聘信息相关操作
 * 数据访问接口
 * @author Administrator
 *
 */
public interface RecruitDao extends JpaRepository<Recruit,String>,JpaSpecificationExecutor<Recruit>{

    /**
     * 查询推荐职位
     * @param state 状态值
     * @return 查询结果
     */
    List<Recruit> findTop6ByStateOrderByCreatetimeDesc(String state);

    /**
     * 查询最新的6条招聘信息
     * @param state 状态值
     * @return 查询结果
     */
    List<Recruit> findTop6ByStateNotOrderByCreatetimeDesc(String state);
}
