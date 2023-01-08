package co.gromao.searchads.core;

import java.util.Objects;
import java.util.UUID;

public class Ad {

    private String id;
    private String title;
    private String description;
    private Double price;
    private Category category;

    public Ad() {
        // default constructor
    }

    public Ad(String id, String title, String description, Double price, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public static Ad create(String title, String description, Double price, Category category) {
        return new Ad(
                UUID.randomUUID().toString(),
                title, description,
                price, category
        );
    }

    @Override
    public String toString() {
        return "Ad{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category=" + category +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ad ad = (Ad) o;
        return Objects.equals(id, ad.id) && Objects.equals(title, ad.title) && Objects.equals(description, ad.description) &&
                Objects.equals(price, ad.price) && category == ad.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, price, category);
    }

}
