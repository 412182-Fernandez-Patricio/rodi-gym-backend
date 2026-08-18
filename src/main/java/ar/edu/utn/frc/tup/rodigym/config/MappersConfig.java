package ar.edu.utn.frc.tup.rodigym.config;

import ar.edu.utn.frc.tup.rodigym.dtos.MemberResponseDto;
import ar.edu.utn.frc.tup.rodigym.models.Member;
import ar.edu.utn.frc.tup.rodigym.models.Membership;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
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
        mapper.typeMap(Member.class, MemberResponseDto.class).addMappings(m ->
                m.using(ctx -> ctx.getSource() == null
                                ? null
                                : ((Membership) ctx.getSource()).getExpirationDate())
                        .<LocalDate>map(Member::getMembership,
                                MemberResponseDto::setExpirationDate));
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
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
