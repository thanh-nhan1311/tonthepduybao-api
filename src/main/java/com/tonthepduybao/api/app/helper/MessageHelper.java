package com.tonthepduybao.api.app.helper;

import com.tonthepduybao.api.app.exception.model.ResponseMessage;

import java.util.List;
import java.util.function.Supplier;

/**
 * MessageHelper
 *
 * @author khal
 * @since 2023/02/11
 */
public interface MessageHelper {

    String get(String messageCode);

    ResponseMessage build(String message, int status);

    ResponseMessage build(String message, int status, List<Object> args);

    Supplier build(Class e, String messageCode, Object... args);

    Supplier buildDataNotFound(Object... args);

    Supplier buildUnauthorized();

}
