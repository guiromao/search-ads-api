package co.gromao.searchads.service;

import co.gromao.searchads.core.Ad;
import co.gromao.searchads.db.AdsDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class SearchService {

    private final AdsDao adsDao;

    @Inject
    public SearchService(AdsDao adsDao) {
        this.adsDao = adsDao;
    }

    public List<Ad> getAds(String text, Double priceFrom, Double priceTo) {
        return adsDao.findAds(text, priceFrom, priceTo);
    }

}
