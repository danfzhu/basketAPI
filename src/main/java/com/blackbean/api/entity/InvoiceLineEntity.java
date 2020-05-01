package com.blackbean.api.entity;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "invoice_line", schema = "public", catalog = "testing")
public class InvoiceLineEntity {
    private int id;
    private int invoiceId;
    private int skuId;
    private BigInteger price;
    private BigInteger qty;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "invoice_id")
    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
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
    @Column(name = "price")
    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    @Basic
    @Column(name = "qty")
    public BigInteger getQty() {
        return qty;
    }

    public void setQty(BigInteger qty) {
        this.qty = qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceLineEntity that = (InvoiceLineEntity) o;
        return id == that.id &&
                invoiceId == that.invoiceId &&
                skuId == that.skuId &&
                Objects.equals(price, that.price) &&
                Objects.equals(qty, that.qty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, invoiceId, skuId, price, qty);
    }
}
