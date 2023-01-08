package co.gromao.searchads.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.Response.Status;
import java.util.Objects;

public class ErrorResponse {

    private final String title;
    private final int code;
    private final String message;

    public ErrorResponse(@JsonProperty("title") String title,
                         @JsonProperty("code") int code,
                         @JsonProperty("message") String message) {
        this.title = title;
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of(String message, Status status) {
        return new ErrorResponse(status.name(), status.getStatusCode(), message);
    }

    public String getTitle() {
        return title;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorResponse that = (ErrorResponse) o;
        return code == that.code && Objects.equals(title, that.title) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, code, message);
    }

}
