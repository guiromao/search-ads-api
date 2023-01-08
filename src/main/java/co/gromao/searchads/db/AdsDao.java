package co.gromao.searchads.db;

import co.gromao.searchads.core.Ad;

import java.util.List;

public interface AdsDao {

    List<Ad> findByParams(String query, Double priceFrom, Double priceTo);

    void saveAll(List<Ad> ads);

}
