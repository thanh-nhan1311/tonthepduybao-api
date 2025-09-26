package com.tonthepduybao.api.app.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.function.Supplier;

/**
 * S3Exception
 *
 * @author khale
 * @since 2022/10/24
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class S3Exception extends RuntimeException {

    private List<Object> args;

    public static Supplier<S3Exception> supplier(final List<Object> args) {
        return () -> new S3Exception(args);
    }


}
