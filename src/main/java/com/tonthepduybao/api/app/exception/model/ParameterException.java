package com.tonthepduybao.api.app.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.function.Supplier;

/**
 * ParameterException
 *
 * @author khale
 * @since 2022/10/24
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParameterException extends RuntimeException {

    private List<Object> args;

    public static Supplier<ParameterException> supplier(final List<Object> args) {
        return () -> new ParameterException(args);
    }

}
