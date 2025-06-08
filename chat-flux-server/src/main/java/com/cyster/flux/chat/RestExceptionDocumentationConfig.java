package com.cyster.flux.chat;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class RestExceptionDocumentationConfig {

  @Bean
  public OperationCustomizer restExceptionCustomizer() {
    return (Operation operation, HandlerMethod handlerMethod) -> {
      for (Class<?> exceptionClass : handlerMethod.getMethod().getExceptionTypes()) {
        if (RestException.class.isAssignableFrom(exceptionClass)) {
          ApiResponse apiResponse =
              new ApiResponse()
                  .description(buildDescription(exceptionClass))
                  .content(
                      new Content()
                          .addMediaType(
                              org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                              new MediaType()
                                  .schema(
                                      new Schema<>()
                                          .$ref("#/components/schemas/RestErrorResponse"))));
          operation.getResponses().addApiResponse("400", apiResponse);
        }
      }
      return operation;
    };
  }

  private String buildDescription(Class<?> exceptionClass) {
    String description = exceptionClass.getSimpleName();
    Class<?> enclosingClass = exceptionClass.getEnclosingClass();
    if (enclosingClass != null) {
      for (Class<?> nestedClass : enclosingClass.getDeclaredClasses()) {
        if (nestedClass.isEnum() && nestedClass.getSimpleName().endsWith("ErrorCode")) {
          String codes =
              Arrays.stream(nestedClass.getEnumConstants())
                  .map(
                      constant -> {
                        try {
                          Field field = nestedClass.getField(((Enum<?>) constant).name());
                          io.swagger.v3.oas.annotations.media.Schema schema =
                              field.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
                          String descriptionText = schema != null ? schema.description() : "";
                          return ((Enum<?>) constant).name()
                              + (descriptionText.isBlank() ? "" : " - " + descriptionText);
                        } catch (NoSuchFieldException exception) {
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
