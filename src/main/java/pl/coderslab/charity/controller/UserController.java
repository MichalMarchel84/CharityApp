package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.service.DonationService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final DonationService donationService;

    public UserController(DonationService donationService) {
        this.donationService = donationService;
    }

    @GetMapping
    public String list(@AuthenticationPrincipal UserDetails user, Model model){
        model.addAttribute("pageMsg", "Zacznij pomagać!<br/>Oddaj niechciane rzeczy w zaufane ręce<br/><br/><a href=\"/donate\" class=\"btn btn--large\">Przekaż rzeczy</a>");
        model.addAttribute("donations", donationService.findByUserEmail(user.getUsername()));
        return "user/profile";
    }

    @GetMapping("/address")
    public String editAddress(){
        return "user/address";
    }
}
