package com.tonthepduybao.api.app.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.function.Supplier;

/**
 * ExistenceException
 *
 * @author khale
 * @since 2022/10/24
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataException extends RuntimeException {

    private String message;
    private List<Object> args;

    public DataException(final String message) {
        super(message);
    }

    public static Supplier<DataException> supplier(final String message) {
        return () -> new DataException(message);
    }
    
    public static Supplier<DataException> supplier(final String message, final List<Object> args) {
        return () -> new DataException(message, args);
    }

}
