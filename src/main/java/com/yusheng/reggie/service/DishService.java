package com.yusheng.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yusheng.reggie.dto.DishDto;
import com.yusheng.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    /*add dish and dishFlavor*/
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIDWithFlavor(Long id);

    public void UpdateWithFlavor(DishDto dishDto);
}
