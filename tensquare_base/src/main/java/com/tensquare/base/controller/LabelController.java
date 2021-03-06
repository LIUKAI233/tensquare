package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private Result findById(@PathVariable String labelId){
        return new Result(true,20000,"查询成功", labelService.findById(labelId));
    }

    @RequestMapping(method = RequestMethod.POST)
    private Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(true,20000,"保存成功");
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.PUT)
    private Result updata(@PathVariable String labelId,@RequestBody Label label){
        label.setId(labelId);
        labelService.update(label);
        return new Result(true,20000,"修改成功");
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.DELETE)
    private Result deleteById(@PathVariable String labelId){
        labelService.deleteById(labelId);
        return new Result(true,20000,"删除成功");
    }

    @RequestMapping(value = "/search",method = RequestMethod.POST)
    private Result findSearch(@RequestBody Label label){
        List<Label> search = labelService.findSearch(label);
        return new Result(true,20000,"查询成功",search);
    }

    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    private Result queryPage(@RequestBody Label label,@PathVariable Integer page,@PathVariable Integer size){
        Page pageDate = labelService.queryPage(label,page,size);
        return new Result(true,20000,"查询成功",new PageResult<Label>(pageDate.getTotalElements(),pageDate.getContent()));
    }
}
