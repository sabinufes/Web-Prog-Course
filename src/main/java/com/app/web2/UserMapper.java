package com.app.web2;


import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {



    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException{

        User user = new User(rs.getInt("id"), rs.getString("users"),
                rs.getString("password"));
        return user;
    }



}
