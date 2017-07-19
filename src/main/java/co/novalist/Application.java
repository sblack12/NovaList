package co.novalist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by user on 5/24/2017.
 */
@EnableAutoConfiguration
@ComponentScan
public class Application{

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}