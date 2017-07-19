package co.novalist.web.controller;

import co.novalist.model.Role;
import co.novalist.model.User;
import co.novalist.service.TokenService;
import co.novalist.web.FlashMessage;
import co.novalist.web.event.PasswordEvent;
import co.novalist.web.event.RegistrationEvent;
import co.novalist.model.Token;
import co.novalist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * Created by user on 5/30/2017.
 */

@Controller
public class AuthenticationController {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;


    @RequestMapping(path = "/login")
    public String loginForm(Model model, HttpServletRequest httpServletRequest){
        model.addAttribute("user", new User());
        model.addAttribute("actionLogin", "/login");
        model.addAttribute("actionSignup", "/saveuser");
        model.addAttribute("submitLogin", "Log in");
        model.addAttribute("submitSignup", "Sign up");
        model.addAttribute("headingLogin", "Log in");
        model.addAttribute("headingSignup", "Don't have an account? " +
                "Use your Villanova email address to sign up!");


        Object loginError = httpServletRequest.getSession().getAttribute("loginError");
        model.addAttribute("loginError", loginError);
        httpServletRequest.getSession().removeAttribute("loginError");

        return "authentication/login";
    }

    @RequestMapping(path = "/saveuser", method = RequestMethod.POST)
    public String saveNewUser(User user,
                              WebRequest webRequest,
                              RedirectAttributes redirectAttributes){
        String email = user.getUsername().toLowerCase();
        if (userService.findByUsername(email) == null) {
            if (email.length() < 15 || !email.substring(email.length() - 13).equals("villanova.edu")){
                redirectAttributes.addFlashAttribute("flash", new FlashMessage("Registration error. " +
                        "You must use your Villanova email address to register for NovaList.", FlashMessage.Status.FAILURE));
            } else if (!webRequest.getParameter("passwordConfirm").equals(user.getPassword())){
                redirectAttributes.addFlashAttribute("flash", new FlashMessage("Registration error. " +
                        "Passwords do not match!", FlashMessage.Status.FAILURE));
            } else {
                Role role = new Role();
                role.setId(1L);
                String encryptedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encryptedPassword);
                role.setName("ROLE_USER");
                user.setRole(role);
                try {
                    String appUrl = webRequest.getContextPath();
                    applicationEventPublisher.publishEvent(new RegistrationEvent
                            (user, webRequest.getLocale(), appUrl));
                } catch (Exception e) {
                    return "redirect:/regError";
                }
                redirectAttributes.addFlashAttribute("flash", new FlashMessage("Registration Successful! " +
                        "We have sent you an email with a confirmation link to activate your account.", FlashMessage.Status.SUCCESS));
            }

        } else {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage(String.format("Registration " +
                            "error. The email '%s' ", user.getUsername()) + " has already been used to sign up for " +
                    "NovaList.", FlashMessage.Status.FAILURE));
        }
        return "redirect:/login";

    }

    @RequestMapping(path ="/forgotpassword")
    public String forgotPasswordForm(Model model){
        model.addAttribute("action", "/sendPasswordEmail");
        model.addAttribute("submit", "Submit");
        return "authentication/forgotPassword";
    }

    @RequestMapping(path = "/sendPasswordEmail")
    public String resendPassword(RedirectAttributes redirectAttributes, WebRequest webRequest){
        User user = userService.findByUsername(webRequest.getParameter("email"));
        if (user == null){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Email not found.",
                    FlashMessage.Status.FAILURE));
        } else {
            String appUrl = webRequest.getContextPath();
            applicationEventPublisher.publishEvent(new PasswordEvent(
                    user, webRequest.getLocale(), appUrl));
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("An email has been sent to you " +
                    "containing a link to reset your password.", FlashMessage.Status.SUCCESS));
        }
        return "redirect:/forgotpassword";
    }

    @RequestMapping(path = "/resetpasswordform")
    public String resetPasswordForm(Model model, @RequestParam("token") String string,
                                    RedirectAttributes redirectAttributes){
        try {
            Token token = tokenService.findByString(string);
            Calendar cal = Calendar.getInstance();
            if (token.getExpiryDate().getTime() - cal.getTime().getTime() <= 0) {
                redirectAttributes.addFlashAttribute("flash", new FlashMessage("This link to reset " +
                        "your password has expired. Please enter your email to reset your password.",
                        FlashMessage.Status.FAILURE));
                return "redirect:/forgotpassword";

            } else {
                User user = token.getUser();
                model.addAttribute("tokenString", string);
                model.addAttribute("user", user);
                model.addAttribute("action", "/updatePassword");
                model.addAttribute("submit", "Reset password");
                return "authentication/resetPassword";
            }
        } catch (Exception e){
            return "error";
        }
    }

    @RequestMapping(path = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(RedirectAttributes redirectAttributes, WebRequest webRequest){
        User user = userService.findByUsername(webRequest.getParameter("username"));
        String newPass = webRequest.getParameter("passwordNew");
        String string = webRequest.getParameter("tokenString");
        if (!webRequest.getParameter("passwordConfirm").equals(newPass)) {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Passwords do not match!",
                    FlashMessage.Status.FAILURE));
            return "redirect:/resetpasswordform.html?token=" + string;
        } else {
            String encryptedPassword = passwordEncoder.encode(newPass);
            user.setPassword(encryptedPassword);
            userService.save(user);
            tokenService.delete(tokenService.findByString(string));
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Password has been reset! " +
                    "You may now login.", FlashMessage.Status.SUCCESS));
            return "redirect:/login";
        }
    }


    @RequestMapping(path = "/resendLink", method = RequestMethod.POST)
    public String resendLink (RedirectAttributes redirectAttributes, WebRequest webRequest) {
        User user = userService.findByUsername(webRequest.getParameter("email"));
        if (user == null){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Email not found.",
                    FlashMessage.Status.FAILURE));
        } else if (user.isEnabled()){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("The account using " +
                    "this email has already been confirmed.", FlashMessage.Status.FAILURE));
        } else {
            Token token = tokenService.findByUser(user);
            tokenService.delete(token);
            try {
                String appUrl = webRequest.getContextPath();
                applicationEventPublisher.publishEvent(new RegistrationEvent
                        (user, webRequest.getLocale(), appUrl));
            } catch (Exception e) {
                return "redirect:/regError";
            }
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("The confirmation link " +
                    "has been sent to your email!", FlashMessage.Status.SUCCESS));
        }
        return "redirect:/resend";

    }

    @RequestMapping(path = "/regconfirm")
    public String confirmReg(@RequestParam("token") String string, RedirectAttributes redirectAttributes){

        Token token = tokenService.findByString(string);
        Calendar cal = Calendar.getInstance();
        if (token == null) {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Confirmation link not found.",
                    FlashMessage.Status.FAILURE));
        } else if (token.getUser().isEnabled()) {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("The account associated " +
                    "with this confirmation link is already active.", FlashMessage.Status.FAILURE));
        } else if (token.getExpiryDate().getTime() - cal.getTime().getTime() <= 0){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Confirmation link expired. " +
                    "Please click here to resend.", FlashMessage.Status.FAILURE));
        } else {
            User user = token.getUser();
            user.setEnabled(true);
            userService.save(user);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Confirmation successful! " +
                    "You can now login.", FlashMessage.Status.SUCCESS));
        }
        return "redirect:/login";

    }
}
