package ar.edu.utn.frc.tup.rodigym.services.impl;

import ar.edu.utn.frc.tup.rodigym.entities.ConfigEntity;
import ar.edu.utn.frc.tup.rodigym.repositories.ConfigRepository;
import ar.edu.utn.frc.tup.rodigym.services.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    private static final String MONTHLY_PRICE_KEY = "monthly_price";
    private static final Double DEFAULT_PRICE = 5000.0;

    @Override
    public Double getMonthlyPrice() {
        return configRepository.findById(MONTHLY_PRICE_KEY)
                .map(config -> Double.parseDouble(config.getValue()))
                .orElse(DEFAULT_PRICE);
    }

    @Override
    public void saveConfig(String key, String value) {
        ConfigEntity configEntity = new ConfigEntity(key, value);
        configRepository.save(configEntity);
    }
}
