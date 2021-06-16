package pl.coderslab.charity.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Table(name = "user_addresses")
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    private User user;
    @NotBlank
    private String street;
    @NotBlank
    private String city;
    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}")
    private String postCode;
    private String phone;
}
