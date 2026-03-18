package ar.edu.utn.frc.tup.rodigym.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Membership {
    Long id;
    LocalDate startDate;
    LocalDate expirationDate;
    Double price;
}

