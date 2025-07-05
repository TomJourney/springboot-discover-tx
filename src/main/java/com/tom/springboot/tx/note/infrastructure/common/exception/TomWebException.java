package com.tom.springboot.tx.note.infrastructure.common.exception;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName TomWebException.java
 * @Description TODO
 * @createTime 2025年03月06日 21:21:00
 */
public class TomWebException extends RuntimeException {

    private String code;

    private String message;

    public TomWebException() {
        super();
    }

    public TomWebException(String message) {
        super("TomWebException-" + message);
    }

    public TomWebException(String code, String message) {
        super(code + "-" + message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

