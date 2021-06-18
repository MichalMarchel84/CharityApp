package pl.coderslab.charity.controller;

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
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Controller
public class DonationController {

    private final CategoryRepository categoryRepository;
    private final InstitutionRepository institutionRepository;
    private final UserService userService;
    private final DonationService donationService;
    private final Validator validator;

    public DonationController(CategoryRepository categoryRepository, InstitutionRepository institutionRepository, UserService userService, DonationService donationService, Validator validator) {
        this.categoryRepository = categoryRepository;
        this.institutionRepository = institutionRepository;
        this.userService = userService;
        this.donationService = donationService;
        this.validator = validator;
    }

    @GetMapping("/donate")
    public String donation(@AuthenticationPrincipal UserDetails user, Model model){

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("institutions", institutionRepository.findAll());
        model.addAttribute("donation", new Donation());
        if(user != null) {
            model.addAttribute("userEmail", user.getUsername());
            model.addAttribute("addresses", userService.getUserAddresses(user.getUsername()));
        }
        return "donation";
    }

    @PostMapping("/donate")
    public String doDonation(@RequestParam String date,
                             @AuthenticationPrincipal UserDetails user,
                             @Valid Donation donation,
                             BindingResult result,
                             HttpServletRequest request,
                             Model model){

        if(result.hasErrors()){
            StringBuilder sb = new StringBuilder();
            result.getFieldErrors().forEach(err -> {
                sb.append(err.getDefaultMessage()).append("<br/>");
            });
            model.addAttribute("pageMsg", "Nie udało się zapisać formularza<br/>Formularz zawierał błędy:<br/>" + sb.toString());
            return "blank";
        }else {
            var adrViolations = validator.validate(donation.getAddress());
            if(!adrViolations.isEmpty()){
                StringBuilder sb = new StringBuilder();
                adrViolations.forEach(err -> {
                    sb.append(err.getMessage()).append("<br/>");
                });
                model.addAttribute("pageMsg", "Nie udało się zapisać formularza<br/>Adres zawierał błędy:<br/>" + sb.toString());
                return "blank";
            }
            try{
                donation.setPickUpDate(LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
            }catch (DateTimeParseException e){
                model.addAttribute("pageMsg", "Nie udało się zapisać formularza<br/>Błędny format daty");
                return "blank";
            }
            if(donation.getPickUpDate().isBefore(LocalDate.now().plusDays(1))){
                model.addAttribute("pageMsg", "Nie udało się zapisać formularza<br/>Podano zbyt wczesną datę odbioru");
                return "blank";
            }
            if(
                    (donation.getPickUpTime() == null) ||
                    donation.getPickUpTime().isBefore(LocalTime.of(8, 0)) ||
                    donation.getPickUpTime().isAfter(LocalTime.of(19, 59))
            ){
                model.addAttribute("pageMsg", "Nie udało się zapisać formularza<br/>Niewłaściwa godzina odbioru: kurier odbiera przesyłki od 8 do 20");
                return "blank";
            }
        }
        boolean ok = true;
        if(user != null) {
            donation.setUser(userService.findByEmail(user.getUsername()));
            if (request.getParameter("saveAdr") != null) {
                Long adrId = userService.saveAddress(user.getUsername(), donation.getAddress());
                if(adrId != null) {
                    ok = donationService.saveDonation(adrId, donation);
                }else{
                    ok = false;
                }
            }else if(!request.getParameter("adrId").isEmpty()){
                try{
                    Long adrId = Long.parseLong(request.getParameter("adrId"));
                    ok = donationService.saveDonation(adrId, donation);
                }catch (NumberFormatException e){
                    ok = false;
                }
            }else donationService.saveDonation(donation);
        }else donationService.saveDonation(donation);

        if(ok) model.addAttribute("pageMsg", "Dziękujemy za przesłanie formularza.<br/> Na maila prześlemy wszelkie informacje o odbiorze.");
        else {
            model.addAttribute("pageMsg", "Nie udało się zapisać formularza<br/>Wystąpił nieoczekiwany błąd");
            return "blank";
        }
        return "blank";
    }
}
