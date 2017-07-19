package co.novalist.web.controller;

import co.novalist.model.Listing;
import co.novalist.model.User;
import co.novalist.service.CategoryService;
import co.novalist.service.ListingService;
import co.novalist.web.FlashMessage;
import co.novalist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/24/2017.
 */

@Controller
public class ListingController {

    @Autowired
    private ListingService listingService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @RequestMapping("/new")
    public String createListing(Model model){
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("listing", new Listing());
        model.addAttribute("action","/save");
        model.addAttribute("heading","Create new listing");
        model.addAttribute("submit", "Create your listing");
        return "listing/form";
    }

    @RequestMapping("/listing/{listingId}/edit")
    public String formEditListing(@PathVariable Long listingId, Model model, Principal principal) {
        Listing listing = listingService.findById(listingId);
        if ( listing == null || !listing.getUser().getUsername().equals(principal.getName())){
            return "redirect:/error";
        }
        if(!model.containsAttribute("listing")) {
            model.addAttribute("listing", listing);
        }
        model.addAttribute("categories",categoryService.findAll());
        model.addAttribute("action","/save");
        model.addAttribute("heading","Edit listing");
        model.addAttribute("submit","Update");
        return "listing/form";
    }

    @PostMapping("/listing/{listingId}/delete")
    public String deleteGif(@PathVariable Long listingId, RedirectAttributes redirectAttributes, Principal principal) {
        Listing listing = listingService.findById(listingId);
        if ( listing == null || !listing.getUser().getUsername().equals(principal.getName())){
            return "redirect:/error";
        }
        redirectAttributes.addFlashAttribute("flash", new FlashMessage(String.format("Your post '%s' ",
                listing.getTitle()) + " has been deleted.", FlashMessage.Status.SUCCESS));
        listingService.delete(listing);
        return "redirect:/mylistings";
    }

    @PostMapping("/save")
    public String addListing(Listing listing,
                             Principal principal,
                             @RequestParam MultipartFile file1,
                             @RequestParam MultipartFile file2,
                             @RequestParam MultipartFile file3,
                             @RequestParam MultipartFile file4,
                             @RequestParam MultipartFile file5,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request) {
        List<MultipartFile> files = new ArrayList<>();
        if (file1.getSize() > 0) { files.add(file1); }
        if (file2.getSize() > 0) { files.add(file2); }
        if (file3.getSize() > 0) { files.add(file3); }
        if (file4.getSize() > 0) { files.add(file4); }
        if (file5.getSize() > 0) { files.add(file5); }
        for (MultipartFile file : files) {
            if (file.getSize() > 1450000) {
                redirectAttributes.addFlashAttribute("flash", new FlashMessage("Maximum size " +
                        "per photo is 1.5MB", FlashMessage.Status.FAILURE));
                return "redirect:" + request.getHeader("Referer");
            }
        }
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        listing.setUser(user);
        listingService.save(listing, files);
        return String.format("redirect:/listing/%s", listing.getId());
    }

    @RequestMapping("/listing/{listingId}/photo1")
    @ResponseBody
    public byte[] listingPhoto1(@PathVariable Long listingId) {
        return listingService.findById(listingId).getPhoto1();
    }

    @RequestMapping("/listing/{listingId}/photo2")
    @ResponseBody
    public byte[] listingPhoto2(@PathVariable Long listingId) {
        return listingService.findById(listingId).getPhoto2();
    }

    @RequestMapping("/listing/{listingId}/photo3")
    @ResponseBody
    public byte[] listingPhoto3(@PathVariable Long listingId) {
        return listingService.findById(listingId).getPhoto3();
    }

    @RequestMapping("/listing/{listingId}/photo4")
    @ResponseBody
    public byte[] listingPhoto4(@PathVariable Long listingId) {
        return listingService.findById(listingId).getPhoto4();
    }

    @RequestMapping("/listing/{listingId}/photo5")
    @ResponseBody
    public byte[] listingPhoto5(@PathVariable Long listingId) {
        return listingService.findById(listingId).getPhoto5();
    }

    @RequestMapping("/listing/{listingId}")
    public String listingDetails(@PathVariable Long listingId, Model model, Principal principal){
        Listing listing = listingService.findById(listingId);
        if (listing == null){
            return "redirect:/error";
        }
        model.addAttribute("listing", listing);
        if (listing.getUser().getUsername().equals(principal.getName())){
            model.addAttribute("lister", true);
        } else {
            model.addAttribute("lister", false);
        }
        return "listing/details";

    }


}
