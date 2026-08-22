package ar.edu.utn.frc.tup.rodigym.controllers;

import ar.edu.utn.frc.tup.rodigym.dtos.CheckinResponseDto;
import ar.edu.utn.frc.tup.rodigym.dtos.PageResponseDto;
import ar.edu.utn.frc.tup.rodigym.models.Checkin;
import ar.edu.utn.frc.tup.rodigym.services.CheckinService;
import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling gym check-ins.
 */
@RestController
@RequestMapping("/checkins")
public class CheckinController {

    private final CheckinService checkinService;
    private final ModelMapper modelMapper;

    /**
     * Constructor.
     */
    public CheckinController(CheckinService checkinService, ModelMapper modelMapper) {
        this.checkinService = checkinService;
        this.modelMapper = modelMapper;
    }

    /**
     * Performs a check-in for a member given their DNI (member ID).
     *
     * @param memberId The DNI of the member.
     * @return The check-in result.
     */
    @PostMapping("/{memberId}")
    public ResponseEntity<CheckinResponseDto> performCheckin(@PathVariable Long memberId) {
        Checkin checkin = checkinService.performCheckin(memberId);
        CheckinResponseDto responseDto = modelMapper.map(checkin, CheckinResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Busca ingresos con filtros opcionales y paginación.
     *
     * <p>Todos los filtros son opcionales: los que no se informan no participan de
     * la query. El rango de fechas es semiabierto, {@code from} incluido y
     * {@code to} excluido.</p>
     *
     * @param memberId filtra por socio.
     * @param success  true para los permitidos, false para los rechazados.
     * @param from     fecha desde, inclusive.
     * @param to       fecha hasta, exclusive.
     * @param pageable página y orden; por defecto los 10 más recientes.
     * @return la página de ingresos encontrados.
     */
    @GetMapping
    public ResponseEntity<PageResponseDto<CheckinResponseDto>> searchCheckins(
            @RequestParam(name = "member_id", required = false) Long memberId,
            @RequestParam(name = "success", required = false) Boolean success,
            @RequestParam(name = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(name = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @PageableDefault(size = 10, sort = "checkinTime", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<Checkin> checkins =
                checkinService.searchCheckins(memberId, success, from, to, pageable);

        return ResponseEntity.ok(PageResponseDto.from(checkins,
                checkin -> modelMapper.map(checkin, CheckinResponseDto.class)));
    }
}
