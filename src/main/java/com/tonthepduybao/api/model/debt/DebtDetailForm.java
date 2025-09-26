package com.tonthepduybao.api.model.debt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DebtCreateForm
 *
 * @author khal
 * @since 2023/07/09
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebtDetailForm {

        Long id;

        @NotBlank @Length(max = 500)
        String name;

        String note;

        @NotEmpty
        Map<Long, Long> properties;

        @NotNull
        Double weight;

        @NotNull
        Integer quantity;

        @NotNull
        Double avgProportion;

        @NotNull
        BigDecimal unitPrice;

        @NotNull
        BigDecimal totalPrice;

        @NotNull
        BigDecimal totalUnitPrice;

        @NotNull
        Long branch;

}
