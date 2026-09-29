package ar.edu.utn.frc.tup.rodigym.controllers;

import ar.edu.utn.frc.tup.rodigym.dtos.ConfigResponseDto;
import ar.edu.utn.frc.tup.rodigym.services.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
     * Devuelve la cuota mensual que va a cobrar el próximo pago.
     *
     * <p>Sale de {@link ConfigService#getMonthlyPrice()}, lo mismo que usa el alta
     * de pagos, y no de la tabla directo: así el monto que se muestra antes de
     * cobrar y el que se cobra no pueden diferir, ni con el valor por defecto.</p>
     *
     * @return la cuota, con la misma forma que la respuesta del PUT.
     */
    @GetMapping("/monthly-price")
    public ResponseEntity<ConfigResponseDto> getMonthlyPrice() {
        return ResponseEntity.ok(new ConfigResponseDto("monthly_price",
                configService.getMonthlyPrice().toString()));
    }

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
