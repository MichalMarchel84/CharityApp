package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.repository.InstitutionRepository;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.EmailService;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;


@Controller
public class HomeController {

    private final InstitutionRepository institutionRepository;
    private final UserService users;
    private final DonationService donationService;
    private final EmailService emailService;

    public HomeController(InstitutionRepository institutionRepository, UserService users, DonationService donationService, EmailService emailService) {
        this.institutionRepository = institutionRepository;
        this.users = users;
        this.donationService = donationService;
        this.emailService = emailService;
    }

    @RequestMapping("/")
    public String homeAction(Model model){
        model.addAttribute("bags", donationService.getTotalBags());
        model.addAttribute("donations", donationService.getTotalDonations());
        model.addAttribute("institutions", institutionRepository.findAllByDeletedIsNull());
        return "index";
    }

    @GetMapping("/default")
    public String redirectByRole(@AuthenticationPrincipal UserDetails user){
        if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) return "redirect:/admin";
        else if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) return "redirect:/user";
        return "redirect:/";
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

    @PostMapping("/message")
    public String sendMail(@RequestParam String email, @RequestParam String message, Model model){
        try{
            emailService.sendSimpleMessage(email, message);
            model.addAttribute("pageMsg", "Wiadomość została wysłana.<br/>Dziękujemy za skorzystanie z formularza kontaktowego");
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("pageMsg", "Nie udało się wysłać wiadomości...");
        }
        return "blank";
    }
}
