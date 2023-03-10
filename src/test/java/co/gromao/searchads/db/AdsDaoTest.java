package co.gromao.searchads.db;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ShardStatistics;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse.Builder;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.gromao.searchads.DataUtils;
import co.gromao.searchads.core.Ad;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

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
        final SearchResponse<Ad> expectedResponse = searchResponse();

        Mockito.when(elasticsearchClient.search(any(SearchRequest.class), any(Class.class))).thenReturn(expectedResponse);

        List<Ad> test = dao.findByParams("Thing", 0.00, 1000.00);

        Assertions.assertEquals(expectedResponse.hits().hits().size(), test.size());
    }

    private SearchResponse<Ad> searchResponse() {
        HitsMetadata.Builder<Ad> hitsMetaDataBuilder = new HitsMetadata.Builder<>();
        hitsMetaDataBuilder.hits(
                DataUtils.ads().stream()
                        .map(ad -> {
                            Hit.Builder<Ad> hitBuilder = new Hit.Builder<>();
                            hitBuilder.index("ads").id(ad.getId()).source(ad);

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

}
