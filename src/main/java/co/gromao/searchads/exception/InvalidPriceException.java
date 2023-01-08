package co.gromao.searchads.exception;

import co.gromao.searchads.api.ErrorResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class InvalidPriceException extends WebApplicationException {

    public InvalidPriceException(InvalidPriceType type) {
        super(Response.status(Status.BAD_REQUEST)
                .entity(ErrorResponse.of(type.getErrorText(), Status.BAD_REQUEST))
                .build());
    }

    public enum InvalidPriceType {
        NO_NEGATIVE("Price values cannot be negative"),
        SWITCHED_PRICES("'priceToEuro' must be equal or higher than 'priceFromEuro'");

        private final String errorText;

        InvalidPriceType(String text) {
            this.errorText = text;
        }

        public String getErrorText() {
            return errorText;
        }
    }

}
