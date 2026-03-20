package ar.edu.utn.frc.tup.rodigym.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Member model.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    Long id;
    String name;
    String lastName;
    String phoneNumber;
    Boolean status;
    Membership membership;
}

