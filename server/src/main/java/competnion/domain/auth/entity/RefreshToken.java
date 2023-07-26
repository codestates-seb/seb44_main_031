package competnion.domain.auth.entity;

import lombok.*;


import javax.persistence.*;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tokenId;

    @Column (nullable = false)
    private long userId;

    @Column (nullable = false)
    private String encodedJws;


}
