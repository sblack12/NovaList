package co.novalist.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by user on 7/6/2017.
 */



@Controller
public class ErrorController {


    @RequestMapping(value = "/**")
    public String redirectError(){
        return "redirect:/error";
    }


}




