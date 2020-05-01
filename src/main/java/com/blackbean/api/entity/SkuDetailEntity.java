package com.blackbean.api.entity;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "sku_detail", schema = "public", catalog = "testing")
public class SkuDetailEntity {
    private int id;
    private int skuId;
    private String name;
    private BigInteger cost;
    private BigInteger price;
    private BigInteger margin;
    private BigInteger stock;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "sku_id")
    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "cost")
    public BigInteger getCost() {
        return cost;
    }

    public void setCost(BigInteger cost) {
        this.cost = cost;
    }

    @Basic
    @Column(name = "price")
    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    @Basic
    @Column(name = "margin")
    public BigInteger getMargin() {
        return margin;
    }

    public void setMargin(BigInteger margin) {
        this.margin = margin;
    }

    @Basic
    @Column(name = "stock")
    public BigInteger getStock() {
        return stock;
    }

    public void setStock(BigInteger stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkuDetailEntity that = (SkuDetailEntity) o;
        return id == that.id &&
                skuId == that.skuId &&
                Objects.equals(name, that.name) &&
                Objects.equals(cost, that.cost) &&
                Objects.equals(price, that.price) &&
                Objects.equals(margin, that.margin) &&
                Objects.equals(stock, that.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, skuId, name, cost, price, margin, stock);
    }
}
