package pl.coderslab.charity.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "institutions")
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String description;
    private Boolean deleted;
}
