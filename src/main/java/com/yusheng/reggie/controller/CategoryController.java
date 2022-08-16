package com.yusheng.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yusheng.reggie.common.CustomException;
import com.yusheng.reggie.common.R;
import com.yusheng.reggie.entity.Category;
import com.yusheng.reggie.entity.Dish;
import com.yusheng.reggie.entity.Setmeal;
import com.yusheng.reggie.mapper.DishMapper;
import com.yusheng.reggie.service.CategoryService;
import com.yusheng.reggie.service.DishService;
import com.yusheng.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
//this is the request URL employee
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("New category add successfully");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);

//      条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);

//        进行分页查询
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long id){
        categoryService.remove(id);

        return R.success("delete Successfully");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){


        categoryService.updateById(category);

        return R.success("Modify successfully");
    }
}
