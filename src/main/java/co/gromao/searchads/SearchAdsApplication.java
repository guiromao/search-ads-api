package co.gromao.searchads;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.gromao.searchads.core.Ad;
import co.gromao.searchads.core.Category;
import co.gromao.searchads.db.AdsDao;
import co.gromao.searchads.db.AdsDaoImpl;
import co.gromao.searchads.resources.SearchResource;
import co.gromao.searchads.service.SearchService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import co.gromao.searchads.health.AppHealthCheck;
import org.apache.http.HttpHost;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;

import java.util.Arrays;
import java.util.List;

public class SearchAdsApplication extends Application<SearchAdsConfiguration> {

    public static void main(final String[] args) throws Exception {
        new SearchAdsApplication().run(args);
    }

    @Override
    public String getName() {
        return "Search Ads API";
    }

    @Override
    public void initialize(final Bootstrap<SearchAdsConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final SearchAdsConfiguration configuration,
                    final Environment environment) {
        environment.healthChecks().register("Search Ads HealthCheck", new AppHealthCheck());

        RestClient restClient = RestClient.builder(
                        new HttpHost("localhost", 9200, "http"))
                .setNodeSelector(NodeSelector.ANY)
                .build();
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        ElasticsearchClient client = new ElasticsearchClient(transport);

        final AdsDao adsDao = new AdsDaoImpl(client);
        final SearchService searchService = new SearchService(adsDao);
        final SearchResource searchResource = new SearchResource(searchService);

        environment.jersey().register(client);
        environment.jersey().register(adsDao);
        environment.jersey().register(searchService);
        environment.jersey().register(searchResource);

        // Save some Ads on the database
        adsDao.save(ads());
    }

    private List<Ad> ads() {
        return Arrays.asList(
                new Ad("1", "Sofa", "Beautiful sofa", 40.0, Category.FURNITURE),
                new Ad("2", "Lamp", "A lamp that matches your sofa", 10.70, Category.FURNITURE),
                new Ad("3", "Football", "World Cup Football", 100.0, Category.SPORTS),
                new Ad("4", "Laptop", "Useful for your everyday work. Also good if you're sitting in your sofa", 200.0, Category.TECHNOLOGY),
                new Ad("5", "Burger", "Freshly made from the order moment", 9.75, Category.FOOD),
                new Ad("6", "Painting", "By a cousin of Van Gogh", 3000.99, Category.ART)
        );
    }

}
