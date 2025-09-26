package com.tonthepduybao.api.model.invoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceForm {

    @NotBlank
    @Length(max = 100)
    private String id;

    @NotBlank
    @Length(max = 8)
    private String date;

    @Length(max = 1000)
    private String note;

    @NotNull
    private Long customerId;

    @NotNull
    private Long branchId;

    @NotNull
    private Long shippingAddressId;

    @NotEmpty
    private List<InvoiceProductForm> items;

}