package co.gromao.searchads;

import co.gromao.searchads.core.Ad;
import co.gromao.searchads.core.Category;

import java.util.Arrays;
import java.util.List;

public class DataUtils {

    private DataUtils() {
        // no instantiation
    }

    public static List<Ad> ads() {
        return Arrays.asList(
                Ad.create("Thing 1", "Description 1", 130.0, Category.ART),
                Ad.create("Thing 2", "Description 2", 200.0, Category.FURNITURE),
                Ad.create("Thing 3", "Description 3", 30.0, Category.SPORTS),
                Ad.create("Thing 4", "Description 4", 7.0, Category.FOOD),
                Ad.create("Thing 5", "Description 5", 20000.0, Category.TECHNOLOGY),
                Ad.create("Something else", "Description 6", 1.0, Category.FURNITURE)
        );
    }

}
