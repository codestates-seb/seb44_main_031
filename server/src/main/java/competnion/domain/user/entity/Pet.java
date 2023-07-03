package competnion.domain.user.entity;

import com.sun.istack.NotNull;
import competnion.global.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "pets")
@NoArgsConstructor(access = PROTECTED)
public class Pet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;
    @NotNull
    private String name;
    private String imgUrl;
    @NotNull
    private LocalDateTime birth;
    @NotNull
    private Boolean gender;
    @NotNull
    private Boolean isNeutered;
    private String inoculated;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
