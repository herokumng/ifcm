package com.dkbmc.ifcm.dto.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ResponseDTO {
        private Object result;
        private String error_code;
        private String error_msg;
        private String return_msg;
}
