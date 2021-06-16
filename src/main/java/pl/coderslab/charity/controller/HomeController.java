package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;


@Controller
public class HomeController {

    private final UserService users;
    private final DonationService donationService;

    public HomeController(UserService users, DonationService donationService) {
        this.users = users;
        this.donationService = donationService;
    }

    @RequestMapping("/")
    public String homeAction(Model model){
        model.addAttribute("bags", donationService.getTotalBags());
        model.addAttribute("donations", donationService.getTotalDonations());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid User user, BindingResult result, @RequestParam String repeat, Model model) {
        if(result.hasErrors()){
            return "register";
        }else if(users.findByEmail(user.getEmail()) != null) {
            model.addAttribute("emailError", "Do tego adresu jest już przypisane konto");
            return "register";
        }else if(!user.getPassword().equals(repeat)){
            model.addAttribute("repeatError", "Hasła nie są identyczne");
            return "register";
        }
        users.saveUser(user);
        model.addAttribute("newAccount", "Konto zostało utworzone");
        return "login";
    }

}
