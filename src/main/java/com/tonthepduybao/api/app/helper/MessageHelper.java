package com.tonthepduybao.api.app.helper;

import com.tonthepduybao.api.app.exception.model.ResponseMessage;

import java.util.List;
import java.util.function.Supplier;

/*public interface MessageHelper {

    String get(String messageCode);

    ResponseMessage build(String message, int status);

    ResponseMessage build(String message, int status, List<Object> args);

    //Supplier build(Class e, String messageCode, Object... args);
    <S> Supplier<S> build(Class<S> e, String messageCode, S... args);

    Supplier buildDataNotFound(Object... args);

    Supplier buildUnauthorized();

}*/
public interface MessageHelper {

    String get(String messageCode);

    ResponseMessage build(String message, int status);

    ResponseMessage build(String message, int status, List<Object> args); // Sử dụng List<?> thay vì List<Object>

    <S> Supplier<S> build(Class<S> e, String messageCode, S... args); // Sử dụng Generics rõ ràng

    Supplier<? extends RuntimeException> buildDataNotFound(Object... args); // Giữ Object... args nhưng có thể kiểm tra lại

    Supplier<? extends RuntimeException> buildUnauthorized();
}
