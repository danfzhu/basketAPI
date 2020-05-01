package com.blackbean.api.entity;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "sku_rules", schema = "public", catalog = "testing")
public class SkuRulesEntity {
    private int id;
    private int antecedentSkuId;
    private int consequentSkuId;
    private BigInteger confidence;
    private BigInteger lift;
    private BigInteger absSupport;
    private BigInteger relativeSupport;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "antecedent_sku_id")
    public int getAntecedentSkuId() {
        return antecedentSkuId;
    }

    public void setAntecedentSkuId(int antecedentSkuId) {
        this.antecedentSkuId = antecedentSkuId;
    }

    @Basic
    @Column(name = "consequent_sku_id")
    public int getConsequentSkuId() {
        return consequentSkuId;
    }

    public void setConsequentSkuId(int consequentSkuId) {
        this.consequentSkuId = consequentSkuId;
    }

    @Basic
    @Column(name = "confidence")
    public BigInteger getConfidence() {
        return confidence;
    }

    public void setConfidence(BigInteger confidence) {
        this.confidence = confidence;
    }

    @Basic
    @Column(name = "lift")
    public BigInteger getLift() {
        return lift;
    }

    public void setLift(BigInteger lift) {
        this.lift = lift;
    }

    @Basic
    @Column(name = "abs_support")
    public BigInteger getAbsSupport() {
        return absSupport;
    }

    public void setAbsSupport(BigInteger absSupport) {
        this.absSupport = absSupport;
    }

    @Basic
    @Column(name = "relative_support")
    public BigInteger getRelativeSupport() {
        return relativeSupport;
    }

    public void setRelativeSupport(BigInteger relativeSupport) {
        this.relativeSupport = relativeSupport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkuRulesEntity that = (SkuRulesEntity) o;
        return id == that.id &&
                antecedentSkuId == that.antecedentSkuId &&
                consequentSkuId == that.consequentSkuId &&
                Objects.equals(confidence, that.confidence) &&
                Objects.equals(lift, that.lift) &&
                Objects.equals(absSupport, that.absSupport) &&
                Objects.equals(relativeSupport, that.relativeSupport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, antecedentSkuId, consequentSkuId, confidence, lift, absSupport, relativeSupport);
    }
}
