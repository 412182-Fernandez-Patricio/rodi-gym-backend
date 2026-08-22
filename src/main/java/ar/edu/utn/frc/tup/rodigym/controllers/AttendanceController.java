package ar.edu.utn.frc.tup.rodigym.controllers;

import ar.edu.utn.frc.tup.rodigym.dtos.AttendanceDayDto;
import ar.edu.utn.frc.tup.rodigym.models.AttendanceDay;
import ar.edu.utn.frc.tup.rodigym.services.CheckinService;
import java.time.YearMonth;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Asistencia mensual de un socio, para el calendario del perfil.
 */
@RestController
@RequestMapping("/members/{memberId}/attendance")
public class AttendanceController {

    private final CheckinService checkinService;
    private final ModelMapper modelMapper;

    /**
     * Constructor.
     */
    public AttendanceController(CheckinService checkinService, ModelMapper modelMapper) {
        this.checkinService = checkinService;
        this.modelMapper = modelMapper;
    }

    /**
     * Días con actividad del socio en un mes.
     *
     * <p>Devuelve una lista y no un mapa por fecha a propósito: el cliente
     * convierte las claves de los objetos a camelCase, así que una clave que sea
     * un dato terminaría reescrita.</p>
     *
     * @param memberId socio a consultar.
     * @param month    mes en formato aaaa-MM; si no se informa, el actual.
     * @return un elemento por día con actividad, en orden cronológico.
     */
    @GetMapping
    public ResponseEntity<List<AttendanceDayDto>> getAttendance(
            @PathVariable Long memberId,
            @RequestParam(name = "month", required = false)
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        List<AttendanceDay> days = checkinService.getAttendance(
                memberId, month == null ? YearMonth.now() : month);

        return ResponseEntity.ok(days.stream()
                .map(day -> modelMapper.map(day, AttendanceDayDto.class))
                .toList());
    }
}
