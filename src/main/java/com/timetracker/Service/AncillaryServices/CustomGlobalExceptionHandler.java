package com.timetracker.Service.AncillaryServices;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

    private HttpHeaders jsonHeader;

    @PostConstruct
    public void init() throws Exception {
        jsonHeader = new HttpHeaders();
        jsonHeader.add("Content-type", "application/json;charset=UTF-8");
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<ResponseMessage> handlerNullPointerException(HttpServletResponse response, Exception e) {
        ResponseMessage responseMessage = new ResponseMessage(false, e.getMessage());
        return new ResponseEntity<>(responseMessage, getJsonHeader(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {DateTimeException.class})
    public ResponseEntity<ResponseMessage> handlerDateTimeException(HttpServletResponse response, Exception e) {
        ResponseMessage responseMessage = new ResponseMessage(false, e.getMessage());
        return new ResponseEntity<>(responseMessage, jsonHeader, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage> onValidationException(MethodArgumentNotValidException e, HttpServletResponse response) throws IOException {
        List<String> errorList = new ArrayList<>(e.getBindingResult().getAllErrors().size());
        BindingResult bindingResult = e.getBindingResult();

        for (FieldError error : bindingResult.getFieldErrors())
            errorList.add(error.getDefaultMessage());

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(false);
        responseMessage.setMessage("Необходимо корректно заполнить поле(я) для запроса");
        responseMessage.addResponseObject("errorList", errorList);
        return new ResponseEntity<>(responseMessage, jsonHeader, HttpStatus.BAD_REQUEST);
    }

    public HttpHeaders getJsonHeader() {
        return jsonHeader;
    }
}
