package com.blackbean.api.Services;

import com.google.common.collect.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class UpsellBasketRecommendationPreComputationService {



    private final int TOP_SKU_COUNT = Integer.MAX_VALUE;
    private final int K_LIMIT = 2;

    public List<AssociationRule> generateAssociationRules(List<Set<Long>> transactions, Multiset<Long> itemSet, long minAbsSup, double minRelativeSup, double minConfidence, double minLift, SetMultimap<Long, Long> invertedIndex){
        List<ItemSet> frequentItemSets = apriori(transactions, itemSet, minAbsSup, minRelativeSup, invertedIndex);
        List<AssociationRule> associationRules = ruleGeneration(transactions, frequentItemSets, minConfidence, minLift, invertedIndex);
        Collections.sort(associationRules, (AssociationRule o1, AssociationRule o2) -> Double.compare(o2.getConfidence(),o1.getConfidence()));
        return associationRules;
    }

    private List<ItemSet> apriori(List<Set<Long>> transactions,  Multiset<Long> itemSet, long minAbsSup, double minRelativeSup,  SetMultimap<Long, Long> invertedIndex) {
        List<ItemSet> frequentItemSets = Lists.newArrayList();

        // k is the length of item sets, we start with 2 since length 1 set do not need calculation
        int k = 2;
        // lk is the level k frequent set
        List<ItemSet> Lk = Lists.newArrayList();

        // Lk: L1 contains all the frequent single item
        for(Long item : itemSet.elementSet()) {
            // any item's occurrence >= mini support is considered as frequent item
            long sup = itemSet.count(item);
            double relativeSup = ((double)sup) / transactions.size();
            if (sup >= minAbsSup && relativeSup >= minRelativeSup ){
                ItemSet singleItemSet = new ItemSet();
                singleItemSet.getItemCodes().add(item);
                singleItemSet.setAbsSup(sup);
                singleItemSet.setRelativeSup(relativeSup);
                Lk.add(singleItemSet);
            }
        }
        Collections.sort(Lk);
        Collections.reverse(Lk);

        Lk = Lists.newArrayList(Lk.subList(0, Math.min(Lk.size(), TOP_SKU_COUNT)));


        // ck is the candidate set for level k
        List<ItemSet> Ck = Lists.newArrayList();

        while(!Lk.isEmpty() && k <= K_LIMIT) {
            // generate un-filtered candidate set(ck) by self join Lk-1
            List<LinkedHashSet<Long>> selfJoined = selfJoin(Lk);

            // filter by support to generate Ck
            Ck.clear();
            for(LinkedHashSet<Long> candidateItemCodes: selfJoined) {
                long rule_support = getSupport(transactions, candidateItemCodes, invertedIndex);
                double relative_rule_support = ((double)rule_support) / transactions.size();

                if(rule_support >= minAbsSup &&  relative_rule_support >= minRelativeSup) {
                    ItemSet candidateItemSet = new ItemSet(candidateItemCodes, rule_support, relative_rule_support);
                    Ck.add(candidateItemSet);
                }
            }

            Lk = Lists.newArrayList(Ck);
            Collections.sort(Lk);
            Collections.reverse(Lk);

            Lk = Lists.newArrayList(Lk.subList(0, Math.min(Lk.size(), TOP_SKU_COUNT)));
            frequentItemSets.addAll(Lk);
            k++;
        }

        return frequentItemSets;
    }

    private List<AssociationRule> ruleGeneration(List<Set<Long>> transactions, List<ItemSet> frequentItemSets, double minConfidence, double minLift, SetMultimap<Long, Long> invertedIndex){
        List<AssociationRule> strongRules = Lists.newArrayList();
        for (ItemSet itemSet: frequentItemSets){
            Set fullSet = itemSet.getItemCodes();
            Set<Set<Long>> allSubSets = Sets.powerSet(fullSet);
            for (Set<Long> subSet: allSubSets){
                if (subSet.size() == itemSet.getItemCodes().size() || subSet.size() == 0){
                    continue;
                }
                Set<Long> difference = Sets.difference(fullSet, subSet);
                double supportX = (double)getSupport(transactions, subSet, invertedIndex) / transactions.size();
                double supportY = (double)getSupport(transactions, difference, invertedIndex) / transactions.size();
                double supportXY = itemSet.getRelativeSup();

                double confidence = supportXY / supportX;

                if (confidence >= minConfidence){
                    double lift = supportXY / (supportX * supportY);
                    if (lift > minLift){
                        AssociationRule rule = new AssociationRule(itemSet.getAbsSup(), itemSet.getRelativeSup(), confidence, lift,
                                subSet, difference);
                        strongRules.add(rule);
                    }
                }
            }
        }
        return strongRules;
    }

    private long getSupport(List<Set<Long>> transactions, Set<Long> candidateItemCodes, SetMultimap<Long, Long> invertedIndex) {
        Set<Long> intersection = null;
        for (Long itemId: candidateItemCodes){
            Set<Long> customerIds = invertedIndex.get(itemId);
            if (intersection == null){
                intersection = Sets.newLinkedHashSet(customerIds);
            }else {
                intersection = Sets.intersection(intersection, customerIds);
            }
        }

        return intersection == null? 0: intersection.size();
    }

    // we find two item sets with the same first length-1 item, and combine the last
    // the set should be linkedHashSet to keep its order
    private static List<LinkedHashSet<Long>> selfJoin(List<ItemSet> itemSets){
        List<LinkedHashSet<Long>> result = Lists.newArrayList();

        ListMultimap<List<Long>, Long> prefixMap = ArrayListMultimap.create();
        int length;
        if (itemSets.isEmpty()){
            return result;
        }else{
            length = itemSets.get(0).getItemCodes().size();
        }

        for (ItemSet itemSetRaw: itemSets){
            LinkedHashSet<Long> itemSet = itemSetRaw.getItemCodes();
            List<Long> prefix = Lists.newArrayList();
            for (Long item: itemSet){
                if (prefix.size() < length-1){
                    prefix.add((item));
                }else{
                    prefixMap.put(prefix, item);
                }
            }
        }
        for (List<Long> prefix: prefixMap.keySet()){
            List<Long> lastItems = prefixMap.get(prefix);
            // we needs to generate combination of the last items, thus it must has length >=2
            if (lastItems.size() >= 2){
                for (int i = 0; i < lastItems.size(); i++){
                    for (int j = i+1; j < lastItems.size(); j++){
                        LinkedHashSet<Long> newSet = Sets.newLinkedHashSet(prefix);
                        newSet.add(lastItems.get(i));
                        newSet.add(lastItems.get(j));
                        result.add(newSet);
                    }
                }
            }
        }
        return result;
    }


    public class ItemSet implements Comparable<ItemSet>{
        LinkedHashSet<Long> itemCodes = Sets.newLinkedHashSet();
        Long absSup;
        Double relativeSup;

        ItemSet(){

        }

        ItemSet(LinkedHashSet<Long> itemCodes, Long absSup,
                Double relativeSup){
            this.itemCodes = itemCodes;
            this.absSup = absSup;
            this.relativeSup = relativeSup;
        }

        public void setAbsSup(Long absSup) {
            this.absSup = absSup;
        }

        public void setItemCodes(LinkedHashSet<Long> itemCodes) {
            this.itemCodes = itemCodes;
        }

        public void setRelativeSup(Double relativeSup) {
            this.relativeSup = relativeSup;
        }

        public Double getRelativeSup() {
            return relativeSup;
        }

        public LinkedHashSet<Long> getItemCodes() {
            return itemCodes;
        }

        public Long getAbsSup() {
            return absSup;
        }

        @Override
        public int compareTo(ItemSet o) {
            return this.absSup.compareTo(o.absSup);
        }
    }
}