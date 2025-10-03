package com.tonthepduybao.api.app.exception.model;

/**
 * SystemException
 *
 * @author khale
 * @since 2022/10/24
 */
public class SystemException extends RuntimeException {
    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
