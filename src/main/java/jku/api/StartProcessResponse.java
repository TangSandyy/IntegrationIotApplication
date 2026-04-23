package jku.api;

public record StartProcessResponse(
        long processInstanceKey,
        String processDefinitionId,
        String message
) {
}