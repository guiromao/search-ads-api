package co.gromao.searchads.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.dropwizard.jackson.Jackson.newObjectMapper;

class AdTest {

    private static final ObjectMapper MAPPER = newObjectMapper();
    private static final Ad AD = new Ad("1", "Sofa Ad", "Sofa appealing description",
            150.50, Category.FURNITURE);

    @Test
    void testDeserialize() throws Exception {
        Ad test = MAPPER.readValue(getClass().getResource("/ad-sofa.json"), Ad.class);

        Assertions.assertEquals(AD, test);
    }

    @Test
    void testSerialize() throws Exception {
        final String expectedJson = MAPPER.writeValueAsString(AD);

        Assertions.assertEquals(
                expectedJson,
                MAPPER.writeValueAsString(MAPPER.readValue(getClass().getResource("/ad-sofa.json"), Ad.class))
        );
    }

}
