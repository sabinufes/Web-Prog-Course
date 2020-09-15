package com.app.web2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import java.util.ArrayList;


@Controller
public class UserController {


    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/register")
    public ModelAndView showRegister() {
        ModelAndView regMV = new ModelAndView("register.html");
        return regMV;


    }

    @GetMapping("/dashboard")
    public ModelAndView showDash() {
        ModelAndView dashMV = new ModelAndView("dashboard.html");
        return dashMV;
    }


    @GetMapping("register-form")
    public ModelAndView register(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "password", required = true) String password) {
        ModelAndView registerMV = new ModelAndView("register.html");


        jdbcTemplate.update("INSERT INTO users values (null, ?, ?)", username, password);
        return new ModelAndView("redirect:/Login.html");

    }


    @GetMapping("/login")
    public ModelAndView showLogin() {
        ModelAndView registerMV = new ModelAndView("login.html");
        return registerMV;

    }


    @GetMapping("login-form")
    public ModelAndView login(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "password", required = true) String password) {
        ModelAndView loginMV = new ModelAndView("Login.html");
        String queryStm = "SELECT * FROM users WHERE users= '" + username + "';";

        ArrayList<User> users = (ArrayList<User>) jdbcTemplate.query(queryStm, new UserMapper());
        if (users.size() == 0) {
            loginMV.addObject("errLogin", "nu exista cont cu acest email");
            return loginMV;
        } else if (users.size() > 1) {
            loginMV.addObject("errLogin", "Exista mai multi utilizatori cu aces nume");
            return loginMV;
        } else {
            User dbUser = users.get(0);
            if (dbUser.getPassword().equals(password)) {
                return new ModelAndView("redirect:/dashboard");
            } else {
                loginMV.addObject("errLogin", "parola incorecta");
                return loginMV;

            }
        }
    }

    @PostMapping(value = "login-form", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView loginPost(LoginForm loginFormParams) {


        ModelAndView loginMV = new ModelAndView("Login.html");
        String queryStm = "SELECT * FROM users WHERE users= '" + loginFormParams.getUsername() + "';";

        ArrayList<User> users =(ArrayList<User>) jdbcTemplate.query(queryStm, new UserMapper());
        if (users.size() == 0) {
            loginMV.addObject("errLogin", "nu exista cont cu acest email");
            return loginMV;
        } else if (users.size() > 1) {
            loginMV.addObject("errLogin", "Exista mai multi utilizatori cu aces nume");
            return loginMV;
        } else {
            User dbUser = users.get(0);
            if (dbUser.getPassword().equals(loginFormParams.getPassword())) {
                return new ModelAndView("redirect:/dashboard");
            } else {
                loginMV.addObject("errLogin", "parola incorecta");
                return loginMV;

            }
        }
    }
}

