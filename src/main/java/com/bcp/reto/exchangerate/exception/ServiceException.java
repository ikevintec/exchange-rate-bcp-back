package com.bcp.reto.exchangerate.exception;


import com.bcp.reto.exchangerate.enums.ErrorCode;
import lombok.Data;


@Data
public class ServiceException extends RuntimeException{
    private ApiError error;

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.error = errorCode.getApiError();
    }

    public ServiceException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage());
        this.error = errorCode.getApiError();
        this.error.setDetail(detail);
    }

}
