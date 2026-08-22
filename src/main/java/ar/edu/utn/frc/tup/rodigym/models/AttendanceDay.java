package ar.edu.utn.frc.tup.rodigym.models;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Un día con actividad de un socio, ya agrupado.
 *
 * <p>El calendario marca días, no eventos: si alguien entró a la mañana y a la
 * tarde, es un solo día.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDay {
    private LocalDate date;
    /** Verdadero si al menos uno de los ingresos de ese día fue permitido. */
    private boolean success;
    /** Cantidad de intentos de ingreso registrados ese día, exitosos o no. */
    private int checkins;
}
