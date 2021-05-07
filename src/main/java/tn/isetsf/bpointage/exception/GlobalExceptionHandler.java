package tn.isetsf.bpointage.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value={ResponseStatusException.class})
    public ResponseEntity<Object> handleApiRequestException(ResponseStatusException e)
    {
        GlobalException globalException = new GlobalException(e.getReason(),
                e.getStatus(),
                new Date()
        );
        return new ResponseEntity<>(globalException, e.getStatus());
    }
}
