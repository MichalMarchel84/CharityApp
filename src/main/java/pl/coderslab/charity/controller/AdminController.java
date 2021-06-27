package pl.coderslab.charity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.repository.InstitutionRepository;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.UserService;

import javax.servlet.http.HttpServletResponse;

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
    public String showPanel(Model model){
        model.addAttribute("institutions", institutionRepository.findAllByDeletedIsNull());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("donations", donationService.findAll());
        return "admin/admin";
    }

    @PostMapping("/institutions")
    public String updateInstitutions(Institution institution){
        institutionRepository.save(institution);
        return "redirect:/admin";
    }

    @GetMapping("/institutions/delete")
    @ResponseBody
    public String deleteInstitution(@RequestParam String id, HttpServletResponse response){
        try {
            var inst = institutionRepository.findById(Long.parseLong(id));
            if (inst.isPresent()) {
                Institution institution = inst.get();
                institution.setDeleted(true);
                institutionRepository.save(institution);
                return "";
            }
        }catch (NumberFormatException e){
            log.error("On parsing institution id: {}", id);
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "";
    }
}
