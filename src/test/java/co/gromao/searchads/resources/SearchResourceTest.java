package co.gromao.searchads.resources;

import co.gromao.searchads.DataUtils;
import co.gromao.searchads.core.Ad;
import co.gromao.searchads.service.SearchService;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@ExtendWith(DropwizardExtensionsSupport.class)
class SearchResourceTest {

    private static final String SEARCH_PATH = "/search";
    private static final String KEYWORD = "Thing";
    private static final List<Ad> ADS = DataUtils.ads();

    SearchService searchService = Mockito.mock(SearchService.class);

    ResourceExtension extension = ResourceExtension.builder()
            .addResource(new SearchResource(searchService))
            .build();

    @BeforeEach
    void setup() {
        when(searchService.getAds(eq(KEYWORD), nullable(Double.class), nullable(Double.class)))
                .thenReturn(adsWith(KEYWORD));
    }

    @ParameterizedTest
    @MethodSource("badRequestParams")
    void testBadRequests(String query, Double priceFrom, Double priceTo) {
        Response resp = extension.target(SEARCH_PATH)
                .queryParam("query", query)
                .queryParam("priceFromEuro", priceFrom)
                .queryParam("priceToEuro", priceTo)
                .request().get();

        Assertions.assertEquals(Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
    }

    @Test
    void testSearch() {
        final int expectedSize = adsWith(KEYWORD).size();

        Response resp = extension.target(SEARCH_PATH)
                .queryParam("query", KEYWORD)
                .queryParam("priceFromEuro", 0.0)
                .queryParam("priceToEuro", 5000.0)
                .request().get();

        Assertions.assertEquals(Status.OK.getStatusCode(), resp.getStatus());
        Assertions.assertEquals(expectedSize, resp.readEntity(List.class).size());
    }

    static Stream<Arguments> badRequestParams() {
        return Stream.of(
                // Empty text
                Arguments.of("", null, null),
                // Negative prices
                Arguments.of("sofa", -8.0, -2.0),
                // 'from' price higher than 'to' price
                Arguments.of("sofa", 60.0, 40.0)
        );
    }

    private List<Ad> adsWith(String keyword) {
        return ADS.stream()
                .filter(ad -> ad.getTitle().contains(keyword) ||
                        ad.getDescription().contains(keyword))
                .toList();
    }

}
