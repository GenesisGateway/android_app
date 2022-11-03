package com.emerchantpay.gateway.androidgenesissample.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TransactionTypes {

    private static Map<String, String> transactionTypesMap = new HashMap<String, String>();

    private String displayName;
    private String value;

    public TransactionTypes() {

    }

    public TransactionTypes(String displayName, String value) {
        this.displayName = displayName;
        this.value = value;
        transactionTypesMap.put(displayName, value);
    }

    public static TransactionTypes authorize = new TransactionTypes("Authorize", "authorize");
    public static TransactionTypes authorize3d = new TransactionTypes("Authorize 3D", "authorize3d");
    public static TransactionTypes sale = new TransactionTypes("Sale", "sale");
    public static TransactionTypes sale3d = new TransactionTypes("Sale 3D", "sale3d");
    public static TransactionTypes paysafecard = new TransactionTypes("Paysafecard", "paysafecard");
    public static TransactionTypes initRecurringSale = new TransactionTypes("Init recurring sale", "init_recurring_sale");
    public static TransactionTypes initRecurringSale3d = new TransactionTypes("Init recurring sale 3D", "init_recurring_sale3d");

    public String getDisplayName() {
        return displayName;
    }

    public String getValue() {
        return value;
    }

    // Get Transaction type names list
    public ArrayList<String> getListNames() throws IllegalAccessException {
        getTransactionTypes();

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.addAll(transactionTypesMap.keySet());

        return sortArrayList(arrayList);
    }

    // Get Transaction type values list
    public ArrayList<String> getListValues() throws IllegalAccessException {
        getTransactionTypes();

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.addAll(transactionTypesMap.values());

        return sortArrayList(arrayList);
    }

    // Get Transaction types
    protected Map<String, String> getTransactionTypes() {
        return transactionTypesMap;
    }

    // Sort Array list
    protected ArrayList<String> sortArrayList(ArrayList<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String value2, String value1) {
                return  value2.compareTo(value1);
            }
        });

        return list;
    }
}
