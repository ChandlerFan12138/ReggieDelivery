package com.yusheng.reggie.dto;


import com.yusheng.reggie.entity.Dish;
import com.yusheng.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;
//
//    private Integer copies;
}
