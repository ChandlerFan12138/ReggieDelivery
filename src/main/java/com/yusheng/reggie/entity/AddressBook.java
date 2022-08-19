package com.yusheng.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址簿
 */
@Data
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //id
    private Long userId;


    //the name of accepter
    private String consignee;


    //phone number
    private String phone;


    //0:female 1:male
    private String sex;



    private String provinceCode;



    private String provinceName;



    private String cityCode;



    private String cityName;



    private String districtCode;



    private String districtName;


    //detailed address
    private String detail;


    //label
    private String label;

    //default: 0 no 1 yes
    private Integer isDefault;

    //create time
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    //updateTime
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    //createUser
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    //updateUser
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    //是否删除
    private Integer isDeleted;
}
