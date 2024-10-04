package io.server.accountserviceserver.service;

import io.server.accountserviceserver.exception.ErrorResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ErrorServiceImplTest {

    private ErrorServiceImpl errorService;

    @BeforeEach
    void setUp() {
        errorService = new ErrorServiceImpl();
    }

    @Test
    void makeResponse_withException_shouldReturnErrorResponse() {
        // Arrange
        Exception exception = mock(Exception.class);
        String exceptionMessage = "Test exception message";
        when(exception.getMessage()).thenReturn(exceptionMessage);

        // Act
        ErrorResponseBody response = errorService.makeResponse(exception);

        // Assert
        assertEquals(exceptionMessage, response.getTitle(), "Response title should be the exception message");
        assertEquals(1, response.getReasons().size(), "There should be one reason");
        assertEquals(List.of(exceptionMessage), response.getReasons().get("errors"), "Reason should contain the exception message");
    }

    @Test
    void makeResponse_withNullExceptionMessage_shouldHandleNullMessage() {
        // Arrange
        Exception exception = mock(Exception.class);
        when(exception.getMessage()).thenReturn(null);

        // Act
        ErrorResponseBody response = errorService.makeResponse(exception);

        // Assert
        assertEquals("Unknown error", response.getTitle(), "Response title should be 'Unknown error' when exception message is null");
        assertEquals(1, response.getReasons().size(), "There should be one reason");
        assertEquals(List.of("Unknown error"), response.getReasons().get("errors"), "Reason should contain 'Unknown error'");
    }

    @Test
    void makeResponse_withBindingResult_shouldFilterNullDefaultMessages() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("objectName", "field1", "Field 1 error message");
        FieldError fieldError2 = new FieldError("objectName", "field2", null); // This error has a null default message
        FieldError fieldError3 = new FieldError("objectName", "field1", "Another Field 1 error message");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2, fieldError3));

        // Act
        ErrorResponseBody response = errorService.makeResponse(bindingResult);

        // Assert
        Map<String, List<String>> expectedReasons = Map.of(
                "field1", List.of("Field 1 error message", "Another Field 1 error message")
        );

        assertEquals("Validation error", response.getTitle());
        assertNotNull(response.getReasons(), "Reasons should not be null");
        assertEquals(expectedReasons, response.getReasons());
    }

    @Test
    void makeResponse_withExistingFieldInReasons() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("objectName", "field1", "Field 1 error message");
        FieldError fieldError2 = new FieldError("objectName", "field1", "Another Field 1 error message");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        // Act
        ErrorResponseBody response = errorService.makeResponse(bindingResult);

        // Assert
        Map<String, List<String>> expectedReasons = new HashMap<>();
        expectedReasons.put("field1", new ArrayList<>(List.of("Field 1 error message", "Another Field 1 error message")));

        assertEquals("Validation error", response.getTitle());
        assertNotNull(response.getReasons(), "Reasons should not be null");
        assertEquals(expectedReasons, response.getReasons());
    }

    @Test
    void makeResponse_withNewFieldInReasons() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("objectName", "field1", "Field 1 error message");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1));

        // Act
        ErrorResponseBody response = errorService.makeResponse(bindingResult);

        // Assert
        Map<String, List<String>> expectedReasons = new HashMap<>();
        expectedReasons.put("field1", new ArrayList<>(List.of("Field 1 error message")));

        assertEquals("Validation error", response.getTitle());
        assertNotNull(response.getReasons(), "Reasons should not be null");
        assertEquals(expectedReasons, response.getReasons());
    }
}