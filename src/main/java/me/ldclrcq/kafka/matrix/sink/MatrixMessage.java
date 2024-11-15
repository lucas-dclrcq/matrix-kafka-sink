package me.ldclrcq.kafka.matrix.sink;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record MatrixMessage(
        @JsonProperty("msgtype") String msgType,
        String body,
        String format,
        @JsonProperty("formatted_body") String formattedBody
) {
    public static MatrixMessage fromIncoming(IncomingMatrixMessage value) {
        return new MatrixMessage(value.getMsgType(), value.getBody(), value.getFormat(), value.getFormattedBody());
    }
}