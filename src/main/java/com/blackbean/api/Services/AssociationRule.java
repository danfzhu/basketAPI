package com.blackbean.api.Services;

import java.util.Set;


public class AssociationRule {
    private static final long serialVersionUID = 1L;

    long id;
    long abs_support;
    double relative_support;
    double confidence;
    double lift;

    Set<Long> antecedent;

    Set<Long> consequent;

    public AssociationRule(long abs_support, double relative_support, double confidence, double lift, Set<Long> antecedent, Set<Long> consequent) {
        this.abs_support = abs_support;
        this.relative_support = relative_support;
        this.confidence = confidence;
        this.lift = lift;
        this.antecedent = antecedent;
        this.consequent = consequent;
    }

    public double getConfidence() {
        return confidence;
    }

    public double getLift() {
        return lift;
    }

    public double getRelative_support() {
        return relative_support;
    }

    public Set<Long> getAntecedent() {
        return antecedent;
    }

    public Set<Long> getConsequent() {
        return consequent;
    }

    public long getAbs_support() {
        return abs_support;
    }

    public AssociationRule(){

    }
}
