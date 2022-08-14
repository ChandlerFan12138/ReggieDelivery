package com.yusheng.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yusheng.reggie.entity.Category;
import com.yusheng.reggie.mapper.CategoryMapper;
import com.yusheng.reggie.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}

