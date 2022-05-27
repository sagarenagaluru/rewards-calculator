package com.rewards.rewardscalculator.advice;
/*
 *Author : Sagar Enagaluru
 */

import com.rewards.rewardscalculator.advice.customExceptions.NoTransactionFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionsAdviceController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoTransactionFoundException.class)
    public Map<String, String> transactionNotFound(NoTransactionFoundException tnxException) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", tnxException.getMessage());
        return errorMap;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, String> unHandled(Exception ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("internal_server_error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }
}
