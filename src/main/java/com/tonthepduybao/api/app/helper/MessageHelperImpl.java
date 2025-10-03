package com.tonthepduybao.api.app.helper;

import com.tonthepduybao.api.app.constant.Constant;
import com.tonthepduybao.api.app.constant.MessageConstant;
import com.tonthepduybao.api.app.exception.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * MessageHelperImpl
 *
 * @author khal
 * @since 2023/02/11
 */
@Component
@RequiredArgsConstructor
public class MessageHelperImpl implements MessageHelper {

    private final Constant constant;
    private final MessageSource messageSource;

    @Override
    public String get(final String messageCode) {
        return messageSource.getMessage(messageCode, null, new Locale(constant.getLocale()));
    }

    @Override
    public ResponseMessage build(final String message, final int status) {
        return new ResponseMessage(message, status);
    }

    @Override
    public ResponseMessage build(final String message, final int status, final List<Object> args) {
        String msg = message;

        if (!CollectionUtils.isEmpty(args)) {
            for (int argIndex = 0; argIndex < args.size(); argIndex++) {
                String argName = "{arg" + argIndex + "}";
                String argValue = String.valueOf(args.get(argIndex))
                        .replace("[", "")
                        .replace("]", "");

                msg = msg.replace(argName, argValue);
            }
        }

        return new ResponseMessage(msg, status);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> Supplier<S> build(final Class<S> clazz, final String messageCode, final Object... args) {
        String message = get(messageCode);
        List<Object> listArgs = List.of(args);

        // If requested exception is DataException (or subclass), return DataException supplier
        if (DataException.class.equals(clazz) || DataException.class.isAssignableFrom((Class<?>) clazz)) {
            return (Supplier<S>) DataException.supplier(message, listArgs);
        }

        // For other exceptions, try to create a supplier that constructs the exception with a message via reflection
        try {
            // Try constructor with (String) parameter
            var ctor = clazz.getDeclaredConstructor(String.class);
            return () -> {
                try {
                    return (S) ctor.newInstance(message);
                } catch (Exception ex) {
                    throw new SystemException(message, ex);
                }
            };
        } catch (NoSuchMethodException ignored) {
            // Fall back to supplier that throws SystemException with message
        }

        return () -> {
            throw new SystemException(message);
        };
    }

    @Override
    public Supplier<? extends RuntimeException> buildDataNotFound(final Object... args) {
        return build(DataException.class, MessageConstant.SYS_DATA_NOT_FOUND, args);
    }

    @Override
    public Supplier<? extends RuntimeException> buildUnauthorized() {
        String message = get(MessageConstant.SYS_UNAUTHORIZED);
        return () -> new BadCredentialsException(message);
    }

}
