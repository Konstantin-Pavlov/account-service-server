package io.server.accountserviceserver.exception.handler;

import io.server.accountserviceserver.exception.AccountNotFoundException;
import io.server.accountserviceserver.exception.ErrorResponseBody;
import io.server.accountserviceserver.service.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorService errorService;

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseBody> noSuchElement(AccountNotFoundException exception) {
        return new ResponseEntity<>(errorService.makeResponse(exception), HttpStatus.NOT_FOUND);
    }

}
