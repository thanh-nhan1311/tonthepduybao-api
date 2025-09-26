package com.tonthepduybao.api.app.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ErrorMessageModel
 *
 * @author khal
 * @since 2022/06/13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {

    private String message;
    private Integer code;

}
