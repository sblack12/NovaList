package co.novalist.web.event;

import co.novalist.model.Token;
import co.novalist.model.User;
import co.novalist.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by user on 6/20/2017.
 */

@Component
public class PasswordListener implements ApplicationListener<PasswordEvent> {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TokenService tokenService;

    @Override
    public void onApplicationEvent(PasswordEvent passwordEvent){
        this.resetPassword(passwordEvent);
    }

    private void resetPassword(PasswordEvent passwordEvent){
        User user = passwordEvent.getUser();
        String string = UUID.randomUUID().toString();
        tokenService.save(tokenService.createToken(string, user, Token.Type.PASSWORD));
        String recipientAddress = user.getUsername();
        String subject = "Reset NovaList Password";
        String confirmationUrl = passwordEvent.getAppUrl() + "/resetpasswordform.html?token=" + string;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Please click this link to reset your NovaList password: http://www.novalist.co" +
                confirmationUrl);
        javaMailSender.send(email);

    }

}
