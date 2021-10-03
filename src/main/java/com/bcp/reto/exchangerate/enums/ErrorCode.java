package com.bcp.reto.exchangerate.enums;

import com.bcp.reto.exchangerate.exception.ApiError;
import lombok.Getter;

@Getter
public enum ErrorCode {

    E000("E000","Error desconocido"),
    E001("E001","Moneda no encontrada"),
    E002("E002","Moneda nacional debe tener tipo de cambio (1)"),
    E003("E003","Ya existe registrada una moneda nacional"),
    E004("E004","El código de moneda ya esta registrado"),
    E005("E005","Configure moneda nacional"),
    E006("E006","Moneda origen no configurada"),
    E007("E007","Moneda destino no configurada");

    String code;
    String message;

    ErrorCode(String code, String message){
        this.code = code;
        this.message=message;
    }

    public ApiError getApiError() {
        return ApiError.builder()
                .code(this.code)
                .message(this.message)
                .build();
    }

}
