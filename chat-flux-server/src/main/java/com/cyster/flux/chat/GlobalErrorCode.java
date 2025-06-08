package com.cyster.flux.chat;

import io.swagger.v3.oas.annotations.media.Schema;

/** Error codes that apply globally across the application. */
public enum GlobalErrorCode {
  @Schema(description = "Endpoint not defined")
  NOT_FOUND;
}
