package co.gromao.searchads.exception;

import co.gromao.searchads.api.ErrorResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class EmptyQueryException extends WebApplicationException {

    public EmptyQueryException() {
        super(Response.status(Status.BAD_REQUEST)
                .entity(ErrorResponse.of("Cannot search for an empty string", Status.BAD_REQUEST))
                .build());
    }

}
