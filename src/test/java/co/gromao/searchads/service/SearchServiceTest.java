package co.gromao.searchads.service;

import co.gromao.searchads.DataUtils;
import co.gromao.searchads.core.Ad;
import co.gromao.searchads.db.AdsDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchServiceTest {

    AdsDao adsDao;

    SearchService service;

    @BeforeEach
    void setup() {
        adsDao = mock(AdsDao.class);

        service = new SearchService(adsDao);
    }

    @Test
    void testGetAds() {
        when(adsDao.findByParams(anyString(), anyDouble(), anyDouble())).thenReturn(DataUtils.ads());

        List<Ad> test = service.getAds("any-text", 0.00, 5000.00);

        Assertions.assertEquals(DataUtils.ads().size(), test.size());

        for (int i = 0; i < test.size() - 1; i++) {
            Double price1 = test.get(i).getPrice();
            Double price2 = test.get(i + 1).getPrice();

            Assertions.assertTrue(price1 <= price2);
        }
    }

}
