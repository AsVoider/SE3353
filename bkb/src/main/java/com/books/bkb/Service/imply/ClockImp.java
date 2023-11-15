package com.books.bkb.Service.imply;

import com.books.bkb.Service.ClockServe;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.time.format.DateTimeFormatter;

@Service
@Scope("session")
public class ClockImp implements ClockServe {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    long StartTime = 0;

    @Override
    public void OnLogin() {
        if(StartTime == 0) {
            StartTime = System.currentTimeMillis();
        }
    }

    @Override
    public String OnLogout() {
        if(StartTime >0) {
            long duration = System.currentTimeMillis() - StartTime;
            long hour = duration / 1000 / 3600;
            long minute = (duration / 1000 % 3660) / 60;
            long second = (duration / 1000 % 3600) % 60;
            long ms = duration - (3600 * hour + 60 * minute + second) * 1000;
            System.out.println("Session lasts " + hour + " hours, " + minute + " minutes, " + second + " seconds, " + ms + " Milliseconds.");
            return "Session lasts " + hour + " hours, " + minute + " minutes, " + second + " seconds, " + ms + " Milliseconds.";
        }
        else {
            return "no Session!";
        }
    }
}
