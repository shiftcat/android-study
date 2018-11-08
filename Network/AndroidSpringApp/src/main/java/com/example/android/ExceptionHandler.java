package com.example.android;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@ControllerAdvice
public class ExceptionHandler
{


    public ResponseEntity<?> makeResponse(HttpStatus status, List<String> msgs)
    {
        Map<String, Object> res = new HashMap<>();
        res.put("status", status);
        res.put("messages", msgs);
        return new ResponseEntity(res, status);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<?> handleBindError(HttpServletRequest req, BindException exception) throws Exception
    {
        log.warn(exception.getMessage());
        List<FieldError> filedErrors = exception.getBindingResult().getFieldErrors();
        return makeResponse(HttpStatus.BAD_REQUEST, filedErrors.stream().map(fe -> fe.getField() + ": " + fe.getDefaultMessage()).collect(Collectors.toList()));
    }



    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<?> handleNotNullError(HttpServletRequest req, MethodArgumentNotValidException exception) throws Exception
    {
        log.warn(exception.getMessage());
        List<FieldError> filedErrors = exception.getBindingResult().getFieldErrors();
        return makeResponse(HttpStatus.BAD_REQUEST, filedErrors.stream().map(fe -> fe.getField() + ": " + fe.getDefaultMessage()).collect(Collectors.toList()));
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<?> handleMessageNotReadableError(HttpServletRequest req, HttpMessageNotReadableException exception) throws Exception
    {
        log.warn(exception.getMessage());
        return makeResponse(HttpStatus.BAD_REQUEST, Arrays.asList(exception.getMessage()));
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleInternalError(HttpServletRequest req, Exception exception) throws Exception
    {
        exception.printStackTrace();
        log.error(exception.getMessage());
        return makeResponse(HttpStatus.INTERNAL_SERVER_ERROR, Arrays.asList(exception.getMessage()));
    }
}
