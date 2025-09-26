package com.tonthepduybao.api.app.exception;

import com.tonthepduybao.api.app.helper.MessageHelper;
import com.tonthepduybao.api.app.constant.MessageConstant;
import com.tonthepduybao.api.app.exception.model.*;
import com.tonthepduybao.api.app.logging.Logger;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * AppExceptionHandler
 *
 * @author khal
 * @since 2022/10/02
 */
@ControllerAdvice
public class ExceptionHandler {

    private final MessageHelper messageHelper;

    public ExceptionHandler(final MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseBody
    public ResponseMessage handleException(final Exception e, final HttpServletResponse response) {
        int status = HttpServletResponse.SC_BAD_REQUEST;

        ResponseMessage error;
        List<Object> args;

        if (e instanceof ConstraintViolationException) {
            String message = messageHelper.get(MessageConstant.SYS_INVALID_PARAMETER);

            error = messageHelper.build(message, status);
        } else if (e instanceof InternalAuthenticationServiceException
                || e instanceof UsernameNotFoundException) {
            status = HttpServletResponse.SC_UNAUTHORIZED;
            String message = messageHelper.get(MessageConstant.SYS_UNAUTHORIZED);

            error = messageHelper.build(message, status);
        } else if (e instanceof BadCredentialsException) {
            status = HttpServletResponse.SC_UNAUTHORIZED;
            error = messageHelper.build(e.getMessage(), status);
        } else if (e instanceof S3Exception exception) {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            String message = messageHelper.get(MessageConstant.S3_UPLOAD_FAILED);
            args = exception.getArgs();

            error = messageHelper.build(message, status, args);
        } else if (e instanceof final DataException exception) {
            status = HttpServletResponse.SC_NOT_FOUND;
            String message = exception.getMessage();
            args = exception.getArgs();

            error = messageHelper.build(message, status, args);
        } else if (e instanceof ParameterException exception) {
            String message = messageHelper.get(MessageConstant.SYS_INVALID_PARAMETER);
            args = exception.getArgs();

            error = messageHelper.build(message, status, args);
        } else if (e instanceof ExistenceException exception) {
            String message = messageHelper.get(MessageConstant.SYS_DUPLICATED_DATA);
            args = exception.getArgs();

            error = messageHelper.build(message, status, args);
        } else {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            String message = messageHelper.get(MessageConstant.SYS_PROCEED_FAILED);

            error = messageHelper.build(message, status);
        }

        response.setStatus(status);
        Logger.ERROR_LOGGER.error(error.getMessage(), e);

        return error;
    }

}
