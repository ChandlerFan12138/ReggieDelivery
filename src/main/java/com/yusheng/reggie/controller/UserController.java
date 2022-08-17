package com.yusheng.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yusheng.reggie.common.R;
import com.yusheng.reggie.entity.User;
import com.yusheng.reggie.service.UserService;
import com.yusheng.reggie.utils.SMSUtils;
import com.yusheng.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * send message
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            //gengerate the code with 4 numbers
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

            //Use the aliyun API to send message
            SMSUtils.sendMessage("Reggie","",phone,code);

            //store the code in Session
            session.setAttribute(phone,code);

            return R.success("Send successfully");
        }

        return R.error("Send not successfully");
    }

    /**
     * User login
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
//    instead of using DTO, we can use one map to store
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

        //get number
        String phone = map.get("phone").toString();

        //get code
        String code = map.get("code").toString();

        //get the code from the session
        Object codeInSession = session.getAttribute(phone);

        //Compare the code input and from the session
        if(codeInSession != null && codeInSession.equals(code)){
            //if successfully

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if(user == null){
                //decide the user if it is a new user. If it is, store it in the user table.
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("Login successfully");
    }

}
