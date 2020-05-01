package com.blackbean.api.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sku_group_map", schema = "public", catalog = "testing")
public class SkuGroupMapEntity {
    private int id;
    private int skuId;
    private Integer skuGroupId;

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
    @Column(name = "sku_group_id")
    public Integer getSkuGroupId() {
        return skuGroupId;
    }

    public void setSkuGroupId(Integer skuGroupId) {
        this.skuGroupId = skuGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkuGroupMapEntity that = (SkuGroupMapEntity) o;
        return id == that.id &&
                skuId == that.skuId &&
                Objects.equals(skuGroupId, that.skuGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, skuId, skuGroupId);
    }
}
