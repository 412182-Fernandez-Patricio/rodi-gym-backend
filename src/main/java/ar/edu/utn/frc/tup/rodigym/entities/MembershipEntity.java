package ar.edu.utn.frc.tup.rodigym.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "memberships")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "price", nullable = false)
    private Double price;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private MemberEntity member;
}
