package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.pojo.Column;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 专栏相关操作
 * @author Administrator
 *
 */
public interface ColumnDao extends JpaRepository<Column,String>,JpaSpecificationExecutor<Column>{

    /**
     * 通过用户id，查询专栏
     * @param id 用户id
     * @return 查询结果
     */
    Column findByUserid(String id);

    /**
     * 通过专栏id，审核
     * @param id 专栏id
     */
    @Modifying
    @Query(value = "UPDATE tb_column set state = 1 where id = ?",nativeQuery = true)
    void updateStatus(String id);
}
