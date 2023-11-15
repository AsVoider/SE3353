package com.books.bkb.Service.imply;

import com.books.bkb.DAO.inter.CartDAO;
import com.books.bkb.DAO.inter.OrderItemDAO;
import com.books.bkb.DAO.inter.UserAuthDAO;
import com.books.bkb.DAO.inter.UserDAO;
import com.books.bkb.DTO.UserDTO;
import com.books.bkb.Entity.Cart;
import com.books.bkb.Entity.User;
import com.books.bkb.Entity.UserStat;
import com.books.bkb.Entity.UserAuth;
import com.books.bkb.Service.UserServe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class UserImp implements UserServe {

    UserDAO userDAO;
    UserAuthDAO userAuthDAO;
    CartDAO cartDAO;
    OrderItemDAO orderItemDAO;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserImp(UserDAO userDAO, UserAuthDAO userAuthDAO, CartDAO cartDAO, OrderItemDAO orderItemDAO) {
        this.userDAO = userDAO;
        this.userAuthDAO = userAuthDAO;
        this.cartDAO = cartDAO;
        this.orderItemDAO = orderItemDAO;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override//todo this is a test method
    public void createUser() {
        User user = new User();
        String Name = "GoodLuck";
        user.setName(Name);
        user.setEmail("cnnofreedom@outlook.com");
        var cart = new Cart();
        user.setAdmin(true);
        user.setEnabled(true);
        Integer userId = userDAO.save(user).getId();
        cart.setUserId(userId);
        cartDAO.save(cart);

        UserAuth userAuth = new UserAuth();
        userAuth.setId(userDAO.getByName(Name).getId());
        userAuth.setUsername("root");
        userAuth.setPassword(passwordEncoder.encode("222222"));
        userAuthDAO.save(userAuth);
    }

    @Override
    public ResponseEntity<String> Register(UserDTO userDTO)
    {
        String username = userDTO.getUserName();
        if(userAuthDAO.exist(username))
            return ResponseEntity.badRequest().body("Username already exists");
        System.out.println(userDTO.getEmail());
        User user = new User();
        String Name = userDTO.getName();
        user.setName(Name);
        user.setEmail(userDTO.getEmail());
        user.setEnabled(true);
        user.setAdmin(false);
        Integer userId = userDAO.save(user).getId();
        var cart = new Cart();
        cart.setUserId(userId);
        cartDAO.save(cart);


        UserAuth userAuth = new UserAuth();
        userAuth.setId(userDAO.findMaxId());
        userAuth.setUsername(userDTO.getUserName());
        userAuth.setPassword(passwordEncoder.encode(userDTO.getPassWord()));
        userAuthDAO.save(userAuth);
        return ResponseEntity.ok("Register Successfully");
    }

    @Override
    public List<User> getUsers()
    {
        return userDAO.getUsers();
    }

    @Override
    public void disableUser(Integer id)
    {
        var user = userDAO.getById(id);
        if(user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no such user");
        user.setEnabled(false);
        userDAO.save(user);
    }

    @Override
    public void enableUser(Integer id)
    {
        var user = userDAO.getById(id);
        if(user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no such user");
        user.setEnabled(true);
        userDAO.save(user);
    }

    @Override
    public User findByUsername(String username)
    {
        return userDAO.getByUsername(username);
    }

    @Override
    public User findById(Integer id)
    {
        return userDAO.getById(id);
    }

    @Override
    public List<UserStat> getQuickBuyers(LocalDate start, LocalDate end)
    {
        var str = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
        var ed = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());
        var users = getUsers();
        var quickBuyers = new ArrayList<UserStat>();
        for(var user : users)
        {
            var orders = user.getOrders();
            System.out.println(orders.size() + "\n");
            BigDecimal total = new BigDecimal(0);
            Integer num = 0;
            for(var order : orders)
            {
                var buyDate = order.getBuyTime();
                if(buyDate.after(str) && buyDate.before(ed))
                {
                    total = total.add(order.getPrice());
                    for(var orderItem : orderItemDAO.findByOrderId(order.getId()))
                    {
                        num += orderItem.getNum();
                    }
                }
            }
            quickBuyers.add(new UserStat(user.getName(), total, num));
        }
        quickBuyers.sort(Comparator.comparing(UserStat::getSpent));
        return quickBuyers;
    }
}
