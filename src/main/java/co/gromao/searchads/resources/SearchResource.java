package co.gromao.searchads.resources;

import co.gromao.searchads.exception.EmptyQueryException;
import co.gromao.searchads.exception.InvalidPriceException;
import co.gromao.searchads.exception.InvalidPriceException.InvalidPriceType;
import co.gromao.searchads.service.SearchService;
import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Singleton
@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource {

    private final SearchService searchService;

    @Inject
    public SearchResource(SearchService searchService) {
        this.searchService = searchService;
    }

    @GET
    @Timed
    public Response searchResults(@QueryParam(value = "query") String query,
                                  @QueryParam(value = "priceFromEuro") Double priceFromEuro,
                                  @QueryParam(value = "priceToEuro") Double priceToEuro) {
        validateParams(query, priceFromEuro, priceToEuro);

        return Response.ok(searchService.getAds(query, priceFromEuro, priceToEuro)).build();
    }

    private void validateParams(String text, Double priceFrom, Double priceTo) {
        if (StringUtils.isEmpty(text)) {
            throw new EmptyQueryException();
        }

        if ((Objects.nonNull(priceFrom) && priceFrom < 0) || Objects.nonNull(priceTo) && priceTo < 0) {
            throw new InvalidPriceException(InvalidPriceType.NO_NEGATIVE);
        }

        if (Objects.nonNull(priceFrom) && Objects.nonNull(priceTo) && priceFrom > priceTo) {
            throw new InvalidPriceException(InvalidPriceType.SWITCHED_PRICES);
        }
    }

}
