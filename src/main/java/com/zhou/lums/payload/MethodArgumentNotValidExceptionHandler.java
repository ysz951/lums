package com.zhou.lums.payload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        BindingResult result = ex.getBindingResult();

        List<String> errorList = new ArrayList<>();
        Map<String, String> errorMap = new HashMap<>();
        result.getFieldErrors().forEach((fieldError) -> {
            // errorList.add(fieldError.getObjectName()+"."+fieldError.getField()+" : " +fieldError.getDefaultMessage() +" : rejected value [" +fieldError.getRejectedValue() +"]" );
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage() +" : rejected value [" +fieldError.getRejectedValue() +"]" );
        });
        result.getGlobalErrors().forEach((fieldError) -> {
            // errorList.add(fieldError.getObjectName()+" : " +fieldError.getDefaultMessage() );
            errorMap.put(fieldError.getObjectName(), fieldError.getDefaultMessage());
        });

        return new Error(HttpStatus.BAD_REQUEST, ex.getMessage(), errorMap);
    }

    public static class Error{
        private int errorCode;
        private String error;
        private String errorMessage;
        // private List<String> fieldErrors = new ArrayList<>();
        private Map<String, String> fieldErrors = new HashMap<>();
        public Error(HttpStatus status, String message, Map<String, String> fieldErrors) {
            this.errorCode = status.value();
            this.error = status.name();
            this.errorMessage = message;
            this.fieldErrors = fieldErrors;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public Map<String, String>  getFieldErrors() {
            return fieldErrors;
        }

        public void setFieldErrors(Map<String, String> fieldErrors) {
            this.fieldErrors = fieldErrors;
        }
    }
}