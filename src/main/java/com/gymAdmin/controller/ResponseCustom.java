package com.gymAdmin.controller;

public record ResponseCustom (Boolean success, String message, Object data)
{
    public static ResponseCustom success(Object data){
        return new ResponseCustom(true, "Operacion exitosa", data);
    }

    public static ResponseCustom error(String message){
        return new ResponseCustom(false, message, null);
    }
}
