package com.yusheng.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yusheng.reggie.common.R;
import com.yusheng.reggie.entity.Employee;
import com.yusheng.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
//this is the request URL employee
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

//    The login method
//    1.md5 encode; 2. search by username; 3. compare the password; 4. watch the status; 5. pass the id to the session
//    this is the post Url login
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
//        1. encode
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

//        2.Sql
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

//        3.compare
        if(emp == null) return R.error("login failure");

//        4. compare password
        if(!emp.getPassword().equals(password)){
            return R.error("login failure");
        }

//        5. status
        if(emp.getStatus()==0){
            return R.error("Restricted");
        }

//        6. pass to Session
        request.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);

    }

//    1.清理Session中保存的当前员工的id
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.removeAttribute("employee");
        return R.success("退出成功");
    }

//    add new member, the RequestBody stores all the information
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){


        log.info("add new member:{}",employee.toString());
//        original password;
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        Long empID = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empID);
//
//        employee.setUpdateUser(empID);
// mybatisplus method
        employeeService.save(employee);
        return R.success("add member successfully");
    }
/*The information of employee page searching
* */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {}, page Szie = {}, name = {}", page, pageSize,name);

//        use the page seperation constructor
        Page pageInfo = new Page(page, pageSize);

//        use the condition Constructor
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();


//        add one wrapper condition
        queryWrapper.like(name!=null,Employee::getName,name);
//        add order condition
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /*modify the status according to the id*/
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){

        Long empId = (Long)request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);


        employeeService.updateById(employee);
        return R.success("The information has been modified successfully");
    }
    @GetMapping("/{id}")
    public R<Employee> getByiD(@PathVariable Long id){
        log.info("getById");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        else{
            return R.error("No such employee");
        }
    }

}
