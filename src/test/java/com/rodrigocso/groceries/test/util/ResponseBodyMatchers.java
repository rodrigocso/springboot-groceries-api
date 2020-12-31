package com.rodrigocso.groceries.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rodrigocso.groceries.exception.ErrorResponse;
import com.rodrigocso.groceries.exception.FieldValidationError;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ResponseBodyMatchers {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static ResponseBodyMatchers responseBody(){
        return new ResponseBodyMatchers();
    }

    public <T> ResultMatcher containsObjectAsJson(Object expectedObject, Class<T> targetClass) {
        objectMapper.registerModule(new JavaTimeModule());
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            T actualObject = objectMapper.readValue(json, targetClass);
            assertThat(actualObject).usingRecursiveComparison().isEqualTo(expectedObject);
        };
    }

    public <T> ResultMatcher isEqualTo(String expectedJsonBody) {
        return mvcResult -> assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(expectedJsonBody);
    }

    public ResultMatcher containsError(String expectedFieldName, String expectedMessage) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            ErrorResponse errorResponse = objectMapper.readValue(json, ErrorResponse.class);

            List<FieldValidationError> fieldErrors = errorResponse.getFieldErrors().stream()
                    .filter(fieldError -> fieldError.getField().equals(expectedFieldName))
                    .filter(fieldError -> fieldError.getMessage().equals(expectedMessage))
                    .collect(Collectors.toList());

            assertThat(fieldErrors).asList().hasSize(1);
        };
    }
}
