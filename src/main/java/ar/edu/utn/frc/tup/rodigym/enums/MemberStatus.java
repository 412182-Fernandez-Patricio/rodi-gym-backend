package ar.edu.utn.frc.tup.rodigym.enums;

/**
 * Estado con el que se muestra un socio, derivado de su baja lógica y de la
 * vigencia de su membresía.
 *
 * <p>No se persiste: existe para filtrar, y replica el criterio de
 * CheckinServiceImpl al decidir si dejar entrar a alguien.</p>
 */
public enum MemberStatus {

    /** Activo y con la membresía vigente. */
    ACTIVE,

    /** Activo pero con la membresía vencida, o directamente sin membresía. */
    EXPIRED,

    /** Dado de baja, sin importar el estado de su membresía. */
    INACTIVE
}
