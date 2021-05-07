package tn.isetsf.bpointage.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;


public class GlobalException {
    private final String message;
    private final HttpStatus httpStatus;
    private final Date date;

    public GlobalException(String message, HttpStatus httpStatus, Date date) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Date getDate() {
        return date;
    }
}
