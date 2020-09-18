package com.app.web2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;


@Controller
public class UserController {


    @Autowired
    JdbcTemplate jdbcTemplate;

    HashMap<String, ServerMemory> memory = new HashMap<String, ServerMemory>();

    @GetMapping("/register")
    public ModelAndView showRegister() {
        ModelAndView regMV = new ModelAndView("register.html");
        return regMV;


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
    public ModelAndView loginPost(HttpServletRequest req, HttpServletResponse resp,
                                  LoginForm loginFormParams) {


        ModelAndView loginMV = new ModelAndView("Login.html");
        String queryStm = "SELECT * FROM users WHERE users= '" + loginFormParams.getUsername() + "';";

        ArrayList<User> users = (ArrayList<User>) jdbcTemplate.query(queryStm, new UserMapper());
        if (users.size() == 0) {
            loginMV.addObject("errLogin", "nu exista cont cu acest email");
            return loginMV;
        } else if (users.size() > 1) {
            loginMV.addObject("errLogin", "Exista mai multi utilizatori cu aces nume");
            return loginMV;
        } else {
            User dbUser = users.get(0);
            if (dbUser.getPassword().equals(loginFormParams.getPassword())) {
                resp.addCookie(new Cookie("username", dbUser.getUsername()));

                Cookie[] cookies = req.getCookies();
                Cookie sessionCookie = createUserCookies(dbUser, cookies);
                resp.addCookie(sessionCookie);
                return new ModelAndView("redirect:/dashboard");
            } else {
                loginMV.addObject("errLogin", "parola incorecta");
                return loginMV;

            }
        }
    }

    @GetMapping("/dashboard")
    public ModelAndView showDash(HttpServletRequest req, HttpServletResponse resp) {
        ModelAndView dashMV = new ModelAndView("dashboard.html");


        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            dashMV.addObject("err", "Utilizatorul nu este autentificat");
            return dashMV;
        }

        boolean isAuthenticated = false;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                dashMV.addObject("username", cookie.getValue());
                isAuthenticated = true;
            }
        }
        if (isAuthenticated) {
            dashMV.addObject("err", "Utilizatorul nu este autentificat");
        }
        dashMV.addObject("isAutjenticated", isAuthenticated);

        return dashMV;
    }

    private Cookie createUserCookies(User dbUser, Cookie[] cookies) {
        Cookie sessionCookie = findCookie("sessionId", cookies);
        if (sessionCookie == null) {
            UUID uuid = UUID.randomUUID();
            memory.put(uuid.toString(), new ServerMemory(dbUser.getUsername(), 0, createRandomInt(), createRandomInt()));
            sessionCookie = new Cookie("sessionId", uuid.toString());
        }
        return sessionCookie;
    }


    private Cookie findCookie(String name, Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }
    private int createRandomInt() {
        Random rand = new Random();
        return rand.nextInt(10);
    }
}

