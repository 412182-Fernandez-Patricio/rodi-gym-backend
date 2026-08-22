package ar.edu.utn.frc.tup.rodigym.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * Respuesta paginada con forma propia.
 *
 * <p>Se usa en lugar de devolver el {@code Page} de Spring directamente porque el
 * JSON de {@code PageImpl} no es un contrato estable entre versiones, y Spring Boot
 * avisa por log cuando se serializa.</p>
 *
 * @param <T> tipo de los elementos de la página.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto<T> {
    private List<T> content;
    private int page;
    private int size;
    @JsonProperty("total_elements")
    private long totalElements;
    @JsonProperty("total_pages")
    private int totalPages;
    private boolean last;

    /**
     * Arma la respuesta a partir de una página del dominio, mapeando cada elemento.
     *
     * @param page   página devuelta por el repositorio.
     * @param mapper conversión de cada elemento a su DTO.
     * @param <S>    tipo de origen.
     * @param <T>    tipo del DTO.
     * @return la página lista para serializar.
     */
    public static <S, T> PageResponseDto<T> from(Page<S> page, Function<S, T> mapper) {
        return new PageResponseDto<>(
                page.getContent().stream().map(mapper).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }
}
