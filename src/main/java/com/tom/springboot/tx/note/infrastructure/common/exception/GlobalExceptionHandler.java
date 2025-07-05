package com.tom.springboot.tx.note.infrastructure.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName GlobalExceptionHandler.java
 * @Description TODO
 * @createTime 2025年03月06日 21:20:00
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        // do nothing.
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<TomWebModel> handleException(Exception ex, WebRequest request) {
        // 打印异常栈
        log.error("GlobalExceptionHandler#handleException() method handle exception. ", ex);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof TomWebException tomWebException) {
            return new ResponseEntity<>(new TomWebModel(tomWebException), headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(new TomWebModel("000000", ex.getMessage()), headers, HttpStatus.OK);
    }

    public void f1() {
        EnableTransactionManagement enableTransactionManagement;
        TransactionInterceptor transactionInterceptor;
        TransactionAspectSupport transactionAspectSupport;

    }
}
