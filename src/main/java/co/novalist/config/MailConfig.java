package co.novalist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by user on 6/9/2017.
 */

@Configuration
@PropertySource("app.properties")
public class MailConfig {

    @Autowired
    private Environment env;

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(env.getProperty("emailHost"));
        javaMailSender.setPort(Integer.parseInt(env.getProperty("emailPort")));
        javaMailSender.setUsername(env.getProperty("emailName"));
        javaMailSender.setPassword(env.getProperty("emailPassword"));
        Properties properties = new Properties();
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");
        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }
}
