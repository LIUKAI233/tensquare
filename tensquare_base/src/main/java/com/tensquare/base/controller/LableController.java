package com.tensquare.base.controller;

import com.tensquare.base.pojo.Lable;
import com.tensquare.base.service.LableService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/lable")
public class LableController {
    @Autowired
    private LableService lableService;

    @RequestMapping(method = RequestMethod.GET)
    private Result findAll(){
        return new Result(true,20000,"查询成功",lableService.findAll());
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.GET)
    private Result findById(@PathVariable("lableId") String lableId){
        return new Result(true,20000,"查询成功",lableService.findById(lableId));
    }

    @RequestMapping(method = RequestMethod.POST)
    private Result save(@RequestBody Lable lable){
        lableService.save(lable);
        return new Result(true,20000,"保存成功");
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.PUT)
    private Result updata(@PathVariable("lableId") String lableId,@RequestBody Lable lable){
        lable.setId(lableId);
        lableService.update(lable);
        return new Result(true,20000,"修改成功");
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.DELETE)
    private Result deleteById(@PathVariable("lableId") String lableId){
        lableService.deleteById(lableId);
        return new Result(true,20000,"删除成功");
    }
}
