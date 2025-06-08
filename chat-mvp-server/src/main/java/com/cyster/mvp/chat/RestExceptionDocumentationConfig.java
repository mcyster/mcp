package com.cyster.mvp.chat;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class RestExceptionDocumentationConfig {

    @Bean
    public OperationCustomizer restExceptionCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            for (Class<?> ex : handlerMethod.getMethod().getExceptionTypes()) {
                if (RestException.class.isAssignableFrom(ex)) {
                    ApiResponse apiResponse = new ApiResponse()
                            .description(buildDescription(ex))
                            .content(new Content().addMediaType(
                                    org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                    new MediaType().schema(
                                            new Schema<>().$ref("#/components/schemas/RestErrorResponse"))));
                    operation.getResponses().addApiResponse("400", apiResponse);
                }
            }
            return operation;
        };
    }

    private String buildDescription(Class<?> ex) {
        String description = ex.getSimpleName();
        Class<?> enclosing = ex.getEnclosingClass();
        if (enclosing != null) {
            for (Class<?> nested : enclosing.getDeclaredClasses()) {
                if (nested.isEnum() && nested.getSimpleName().endsWith("ErrorCode")) {
                    String codes = Arrays.stream(nested.getEnumConstants())
                            .map(constant -> {
                                try {
                                    Field f = nested.getField(((Enum<?>) constant).name());
                                    io.swagger.v3.oas.annotations.media.Schema schema = f.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
                                    String d = schema != null ? schema.description() : "";
                                    return ((Enum<?>) constant).name() + (d.isBlank() ? "" : " - " + d);
                                } catch (NoSuchFieldException e) {
                                    return ((Enum<?>) constant).name();
                                }
                            })
                            .collect(Collectors.joining(", "));
                    description += " (codes: " + codes + ")";
                }
            }
        }
        return description;
    }
}
