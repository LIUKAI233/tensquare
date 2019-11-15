package com.tensquare.friend.Dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend,String> {

    public Friend findByUseridAndFriendid(String userid,String friendid);

    @Modifying
    @Query(value = "update tb_friend set islike = ? WHERE userid=? and friendid =?",nativeQuery = true)
    public void updateIslike(String isLise,String userid,String friendid);

    @Modifying
    @Query(value = "delete from tb_friend where userid=? and friendid =?",nativeQuery = true)
    void deleteFriend(String userid, String friendid);
}
