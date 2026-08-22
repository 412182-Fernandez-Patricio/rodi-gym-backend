package ar.edu.utn.frc.tup.rodigym.enums;

/**
 * Motivo por el que un intento de ingreso fue permitido o rechazado.
 *
 * <p>Cada motivo lleva su mensaje asociado, para que el código y el texto no
 * puedan quedar desalineados: el cliente traduce a partir del código y el mensaje
 * queda como apoyo para diagnóstico.</p>
 */
public enum CheckinReason {

    ACCESS_GRANTED("Access granted"),
    MEMBER_INACTIVE("Member is not active"),
    MEMBERSHIP_EXPIRED("Membership expired or not found");

    private final String message;

    CheckinReason(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Si el intento terminó en ingreso permitido.
     *
     * @return true solo para el acceso concedido.
     */
    public boolean isSuccessful() {
        return this == ACCESS_GRANTED;
    }
}
