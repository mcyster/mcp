package com.cyster.flux.chat;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class RestExceptionDocumentationConfig {

    @Bean
    public OperationCustomizer restExceptionCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            for (Class<?> ex : handlerMethod.getMethod().getExceptionTypes()) {
                if (RestException.class.isAssignableFrom(ex)) {
                    ApiResponse apiResponse = new ApiResponse()
                            .description(ex.getSimpleName())
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
}
