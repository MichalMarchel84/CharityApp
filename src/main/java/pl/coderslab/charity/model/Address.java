package pl.coderslab.charity.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    private User user;
    @NotBlank(message = "Nie podano ulicy")
    private String street;
    @NotBlank(message = "Nie podano miasta")
    private String city;
    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}", message = "Niepoprawny kod pocztowy")
    private String postCode;
    @Pattern(regexp = "^ *$|^(\\+[0-9]{2})?[0-9 ]{9,}$", message = "Niepoprawny numer telefonu")
    private String phone;

    @Override
    public String toString(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
