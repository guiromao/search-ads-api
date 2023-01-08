package co.gromao.searchads.core;

public enum Category {
    FURNITURE("Furniture"),
    SPORTS("Sports"),
    TECHNOLOGY("Technology"),
    FOOD("Food"),
    ART("Art");

    private final String title;

    Category(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
