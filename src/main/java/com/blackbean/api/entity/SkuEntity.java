package com.blackbean.api.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sku", schema = "public", catalog = "testing")
public class SkuEntity {
    private int id;
    private int fileId;
    private String code;

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
    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkuEntity skuEntity = (SkuEntity) o;
        return id == skuEntity.id &&
                fileId == skuEntity.fileId &&
                Objects.equals(code, skuEntity.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileId, code);
    }
}
