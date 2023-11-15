package com.books.bkb.Controller;

import com.books.bkb.DTO.UserDTO;
import com.books.bkb.Entity.User;
import com.books.bkb.Entity.UserStat;
import com.books.bkb.Service.ClockServe;
import com.books.bkb.Service.UserServe;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Scope("session")
public class UserController {
    @Autowired
    UserServe userServe;
    @Autowired
    ClockServe clockServe;

    @GetMapping("/public/user")
    public void createUser()
    {
        userServe.createUser();
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO body)
    {
        System.out.println(body.getEmail() + "\n");
        System.out.println(body.getPassWord() + "\n");
        return userServe.Register(body);
    }

    @GetMapping("/public/check/{id}")
    public boolean checkOnline(@PathVariable Integer id)
    {
        return false;
    }

    @GetMapping("/public/check/checkAdmin/{id}")
    public boolean checkAdmin(@PathVariable Integer id)
    {
        return true;
    }

    @GetMapping("/admin/users")
    public List<User> getUsers()
    {
        return userServe.getUsers();
    }

    @GetMapping("/admin/quickBuyers")
    public List<UserStat> quickBuyers(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end)
    {
        return userServe.getQuickBuyers(start.toLocalDate(), end.toLocalDate());
    }//todo

    @DeleteMapping("/admin/disable/{id}")
    public void disable(@PathVariable Integer id)
    {
        userServe.disableUser(id);
    }

    @GetMapping("/admin/enable/{id}")
    public void enable(@PathVariable Integer id)
    {
        userServe.enableUser(id);
    }

    @GetMapping("/public/onLogin")
    public void startClock() {
        clockServe.OnLogin();
    }

    @GetMapping("/public/onLogout")
    public ResponseEntity<?> endClock() throws JsonProcessingException {
        String time = clockServe.OnLogout();
        Map<String, String> map = new HashMap<>();
        map.put("session time: ", time);
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
    }
}
