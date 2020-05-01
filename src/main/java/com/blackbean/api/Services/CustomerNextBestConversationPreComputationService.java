package com.blackbean.api.Services;

import com.google.common.collect.*;
import com.opencsv.CSVReader;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class CustomerNextBestConversationPreComputationService  {



    private UpsellBasketRecommendationPreComputationService upsellBasketRecommendationPreComputationService = new UpsellBasketRecommendationPreComputationService();

    private List<AssociationRule> generateAssociationRules(SetMultimap<Long, Long> customerPH3Data, String businessUnitCode, String stateCode) {
        SetMultimap<Long, Long> invertedIndex = LinkedHashMultimap.create();
        List<Set<Long>> transactions = Lists.newArrayList();
        Multiset<Long> itemSet = HashMultiset.create();

        for (Long customerId : customerPH3Data.keySet()) {
            Set<Long> boughtPH3s = Sets.newLinkedHashSet(customerPH3Data.get(customerId));
            for (Long ph3 : boughtPH3s) {
                invertedIndex.put(ph3, customerId);
            }
            transactions.add(boughtPH3s);
            itemSet.addAll(boughtPH3s);
        }

        return upsellBasketRecommendationPreComputationService
                .generateAssociationRules(transactions, itemSet, 5, 0.000001, 0.1, 1.5, invertedIndex);
    }


    public void basketAnalysis() throws ExecutionException, IOException {
        long product_id = 1;
        long invoice_id = 1;
        final Map<String, Long> products = new HashMap<>();
        final Map<String, Long> invoices = new HashMap<>();
        final Map<Long, String> products_r = new HashMap<>();
        final Map<Long, String> invoices_r = new HashMap<>();
        final SetMultimap<Long, Long> invoice_product_data = LinkedHashMultimap.create();



        List<List<String>> dataSet = readingCSV();
        Map<String, Double> sales = new LinkedHashMap<String, Double>();
        for (int i = 1; i< dataSet.size(); i++){
            List<String> row = dataSet.get(i);
            String product = row.get(8);
            double qty = Double.parseDouble(row.get(1));
            double price = Double.parseDouble(row.get(11));
            double cost = qty * price;
            if(sales.containsKey(product)){
                sales.put(product,sales.get(product)+cost);
            } else {
                sales.put(product, cost);
            }
        }

        List<Map.Entry<String, Double>> list =
                new LinkedList<Map.Entry<String, Double>>(sales.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        int count = 0;
        for (Map.Entry<String, Double> entry : list) {
            if (count > 2000){
                break;
            }
            count++;
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        for (int i = 1; i< dataSet.size(); i++){
            List<String> row = dataSet.get(i);
            String invoice = row.get(3);
            String product = row.get(8);
            String category = row.get(9);
            if(product.equals("NULL")) {
                continue;
            }
            if (!products.containsKey(product)){
                products.put(product,product_id);
                products_r.put(product_id,product);
                product_id++;
            }

            if (!invoices.containsKey(invoice)){
                invoices.put(invoice, invoice_id);
                invoices_r.put(invoice_id,invoice);
                invoice_id++;
            }
            if(sortedMap.containsKey(product)) {
                invoice_product_data.put(invoices.get(invoice), products.get(product));
            }
        }

        System.out.println("Start Generating Rules");
        List<AssociationRule> rules = generateAssociationRules(invoice_product_data, null, null);
        Collections.sort(rules, (a, b) -> Double.compare(a.getLift(), b.getLift()));
        Collections.reverse(rules);

        System.out.println("Start Exporting");
        List<Object> export = new ArrayList<>();
        for (AssociationRule r : rules) {
            Long r1 = r.getAntecedent().iterator().next();
            Long r2 = r.getConsequent().iterator().next();
            final String antecedent_product_name = products_r.get(r1);
            final String consequent_product_name = products_r.get(r2);
            final String antecedent_invoice = invoices_r.get(r1);
            final String consequent_invoice = invoices_r.get(r2);

            export.add("Conf=" + r.getConfidence() + ",lift=" + r.getLift() + ",antecedent_product_name=" + antecedent_product_name +
                    ",consequent_product_name=" + consequent_product_name + ",antecedent_invoice=" + antecedent_invoice + ",consequent_invoice=" +
                    consequent_invoice +
                    ",ABsSupport=" + r.getAbs_support() + ",RelativeSupport=" + r.getRelative_support());
        }
        objects2Csv(export, "/Users/chris/Documents/Testing/", "Export_Saales");
//
//
//                List<com.company.AssociationRule> rulesSplit = rules.stream().filter(x -> x.getConfidence() >= 0.7 && x.getLift() >= 2.5).collect(Collectors.toList());
//                Collections.sort(rulesSplit, (com.company.AssociationRule o1, com.company.AssociationRule o2) -> Double.compare(o2.getRelative_support(), o1.getRelative_support()));
//                sortedRules.addAll(rulesSplit);
//
//                rulesSplit = rules.stream().filter(x -> x.getConfidence() >= 0.7 && x.getLift() < 2.5).collect(Collectors.toList());
//                Collections.sort(rulesSplit, (com.company.AssociationRule o1, com.company.AssociationRule o2) -> Double.compare(o2.getRelative_support(), o1.getRelative_support()));
//                sortedRules.addAll(rulesSplit);
//
//                rulesSplit = rules.stream().filter(x -> x.getConfidence() < 0.7 && x.getConfidence() >= 0.4).collect(Collectors.toList());
//                Collections.sort(rulesSplit, (com.company.AssociationRule o1, com.company.AssociationRule o2) -> Double.compare(o2.getLift(), o1.getLift()));
//                sortedRules.addAll(rulesSplit);
//
//                rulesSplit = rules.stream().filter(x -> x.getConfidence() < 0.4).collect(Collectors.toList());
//                Collections.sort(rulesSplit, (com.company.AssociationRule o1, com.company.AssociationRule o2) -> Double.compare(o2.getRelative_support(), o1.getRelative_support()));
//                sortedRules.addAll(rulesSplit);
//
//                rules = sortedRules;
//
//                final SetMultimap<Long, com.company.AssociationRule> antecedentIndex = LinkedHashMultimap.create();
//                final SetMultimap<Long, com.company.AssociationRule> consequentIndex = LinkedHashMultimap.create();
//
//                for (final com.company.AssociationRule rule : rules) {
//                    antecedentIndex.put(rule.getAntecedent().iterator().next(), rule);
//                    consequentIndex.put(rule.getConsequent().iterator().next(), rule);
//                }
    System.out.println("Finish");
    }

    private static List<List<String>> readingCSV(){
        List<List<String>> records = new ArrayList<List<String>>();
        try {
            CSVReader csvReader = new CSVReader(new FileReader("/Users/chris/Downloads/SalesData.csv"));
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    private static void objects2Csv(List<Object> objects,String savePath,String fileName)    throws IOException {
        File file = new File(savePath + fileName + ".csv");
        try (OutputStream out = new FileOutputStream(file);

             OutputStreamWriter writer = new OutputStreamWriter((out),"GBK")) {

            List<String> list = new ArrayList<String>();
            int rowNumCount = objects.size();
            // 获取title
            String title = "";
            boolean titleStatusFlag = false;
            // 循环行Row
            for (int rowNum = 0; rowNum < rowNumCount; rowNum++) {

                // 获取传来的对象数据
                String o = objects.get(rowNum).toString();
                // 获取 对象属性数据对
                String[] entrys = o.split(",");

                // 创建对应的csv 数据对象
                String data = "";
                // 获取当前的 行 Cell 的所有列 Row 数据
                for (int cellNum = 0; cellNum < entrys.length; cellNum++) {
                    String entry = entrys[cellNum];
                    String[] titleAndData = entry.split("=");
                    if(!titleStatusFlag){
                        // title
                        title += titleAndData[0]+",";
                    }
                    // data
                    data += titleAndData[1] + ",";
                }
                titleStatusFlag = true ;
                list.add(data);
            }

            writer.append(title + "\n");
            for (String string : list) {
                writer.append(string + "\n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

