package co.gromao.searchads.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.dropwizard.jackson.Jackson.newObjectMapper;

class ErrorResponseTest {

    private static final ObjectMapper MAPPER = newObjectMapper();
    private static final ErrorResponse ERROR_RESPONSE = new ErrorResponse(
            "BAD_REQUEST", 400, "Cannot search for an empty string"
    );

    @Test
    void testDeserialize() throws Exception {
        ErrorResponse test = MAPPER.readValue(getClass().getResource("/error-response.json"), ErrorResponse.class);

        Assertions.assertEquals(ERROR_RESPONSE, test);
    }

    @Test
    void testSerialize() throws Exception {
        final String expectedJson = MAPPER.writeValueAsString(ERROR_RESPONSE);

        Assertions.assertEquals(
                expectedJson,
                MAPPER.writeValueAsString(MAPPER.readValue(getClass().getResource("/error-response.json"), ErrorResponse.class))
        );
    }

}
