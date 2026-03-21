package ar.edu.utn.frc.tup.rodigym.controllers;

import ar.edu.utn.frc.tup.rodigym.dtos.ConfigResponseDto;
import ar.edu.utn.frc.tup.rodigym.services.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    /**
     * Updates the monthly price in the configuration.
     *
     * @param price the new monthly price.
     * @return the updated configuration details.
     */
    @PutMapping("/monthly-price")
    public ResponseEntity<ConfigResponseDto> updateMonthlyPrice(@RequestParam Double price) {
        configService.saveConfig("monthly_price", price.toString());
        ConfigResponseDto response = new ConfigResponseDto("monthly_price", price.toString());
        return ResponseEntity.ok(response);
    }
}
