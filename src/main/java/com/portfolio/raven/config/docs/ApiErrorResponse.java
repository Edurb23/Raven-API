package com.portfolio.raven.config.docs;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiErrorResponse", description = "Default error response format")
public class ApiErrorResponse {

    @Schema(example = "2026-02-06T07:39:37.644+00:00")
    public String timestamp;

    @Schema(example = "400")
    public Integer status;

    @Schema(example = "Bad Request")
    public String error;

    @Schema(example = "Validation failed")
    public String message;

    @Schema(example = "/artist")
    public String path;
}
