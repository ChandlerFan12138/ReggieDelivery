package com.yusheng.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yusheng.reggie.entity.Setmeal;
import com.yusheng.reggie.mapper.SetmealMapper;
import com.yusheng.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
