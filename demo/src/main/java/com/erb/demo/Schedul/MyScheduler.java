package com.erb.demo.Schedul;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;
@Component
public class MyScheduler {
    @Scheduled(fixedRate = 5000)
    public void runEvery5Sec() {
        System.out.println("Running every 5 seconds: " + new Date());
    }
    @Scheduled(fixedDelay = 10000)
    public void runAfter10SecOfLastFinish() {
        System.out.println("Runs 10 seconds after the last execution finishes");
    }
    @Scheduled(initialDelay = 3000, fixedRate = 60000)
    public void runWithInitialDelay() {
        System.out.println("Runs 3 sec after startup, then every 1 min");
    }


    @Scheduled(cron = "0 0 * * * *")
    public void runEveryHour() {
        System.out.println("Runs every hour");
    }
}
