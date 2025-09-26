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
    public Supplier build(final Class clazz, final String messageCode, final Object... args) {
        String message = get(messageCode);
        List<Object> listArgs = List.of(args);

        try {
            Exception e = (Exception) clazz.getDeclaredConstructor().newInstance();

            if (e instanceof DataException)
                return DataException.supplier(message, listArgs);

            return () -> new SystemException();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return () -> new SystemException();
    }

    @Override
    public Supplier buildDataNotFound(final Object... args) {
        return build(DataException.class, MessageConstant.SYS_DATA_NOT_FOUND, args);
    }

    @Override
    public Supplier buildUnauthorized() {
        String message = get(MessageConstant.SYS_UNAUTHORIZED);
        return () -> new BadCredentialsException(message);
    }

}
