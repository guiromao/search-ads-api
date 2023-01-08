package co.gromao.searchads.db;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ShardStatistics;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse.Builder;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.gromao.searchads.DataUtils;
import co.gromao.searchads.core.Ad;
import org.apache.http.HttpHost;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

class AdsDaoTest {

    ElasticsearchClient elasticsearchClient;

    AdsDao dao;

    @BeforeEach
    void setup() {
        elasticsearchClient = mock(ElasticsearchClient.class);

        dao = new AdsDaoImpl(elasticsearchClient);

        dao.saveAll(DataUtils.ads());
    }

    @Test
    void testFindAds() throws Exception {
        Mockito.when(elasticsearchClient.search(any(Function.class), any(Class.class))).thenReturn(searchResponse());

        List<Ad> test = dao.findByParams("any-text", 0.00, 1000.00);

        Assertions.assertEquals(DataUtils.ads().size(), test.size());
    }

    private SearchResponse<Ad> searchResponse() throws Exception {
        HitsMetadata.Builder<Ad> hitsMetaDataBuilder = new HitsMetadata.Builder<>();
        hitsMetaDataBuilder.hits(
                DataUtils.ads().stream()
                        .map(ad -> {
                            Hit.Builder<Ad> hitBuilder = new Hit.Builder<>();
                            hitBuilder.index("ads").id(ad.getId()).fields(adFieldMapOf(ad));

                            return hitBuilder.build();
                        })
                        .toList()
        );

        SearchResponse.Builder<Ad> responseBuilder = new Builder<>();
        responseBuilder.hits(hitsMetaDataBuilder.build());

        return responseBuilder
                .took(0)
                .timedOut(false)
                .shards(new ShardStatistics.Builder()
                        .failed(0)
                        .successful(DataUtils.ads().size())
                        .total(DataUtils.ads().size())
                        .build())
                .build();
    }

    private Map<String, JsonData> adFieldMapOf(Ad ad) {
        return Map.of(
                "id", JsonData.of(ad.getId()),
                "title", JsonData.of(ad.getTitle()),
                "description", JsonData.of(ad.getDescription()),
                "price", JsonData.of(ad.getPrice()),
                "category", JsonData.of(ad.getCategory())
        );
    }

}
