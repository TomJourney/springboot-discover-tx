package com.tom.springboot.tx.note.infrastructure.common.exception;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName TomWebModel.java
 * @Description TODO
 * @createTime 2025年03月06日 21:21:00
 */
public class TomWebModel {

    private String code;

    private String message;

    public TomWebModel() {
        // do nothing
    }

    public TomWebModel(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public TomWebModel(TomWebException tomWebException) {
        this.code = tomWebException.getCode();
        this.message = tomWebException.getMessage();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
