package pl.coderslab.charity.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

@Controller
@RequestMapping("/user")
public class UserController {

    private final DonationService donationService;
    private final UserService userService;
    private final Validator validator;
    private final BCryptPasswordEncoder encoder;

    public UserController(DonationService donationService, UserService userService, Validator validator, BCryptPasswordEncoder encoder) {
        this.donationService = donationService;
        this.userService = userService;
        this.validator = validator;
        this.encoder = encoder;
    }

    @GetMapping
    public String list(@AuthenticationPrincipal UserDetails user, Model model){
        model.addAttribute("pageMsg", "Zacznij pomagać!<br/>Oddaj niechciane rzeczy w zaufane ręce<br/><br/><a href=\"/donate\" class=\"btn btn--large\">Przekaż rzeczy</a>");
        model.addAttribute("donations", donationService.findByUserEmail(user.getUsername()));
        return "user/profile";
    }

    @GetMapping("/address")
    public String editAddress(@AuthenticationPrincipal UserDetails user, Model model){
        model.addAttribute("addresses", userService.getUserAddresses(user.getUsername()));
        return "user/address";
    }

    @PostMapping("/change-email")
    @ResponseBody
    public String changeEmail(@AuthenticationPrincipal UserDetails ud,
                              @RequestParam String email,
                              HttpServletResponse response){
        User test = userService.findByEmail(email);
        if(test != null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Ten email jest już zarejestrowany";
        }
        User user = userService.findByEmail(ud.getUsername());
        user.setEmail(email);
        var violations = validator.validate(user);
        if(!violations.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return violations.stream().findFirst().get().getMessage();
        }
        userService.updateUser(user);
        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(email, "1", ud.getAuthorities());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, ud.getPassword(), ud.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "";
    }

    @PostMapping("/change-pass")
    @ResponseBody
    public String changePass(@AuthenticationPrincipal UserDetails ud,
                             @RequestParam String pass,
                             @RequestParam String repeat,
                             HttpServletResponse response){

        if(pass.equals(repeat)) {
            User user = userService.findByEmail(ud.getUsername());
            user.setPassword(pass);
            var violations = validator.validate(user);
            if(!violations.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return violations.stream().findFirst().get().getMessage();
            }
            user.setPassword(encoder.encode(user.getPassword()));
            userService.updateUser(user);
        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Hasła nie są identyczne";
        }
        return "";
    }
}
