package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @RequestMapping(method = RequestMethod.GET)
    private Result findAll(){
        return new Result(true,20000,"查询成功", labelService.findAll());
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.GET)
    private Result findById(@PathVariable String id){
        return new Result(true,20000,"查询成功", labelService.findById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    private Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(true,20000,"保存成功");
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.PUT)
    private Result updata(@PathVariable String id,@RequestBody Label label){
        label.setId(id);
        labelService.update(label);
        return new Result(true,20000,"修改成功");
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.DELETE)
    private Result deleteById(@PathVariable String id){
        labelService.deleteById(id);
        return new Result(true,20000,"删除成功");
    }
}
