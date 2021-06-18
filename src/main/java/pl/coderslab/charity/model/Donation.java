package pl.coderslab.charity.model;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull(message = "Nie została podana ilość oddawanych worków")
    @Range(min = 1, message = "Ilość oddawanych worków nie może być mniejsza od 1")
    private Integer quantity;
    @ManyToMany
    @Size(min = 1, message = "Brak kategorii oddawanych przedmiotów")
    private Set<Category> categories;
    @ManyToOne
    @NotNull(message = "Nie została wskazana żadna instytucja")
    private Institution institution;
    @ManyToOne
    @NotNull
    private Address address;
    private LocalDate pickUpDate;
    private LocalTime pickUpTime;
    private String pickUpComment;
    @NotBlank(message = "Brak adresu email")
    @Email(message = "Niepoprawny adres email")
    private String email;
    @ManyToOne
    private User user;
}
