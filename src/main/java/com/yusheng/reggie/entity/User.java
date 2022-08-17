package com.yusheng.reggie.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
/**
 * 用户信息
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;



    private String name;



    private String phone;


    //0 female 1 male
    private String sex;


    //BRP number
    private String idNumber;


    //image
    private String avatar;


    //0: restricted; 1. allowed
    private Integer status;
}
