package com.yusheng.reggie.dto;

import com.yusheng.reggie.entity.Setmeal;
import com.yusheng.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
