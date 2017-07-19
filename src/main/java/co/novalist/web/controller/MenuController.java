package co.novalist.web.controller;

import co.novalist.model.Category;
import co.novalist.model.Listing;
import co.novalist.model.User;
import co.novalist.service.CategoryService;
import co.novalist.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;
import java.util.List;

/**
 * Created by user on 7/1/2017.
 */

@Controller
public class MenuController {

    @Autowired
    private ListingService listingService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/home")
    public String home(Model model){
        model.addAttribute("categories",categoryService.findAll());
        model.addAttribute("headingBrowse", "Browse listings by category");
        model.addAttribute("headingNew", "Or");
        model.addAttribute("actionBrowse", "/browseByCategory");
        model.addAttribute("submitBrowse", "Browse");
        model.addAttribute("actionNew", "/new");
        model.addAttribute("submitNew", "Create new listing");
        return "listing/home";
    }

    @RequestMapping("/browseByCategory")
    public String browseByCategory(WebRequest webRequest){
        String category = webRequest.getParameter("categoryName");
        return "redirect:/browse/" + category;
    }

    @RequestMapping("/browse/{categoryName}")
    public String browse(@PathVariable String categoryName, Model model) {
        List<Listing> listings;
        if (categoryName.equals("All")){
            listings = listingService.findAll();
            categoryName = "All Listings";
        } else {
            Category category = categoryService.findByName(categoryName);
            if (category == null) {
                return "redirect:/error";
            }
            listings = listingService.findByCategoryId(category.getId());
        }
        model.addAttribute("header", categoryName);
        model.addAttribute("listings", listings);
        return "listing/index";
    }

    @RequestMapping("/mylistings")
    public String myListings(Model model, Principal principal){
        User user = (User)((UsernamePasswordAuthenticationToken)principal).getPrincipal();
        List<Listing> listings = listingService.findByUser(user);
        model.addAttribute("header", "Your Listings");
        model.addAttribute("listings", listings);
        return "listing/index";
    }

    @RequestMapping("/")
    public String redirectHome(){
        return "redirect:/home";
    }

}
