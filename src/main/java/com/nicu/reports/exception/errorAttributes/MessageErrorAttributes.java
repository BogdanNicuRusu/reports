package com.nicu.reports.exception.errorAttributes;

import static java.util.Collections.emptyList;
import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.BINDING_ERRORS;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;

@Component
public class MessageErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options.including(BINDING_ERRORS));
        List<ObjectError> errors = (List<ObjectError>) errorAttributes.getOrDefault("errors", emptyList());
        String errorMessage = errors.stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining("; "));
        errorAttributes.remove("errors");
        if (!StringUtils.isEmpty(errorMessage)) {
            errorAttributes.put("message", errorMessage);
        }
        return errorAttributes;
    }
}
