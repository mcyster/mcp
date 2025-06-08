package com.cyster.flux.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

/** Interface describing an error returned to the client. */
@Schema(name = "ErrorResponse", description = "Details about an error response")
public interface ErrorResponse<CODE extends Enum<?>> {

  @Schema(description = "HTTP status code of the error", example = "400")
  int httpStatusCode();

  @Schema(
      description = "Unique identifier for tracking the error",
      example = "fa1ef2c2-cb51-4b62-8887-2fa8328bc8f2")
  String uniqueId();

  @Schema(description = "Application specific error code", example = "PROMPT_MISSING")
  CODE errorCode();

  @Schema(description = "Human readable error message", example = "No prompt was specified")
  String message();

  @Schema(description = "Additional information about the error")
  Map<String, Object> parameters();
}
