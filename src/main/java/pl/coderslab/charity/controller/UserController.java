package pl.coderslab.charity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.model.Address;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.repository.AddressRepository;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.Validator;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private final DonationService donationService;
    private final UserService userService;
    private final Validator validator;
    private final BCryptPasswordEncoder encoder;
    private final AddressRepository addressRepository;

    public UserController(DonationService donationService, UserService userService, Validator validator, BCryptPasswordEncoder encoder, AddressRepository addressRepository) {
        this.donationService = donationService;
        this.userService = userService;
        this.validator = validator;
        this.encoder = encoder;
        this.addressRepository = addressRepository;
    }

    @GetMapping
    public String list(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("pageMsg", "Zacznij pomagać!<br/>Oddaj niechciane rzeczy w zaufane ręce<br/><br/><a href=\"/donate\" class=\"btn btn--large\">Przekaż rzeczy</a>");
        model.addAttribute("donations", donationService.findByUserEmail(user.getUsername()));
        return "user/profile";
    }

    @GetMapping("/address")
    public String editAddress(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("addresses", userService.getUserAddresses(user.getUsername()));
        return "user/address";
    }

    @PostMapping("/address")
    public String doEditAddress(@AuthenticationPrincipal UserDetails user,
                                @Valid Address address,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            result.getFieldErrors().forEach(err -> sb.append(err.getDefaultMessage()).append("<br/>"));
            model.addAttribute("errMsg", sb.toString());
            model.addAttribute("addresses", userService.getUserAddresses(user.getUsername()));
            return "user/address";
        }
        address.setUser(userService.findByEmail(user.getUsername()));
        addressRepository.save(address);
        return "redirect:/user/address";
    }

    @GetMapping("/address/delete")
    @ResponseBody
    public String deleteAddress(@AuthenticationPrincipal UserDetails user, @RequestParam String id, HttpServletResponse response){
        try {
            var adr = addressRepository.findById(Long.parseLong(id));
            if (adr.isPresent()) {
                Address address = adr.get();
                if (address.getUser().getEmail().equals(user.getUsername())) {
                    address.setUser(null);
                    addressRepository.save(address);
                    return "";
                }
            }
        }catch (NumberFormatException e){
            log.error("On parsing address id: {}", id);
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "";
    }

    @PostMapping("/change-email")
    @ResponseBody
    public String changeEmail(@AuthenticationPrincipal UserDetails ud,
                              @RequestParam String email,
                              HttpServletResponse response) {
        if(ud.getUsername().equals("admin@admin.pl")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Nie możesz modyfikować tego użytkownika";
        }
        User test = userService.findByEmail(email);
        if (test != null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Ten email jest już zarejestrowany";
        }
        User user = userService.findByEmail(ud.getUsername());
        user.setEmail(email);
        var violations = validator.validate(user);
        if (!violations.isEmpty()) {
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
                             HttpServletResponse response) {

        if(ud.getUsername().equals("admin@admin.pl")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Nie możesz modyfikować tego użytkownika";
        }
        if (pass.equals(repeat)) {
            User user = userService.findByEmail(ud.getUsername());
            user.setPassword(pass);
            var violations = validator.validate(user);
            if (!violations.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return violations.stream().findFirst().get().getMessage();
            }
            user.setPassword(encoder.encode(user.getPassword()));
            userService.updateUser(user);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Hasła nie są identyczne";
        }
        return "";
    }
}
