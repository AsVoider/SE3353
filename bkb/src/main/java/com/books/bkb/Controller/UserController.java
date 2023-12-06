package com.books.bkb.Controller;

import com.books.bkb.DTO.AvatarDTO;
import com.books.bkb.DTO.UserDTO;
import com.books.bkb.Entity.User;
import com.books.bkb.Entity.UserAvatar;
import com.books.bkb.Entity.UserStat;
import com.books.bkb.Service.inter.ClockServe;
import com.books.bkb.Service.inter.UserServe;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;;
import java.util.Base64;
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

//    @PostMapping("/public/uploadAva")
//    public ResponseEntity<?> upload(@RequestBody AvatarDTO avatarDto) throws IOException {
//        byte[] bytes = avatarDto.getFile().getBytes();
//        String base64Data = Base64.getEncoder().encodeToString(bytes);
//        UserAvatar userAvatar = new UserAvatar();
//        userAvatar.setAvatar(base64Data);
//        userAvatar.setUsername(avatarDto.getUsername());
//        if (userServe.save(userAvatar) != null) {
//            return ResponseEntity.ok("success");
//        }
//        return ResponseEntity.badRequest().body("Error");
//    }

    @GetMapping("/public/getAvatar/{id}")
    public ResponseEntity<?> getAvatar(@PathVariable Integer id) throws JsonProcessingException {
        var base64Data = userServe.find(id);
        if (base64Data == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("img", base64Data.getAvatar());
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
    }

//    @PostMapping("/public/updateAvatar")
//    public ResponseEntity<?> updateAvatar(@RequestBody AvatarDTO avatarDto) throws IOException {
//        byte[] bytes = avatarDto.getFile().getBytes();
//        String new64 = Base64.getEncoder().encodeToString(bytes);
//        if (userServe.update(avatarDto.getUsername(), new64) != null) {
//            return ResponseEntity.ok("success");
//        }
//        return ResponseEntity.badRequest().body("Error");
//    }
}
