package com.yusheng.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yusheng.reggie.dto.DishDto;
import com.yusheng.reggie.entity.Dish;
import com.yusheng.reggie.entity.DishFlavor;
import com.yusheng.reggie.mapper.DishMapper;
import com.yusheng.reggie.service.DishFlavorService;
import com.yusheng.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        /*save basici information to dish*/
        this.save(dishDto);

        Long dishId = dishDto.getId();/*dish id*/

        /*method1: Set value by for loop*/
        for(DishFlavor dishFlavor:dishDto.getFlavors()){
            dishFlavor.setDishId(dishId);
        }

        /*method 2: set value by stream*/

//        List<DishFlavor> flavors = dishDto.getFlavors();
//        flavors = flavors.stream().map((item)->{
//            item.setDishId(dishId);
//            return item;
//                }).collect(Collectors.toList());

        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    @Override
    public DishDto getByIDWithFlavor(Long id) {
        /*search the basic information of dish*/

        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getId,id);

        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    public void UpdateWithFlavor(DishDto dishDto) {
//        update dish
        this.updateById(dishDto);
//        update flavor: delete first and then insert agagin
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        for(DishFlavor dishFlavor:dishDto.getFlavors()){
            dishFlavor.setDishId(dishDto.getId());
        }
        List<DishFlavor> flavors = dishDto.getFlavors();

        dishFlavorService.saveBatch(flavors);

    }
}
