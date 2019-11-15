package com.tensquare.friend.controller;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserClient userClient;

    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid,@PathVariable String type){
        //从登陆信息中获取用户id,在根据type确定是喜欢还是不喜欢
        Claims claimsUser =(Claims) request.getAttribute("claims_user");
        if (claimsUser == null ){
            return new Result(false,StatusCode.LOGINERROR,"权限不足");
        }
        String userid = claimsUser.getId();
        if (type != null) {
            if (type.equals("1")) {
                //执行喜欢操作
                int flag = friendService.addFriend(userid,friendid);
                if (flag == 0){
                    return new Result(false,StatusCode.ERROR,"不能重复添加");
                }
                if (flag == 1){
                    //修改用户关注数和好友粉丝数
                    userClient.updatefollowcountandfanscount(userid,friendid,1);
                    return new Result(true,StatusCode.OK,"添加成功");
                }
            } else if (type.equals("2")){
                //执行不喜欢操作
                int flag = friendService.addNoFriend(userid,friendid);
                if (flag == 0){
                    return new Result(false,StatusCode.ERROR,"不能重复添加");
                }
                if (flag == 1){
                    return new Result(true,StatusCode.OK,"添加成功");
                }
            }
            return new Result(false, StatusCode.ERROR,"参数异常");
        }else {
            return new Result(false, StatusCode.ERROR,"参数异常");
        }
    }

    @RequestMapping(value = "/{friendid}",method = RequestMethod.DELETE)
    public Result deletefrirnd(@PathVariable String friendid){
        Claims claimsUser =(Claims) request.getAttribute("claims_user");
        if (claimsUser == null ){
            return new Result(false,StatusCode.LOGINERROR,"权限不足");
        }
        String userid = claimsUser.getId();
        friendService.deleteFriend(userid,friendid);
        userClient.updatefollowcountandfanscount(userid,friendid,-1);
        return new Result(true,StatusCode.OK,"删除成功");
    }
}
