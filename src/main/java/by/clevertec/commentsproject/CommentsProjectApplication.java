package by.clevertec.commentsproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CommentsProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommentsProjectApplication.class, args);
    }

}
