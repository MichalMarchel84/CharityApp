package pl.coderslab.charity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.model.Role;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.repository.InstitutionRepository;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final InstitutionRepository institutionRepository;
    private final UserService userService;
    private final DonationService donationService;

    public AdminController(InstitutionRepository institutionRepository, UserService userService, DonationService donationService) {
        this.institutionRepository = institutionRepository;
        this.userService = userService;
        this.donationService = donationService;
    }

    @GetMapping
    public String showPanel(Model model) {
        model.addAttribute("institutions", institutionRepository.findAllByDeletedIsNull());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("donations", donationService.findAll());
        return "admin/admin";
    }

    @PostMapping("/institutions")
    public String updateInstitutions(Institution institution) {
        institutionRepository.save(institution);
        return "redirect:/admin";
    }

    @GetMapping("/institutions/delete")
    @ResponseBody
    public String deleteInstitution(@RequestParam String id, HttpServletResponse response) {
        try {
            var inst = institutionRepository.findById(Long.parseLong(id));
            if (inst.isPresent()) {
                Institution institution = inst.get();
                institution.setDeleted(true);
                institutionRepository.save(institution);
                return "";
            }
        } catch (NumberFormatException e) {
            log.error("On parsing institution id: {}", id);
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "";
    }

    @PostMapping("/user")
    @ResponseBody
    public String editUser(@AuthenticationPrincipal UserDetails ud, @Valid User user, BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return result.getFieldErrors().get(0).getDefaultMessage();
        }
        User test = userService.findByEmail(user.getEmail());
        if ((test != null) && !test.getId().equals(user.getId())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Ten email jest już zarejestrowany";
        }
        Optional<User> savedUser = userService.findById(user.getId());
        if (savedUser.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Użytkownik nieznany";
        }
        if(savedUser.get().getEmail().equals("admin@admin.pl")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Nie możesz edytować tego użytkownika";
        }
        if (savedUser.get().getEmail().equals(ud.getUsername())) {

            org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(user.getEmail(), "1", ud.getAuthorities());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, ud.getPassword(), ud.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (user.getEnabled() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return "Nie możesz zablokować sam siebie...";
            }

            if (((Role) user.getRoles().toArray()[0]).getName().equals("ROLE_USER")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return "Nie sam sobie odebrać uprawnień administratora...";
            }
        }
        user.setPassword(savedUser.get().getPassword());
        userService.updateUser(user);
        return "";
    }
}
