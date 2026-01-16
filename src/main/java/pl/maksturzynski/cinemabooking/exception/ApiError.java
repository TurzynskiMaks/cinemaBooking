package pl.maksturzynski.cinemabooking.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
public class ApiError {

    private Instant timestamp;
    private int status;
    private String message;
    private String path;

    private Map<String, String> details;

    public ApiError(){}

    public ApiError(Instant timestamp, int status, String message, String path, Map<String, String> details) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
        this.details = details;
    }

}
