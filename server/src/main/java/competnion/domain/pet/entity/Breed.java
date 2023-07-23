package competnion.domain.pet.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "breeds")
@NoArgsConstructor(access = PROTECTED)
public class Breed {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "breed_id", nullable = false)
    private Long id;
    @Column(nullable = false)
    @NotBlank
    private String name;
    @OneToMany(mappedBy = "breed")
    List<Pet> pets = new ArrayList<>();
}
