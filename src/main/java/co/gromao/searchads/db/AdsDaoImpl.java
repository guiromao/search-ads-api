package co.gromao.searchads.db;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.gromao.searchads.core.Ad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static co.gromao.searchads.SearchAdsConfiguration.ADS_INDEX;

@Singleton
public class AdsDaoImpl implements AdsDao {

    private static final Logger LOG = LoggerFactory.getLogger(AdsDaoImpl.class);
    private static final String FIELD_PRICE = "price";

    private final ElasticsearchClient client;

    @Inject
    public AdsDaoImpl(ElasticsearchClient client) {
        this.client = client;
    }

    @Override
    public List<Ad> findAds(String text, Double priceFrom, Double priceTo) {
        BoolQuery.Builder boolQueryBuilder = new Builder();

        // Add query for text
        boolQueryBuilder.must(MatchQuery.of(match -> match
                .field("name")
                .field("description")
                .query(text)
        )._toQuery());

        RangeQuery.Builder rangeBuilder = new RangeQuery.Builder();

        if (Objects.nonNull(priceFrom)) {
            rangeBuilder.field(FIELD_PRICE).gte(JsonData.of(priceFrom));
        }
        if (Objects.nonNull(priceTo)) {
            rangeBuilder.field(FIELD_PRICE).lte(JsonData.of(priceTo));
        }
        if (Objects.nonNull(priceFrom) || Objects.nonNull(priceTo)) {
            boolQueryBuilder.must(rangeBuilder.build()._toQuery());
        }

        try {
            SearchResponse<Ad> search = client.search(s -> s
                            .index(ADS_INDEX)
                            .query(q -> q
                                    .bool(b -> boolQueryBuilder)
                            ),
                    Ad.class);

            return search.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            LOG.error("Could not read database results", e);
        }

        return Collections.emptyList();
    }

    @Override
    public void save(List<Ad> ads) {
        final BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();

        for (Ad ad: ads) {
            bulkBuilder.operations(op ->
                    op.index(i -> i
                            .index(ADS_INDEX)
                            .id(ad.getId())
                            .document(ad))
            );
        }

        try {
            client.bulk(bulkBuilder.build());
        } catch (IOException e) {
            LOG.error("Unable to bulk save the Ads", e);
        }
    }

}
