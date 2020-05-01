package com.blackbean.api.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "file_sku_rules_map", schema = "public", catalog = "testing")
public class FileSkuRulesMapEntity {
    private int id;
    private int fileId;
    private int skuRulesId;
    private boolean isActive;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "file_id")
    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    @Basic
    @Column(name = "sku_rules_id")
    public int getSkuRulesId() {
        return skuRulesId;
    }

    public void setSkuRulesId(int skuRulesId) {
        this.skuRulesId = skuRulesId;
    }

    @Basic
    @Column(name = "is_active")
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileSkuRulesMapEntity that = (FileSkuRulesMapEntity) o;
        return id == that.id &&
                fileId == that.fileId &&
                skuRulesId == that.skuRulesId &&
                isActive == that.isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileId, skuRulesId, isActive);
    }
}
