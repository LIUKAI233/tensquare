package com.tensquare.qa.dao;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 关于问答的相关操作
 */
public interface ProblemDao extends JpaRepository<Problem,String>, JpaSpecificationExecutor<Problem> {

    /**
     * 最新回答
     * @param labelId 标签id
     * @return 查询结果
     */
    @Query(value = "SELECT id,title,content,createtime,updatetime,userid,nickname,visits,thumbup,reply,solve,replyname,replytime FROM tb_problem,tb_pl WHERE problemid = id And labelid = ? ORDER BY replytime DESC;",nativeQuery = true)
    public Page<Problem> newlist(String labelId, Pageable pageable);

    /**
     * 最热回答
     * @param labelId 标签id
     * @return 查询结果
     */
    @Query(value = "SELECT id,title,content,createtime,updatetime,userid,nickname,visits,thumbup,reply,solve,replyname,replytime FROM tb_problem,tb_pl WHERE problemid = id And labelid = ? ORDER BY reply DESC;",nativeQuery = true)
    public Page<Problem> hotlist(String labelId, Pageable pageable);

    /**
     * 等待回答
     * @param labelId 标签id
     * @return 查询结果
     */
    @Query(value = "SELECT id,title,content,createtime,updatetime,userid,nickname,visits,thumbup,reply,solve,replyname,replytime FROM tb_problem,tb_pl WHERE problemid = id And labelid = ? AND reply = 0 ORDER BY createtime;",nativeQuery = true)
    public Page<Problem> waitlist(String labelId, Pageable pageable);

    /**
     * 查询该标签下所有的问题
     * @param labelId 标签id
     * @param pageable 分页参数
     * @return 查询结果
     */
    @Query(value = "SELECT id,title,content,createtime,updatetime,userid,nickname,visits,thumbup,reply,solve,replyname,replytime FROM tb_problem,tb_pl WHERE problemid = id And labelid = ?",nativeQuery = true)
    public Page<Problem> findAllByLabelId(String labelId, Pageable pageable);
}
