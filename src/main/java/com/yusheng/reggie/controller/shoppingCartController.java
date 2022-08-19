package com.yusheng.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yusheng.reggie.common.BaseContext;
import com.yusheng.reggie.common.R;
import com.yusheng.reggie.entity.ShoppingCart;
import com.yusheng.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class shoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

//        set user_id to decide which shoppingcart it is
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

//        check if there is a dish or setmeal in database;

        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper  = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        if(dishId!=null){
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        }
        else {
            queryWrapper.eq(ShoppingCart::getSetmealId, dishId);
        }

        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);

        if(shoppingCart1!=null){
            Integer number = shoppingCart1.getNumber();
            shoppingCart1.setNumber(number+1);
            shoppingCartService.updateById(shoppingCart1);
        }
        else{
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCart1 = shoppingCart;
        }
        return R.success(shoppingCart1);

    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("check the shoppingcart");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        //SQL:delete from shopping_cart where user_id = ?

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return R.success("successfully clean the shopping cart");
    }
}
