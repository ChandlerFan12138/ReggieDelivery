package com.yusheng.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yusheng.reggie.common.R;
import com.yusheng.reggie.dto.DishDto;
import com.yusheng.reggie.entity.Category;
import com.yusheng.reggie.entity.Dish;
import com.yusheng.reggie.entity.DishFlavor;
import com.yusheng.reggie.service.CategoryService;
import com.yusheng.reggie.service.DishFlavorService;
import com.yusheng.reggie.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        dishService.saveWithFlavor(dishDto);
        return R.success("saved successfully");
    }


    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page<Dish> pageInfo = new Page<>(page,pageSize);

        Page<DishDto> dishDtoPage= new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null, Dish::getName,name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,queryWrapper);

//        copy
        BeanUtils.copyProperties(pageInfo,dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = null;

        list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }


    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIDWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.UpdateWithFlavor(dishDto);
        return R.success("update successfully");
    }

    @GetMapping("/list")
    public R<List<DishDto>> getById(Dish dish){


        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(dish.getName()), Dish::getName, dish.getName());
        queryWrapper.eq(null != dish.getCategoryId(), Dish::getCategoryId, dish.getCategoryId());

        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        List<Dish> dishs = dishService.list(queryWrapper);

        List<DishDto> dishDtos = dishs.stream().map(item -> {
//            step1. copy the properties of dish into dishDto
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
//            step2. get the category name by searching
            Category category = categoryService.getById(item.getCategoryId());
//            step3. set the category name
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
//            step4. searching and set the dishFlavour
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, item.getId());

            dishDto.setFlavors(dishFlavorService.list(wrapper));
            return dishDto;

        }).collect(Collectors.toList());

        return R.success(dishDtos);
    }
}
