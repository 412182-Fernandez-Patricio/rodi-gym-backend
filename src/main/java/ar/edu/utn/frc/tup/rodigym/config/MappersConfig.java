package ar.edu.utn.frc.tup.rodigym.config;

import ar.edu.utn.frc.tup.rodigym.dtos.PaymentResponseDto;
import ar.edu.utn.frc.tup.rodigym.entities.PaymentEntity;
import ar.edu.utn.frc.tup.rodigym.models.Payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for ModelMapper and ObjectMapper beans.
 */
@Configuration
public class MappersConfig {

    /**
     * Creates a standard ModelMapper bean.
     *
     * @return a new ModelMapper instance.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        
        // Custom mapping for PaymentEntity to Payment model
        mapper.typeMap(PaymentEntity.class, Payment.class).addMappings(m -> {
            m.map(src -> src.getMember().getId(), Payment::setMember);
        });

        // Custom mapping for PaymentEntity to PaymentResponseDto
        mapper.typeMap(PaymentEntity.class, PaymentResponseDto.class).addMappings(m -> {
            m.map(src -> src.getMember().getId(), PaymentResponseDto::setMemberId);
        });

        // Custom mapping for Payment model to PaymentResponseDto (needed for Controller)
        mapper.typeMap(Payment.class, PaymentResponseDto.class).addMappings(m -> {
            m.map(Payment::getMember, PaymentResponseDto::setMemberId);
        });

        return mapper;
    }

    /**
     * Creates a ModelMapper bean configured to merge only non-null properties.
     *
     * @return a configured ModelMapper instance.
     */
    @Bean("mergerMapper")
    public ModelMapper mergerMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setPropertyCondition(Conditions.isNotNull());
        return mapper;
    }

    /**
     * Creates an ObjectMapper bean with JavaTimeModule registered.
     *
     * @return a configured ObjectMapper instance.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
