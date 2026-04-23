package jku.api;

import jakarta.validation.constraints.NotBlank;

public record ScanRequest(
        @NotBlank String rfidId,
        @NotBlank String location,
        String condition,
        String timestamp
) {
}