package com.app.web2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class UserController {


   @Autowired
    JdbcTemplate jdbcTemplate;
    @GetMapping("/register")
    public ModelAndView showRegister() {
        ModelAndView regMV = new ModelAndView("register.html");
        return regMV;
    }

    @GetMapping("register-form")
    public ModelAndView register(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "password", required = true) String password)
            {
        ModelAndView registerMV = new ModelAndView("register.html");


        jdbcTemplate.update("INSERT INTO users values (null, ?, ?)", username, password);
        return new ModelAndView("redirect:/index.html");

    }
}
