package com.edix.krados.entity;


public class Product {

    private Long id;
    private String name;
    private String info;
    private double uPrice;
    private int amount;
    private String url;
    private boolean inOffer;

    private int category;

    public Product(Long id, String name, String info, double uPrice, int amount, String url, boolean inOffer, int category) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.uPrice = uPrice;
        this.amount = amount;
        this.url = url;
        this.inOffer = inOffer;
        this.category = category;
    }

    public Product() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public double getuPrice() {
        return uPrice;
    }

    public void setuPrice(double uPrice) {
        this.uPrice = uPrice;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isInOffer() {
        return inOffer;
    }

    public void setInOffer(boolean inOffer) {
        this.inOffer = inOffer;
    }

    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + name + ", info=" + info + ", uPrice=" + uPrice + ", categoria="+ category +"]";
    }
}
