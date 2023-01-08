package co.gromao.searchads.db;

import co.gromao.searchads.core.Ad;

import java.util.List;

public interface AdsDao {

    List<Ad> findAds(String query, Double priceFrom, Double priceTo);

    void save(List<Ad> ads);

}
