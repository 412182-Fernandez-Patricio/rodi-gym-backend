package ar.edu.utn.frc.tup.rodigym.exceptions;

/**
 * Se intentó dar de alta un socio con un DNI que ya está registrado, esté activo
 * o dado de baja.
 *
 * <p>Tiene su propio tipo para responder 409: IllegalArgumentException ya se usa
 * para otros 400 y habría que distinguirlos por el texto.</p>
 */
public class MemberAlreadyExistsException extends RuntimeException {

    /**
     * Crea la excepción para el DNI repetido.
     *
     * @param id DNI del socio que ya existe.
     */
    public MemberAlreadyExistsException(Long id) {
        super("Member already exists with id: " + id);
    }
}
