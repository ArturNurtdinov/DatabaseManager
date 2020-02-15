package db;

import java.util.Objects;
import java.util.UUID;

public class Product {
    int id;
    String prodid;
    String title;
    int cost;

    public Product() {
    }

    public Product(int id, String prodid, String title, int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost can't be negative.");
        }
        this.id = id;
        this.prodid = prodid;
        this.title = title;
        this.cost = cost;
    }

    public Product(int id, String title, int cost) {
        this(id, UUID.randomUUID().toString(), title, cost);
    }


    public int getId() {
        return id;
    }

    public String getProdid() {
        return prodid;
    }

    public String getTitle() {
        return title;
    }

    public int getCost() {
        return cost;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCost(int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost can't be negative.");
        }
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "ID: " + id + " " +
                "prodid: " + prodid + " " +
                "title: " + title + " " +
                "cost: " + cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                cost == product.cost &&
                prodid.equals(product.prodid) &&
                title.equals(product.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prodid, title, cost);
    }
}
