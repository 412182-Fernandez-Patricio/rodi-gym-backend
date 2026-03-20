package ar.edu.utn.frc.tup.rodigym.controllers;

import ar.edu.utn.frc.tup.rodigym.dtos.CheckinResponseDto;
import ar.edu.utn.frc.tup.rodigym.models.Checkin;
import ar.edu.utn.frc.tup.rodigym.services.CheckinService;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * Gets all check-ins.
     *
     * @return List of check-ins.
     */
    @GetMapping
    public ResponseEntity<List<CheckinResponseDto>> getAllCheckins() {
        List<Checkin> checkins = checkinService.getAllCheckins();
        List<CheckinResponseDto> responseDtos = checkins.stream()
                .map(checkin -> modelMapper.map(checkin, CheckinResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
}
