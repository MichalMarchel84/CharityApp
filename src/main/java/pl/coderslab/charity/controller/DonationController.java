package pl.coderslab.charity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.repository.CategoryRepository;
import pl.coderslab.charity.repository.InstitutionRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Controller
public class DonationController {

    private final CategoryRepository categoryRepository;
    private final InstitutionRepository institutionRepository;

    public DonationController(CategoryRepository categoryRepository, InstitutionRepository institutionRepository) {
        this.categoryRepository = categoryRepository;
        this.institutionRepository = institutionRepository;
    }

    @GetMapping("/donate")
    public String donation(@AuthenticationPrincipal UserDetails user, Model model){

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("institutions", institutionRepository.findAll());
        model.addAttribute("donation", new Donation());
        return "donation";
    }

    @PostMapping("/donate")
    public String doDonation(@RequestParam String date, @Valid Donation donation, BindingResult result, Model model){

        if(result.hasErrors()){
            return "donation";
        }else {
            try{
                donation.setPickUpDate(LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
            }catch (DateTimeParseException e){
                model.addAttribute("dateErr", "Nieprawidłowy format daty");
                return "donation";
            }
            if(donation.getPickUpDate().isBefore(LocalDate.now())){
                model.addAttribute("dateErr", "Ten termin już minął");
                return "donation";
            }
        }
        System.out.println(">>>>>>>>Donation");
        System.out.println(donation);
        return "redirect:/";
    }
}
