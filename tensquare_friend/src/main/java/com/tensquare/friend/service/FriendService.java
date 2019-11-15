package com.tensquare.friend.service;

import com.tensquare.friend.Dao.FriendDao;
import com.tensquare.friend.Dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;


    public int addFriend(String userId,String friendId){
        //先查询记录，如果有，则表示重复添加，返回0
        Friend friend = friendDao.findByUseridAndFriendid(userId, friendId);
        if (friend != null){
            return 0;
        }
        //添加信息，islike默认为0，单向喜欢
        friend = new Friend();
        friend.setUserid(userId);
        friend.setFriendid(friendId);
        friend.setIslike("0");
        friendDao.save(friend);
        //再次查询信息，若双向喜欢，则将2条记录中islike置为1
        if(friendDao.findByUseridAndFriendid(friendId,userId) != null){
            friendDao.updateIslike("1",userId,friendId);
            friendDao.updateIslike("1",friendId,userId);
        }
        return 1;
    }

    public int addNoFriend(String userid, String friendid) {
        NoFriend noFriend = noFriendDao.findByUseridAndFriendid(userid, friendid);
        if (noFriend != null){
            return 0;
        }
        noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
        return 1;
    }

    public void deleteFriend(String userid , String friendid) {
        //删除记录
        friendDao.deleteFriend(userid,friendid);
        //然后更改相互喜欢状态为0
        friendDao.updateIslike("0",friendid,userid);
        //向非好友表添加数据
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }
}
