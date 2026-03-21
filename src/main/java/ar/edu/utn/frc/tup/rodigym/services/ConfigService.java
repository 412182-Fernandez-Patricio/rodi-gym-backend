package ar.edu.utn.frc.tup.rodigym.services;

import org.springframework.stereotype.Service;

@Service
public interface ConfigService {
    Double getMonthlyPrice();
    void saveConfig(String key, String value);
}
