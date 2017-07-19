package co.novalist.web.event;

import co.novalist.model.User;
import co.novalist.service.TokenService;
import co.novalist.service.UserService;
import co.novalist.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;


/**
 * Created by user on 6/9/2017.
 */

@Component
public class RegistrationListener implements ApplicationListener<RegistrationEvent>{

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TokenService tokenService;

    @Override
    public void onApplicationEvent(RegistrationEvent registrationEvent){
        this.confirmRegistration(registrationEvent);
    }

    private void confirmRegistration(RegistrationEvent registrationEvent){
        User user = registrationEvent.getUser();
        String string = UUID.randomUUID().toString();
        userService.save(user);
        tokenService.save(tokenService.createToken(string, user, Token.Type.VERIFICATION));
        String recipientAddress = user.getUsername();
        String subject = "NovaList Registration Confirmation";
        String confirmationUrl = registrationEvent.getAppUrl() + "/regconfirm.html?token=" + string;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Please click this link to complete your NovaList registration: http://www.novalist.co"
                + confirmationUrl);
        javaMailSender.send(email);
    }


}
