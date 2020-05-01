package com.blackbean.api.Services;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class EntityCreationServiceImpl implements EntityCreationService {

    @Override
    public Boolean createEntity(String filepath) {
        List<List<String>> dataSet = readingCSV(filepath);
        if(!validating(dataSet)){
            return false;
        }
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

        return true;
    }


    private boolean validating(List<List<String>> dataSet){
        return true;
    }

    private static List<List<String>> readingCSV(String filepath){
        filepath = "/Users/chris/Downloads/SalesData.csv";
        List<List<String>> records = new ArrayList<List<String>>();
        try {
            CSVReader csvReader = new CSVReader(new FileReader(filepath));
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
}
