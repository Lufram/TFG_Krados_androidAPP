package com.edix.krados.entity;

import java.util.List;

public class Cart {
    private Long id;
    private List<Product> prodList;

    public Cart() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Product> getProdList() {
        return prodList;
    }

    public void setProdList(List<Product> prodList) {
        this.prodList = prodList;
    }
}
