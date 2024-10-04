package io.server.accountserviceserver.service;


import io.server.accountserviceserver.exception.ErrorResponseBody;
import io.server.accountserviceserver.service.ErrorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErrorServiceImpl implements ErrorService {

    @Override
    public ErrorResponseBody makeResponse(Exception exception) {
        String message = exception.getMessage() != null ? exception.getMessage() : "Unknown error";  // Handle null message
        log.debug(Arrays.toString(exception.getStackTrace()));
        return ErrorResponseBody.builder()
                .title(message)
                .reasons(Map.of("errors", List.of(message)))
                .build();
    }



    @Override
    public ErrorResponseBody makeResponse(BindingResult exception) {
        Map<String, List<String>> reasons = new HashMap<>();
        exception.getFieldErrors().stream()
                .filter(e -> e.getDefaultMessage() != null)
                .forEach(e -> {
                    List<String> errors = new ArrayList<>();
                    errors.add(e.getDefaultMessage());
                    if (!reasons.containsKey(e.getField())) {
                        reasons.put(e.getField(), errors);
                    } else {
                        reasons.get(e.getField()).addAll(errors);
                    }
                });
        return ErrorResponseBody.builder()
                .title("Validation error")
                .reasons(reasons)
                .build();
    }

}


