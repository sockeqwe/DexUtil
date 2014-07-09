package com.android.dex;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Summary {
    public static void main(String[] args) throws IOException {
        Dex dex = new Dex(new File(args[0]));
        Map<String, AtomicInteger> packageToCount = new TreeMap<String, AtomicInteger>();
        for (MethodId methodId : dex.methodIds()) {
            String typeName = dex.typeNames().get(methodId.getDeclaringClassIndex());
            String packageName = typeNameToPackageName(typeName);
            AtomicInteger count = packageToCount.get(packageName);
            if (count == null) {
                count = new AtomicInteger();
                packageToCount.put(packageName, count);
            }
            count.incrementAndGet();
        }
        int totalCount = 0;

        Map<String, Integer> prefixCountMap = new TreeMap<String, Integer>();
        String previousPackage = null;


        System.out.println(" ============== DETAILS ==============");
        for (Map.Entry<String, AtomicInteger> entry : packageToCount.entrySet()) {
            String packageName = entry.getKey();
            int value = entry.getValue().get();
            totalCount += value;

            System.out.printf("% 8d %s%n", value, packageName);

        }

        System.out.append("\n\n");
        System.out.println(" ============== TOTAL ==============");
        System.out.println("Total: " + totalCount);
    }

   /* *//**
     * @param p1
     * @param p2
     * @return  a Pair, with int as first indicating which parameter is
     *//*
    private static String getPrefix(String p1, String p2) {
        // Find shortest prefix

        String [] ps1 = p1.split("\\.");
        String [] ps2 = p2.split("\\.");

        if (ps1.length < ps2.length) {

            return calcPrefix(ps1, ps2);

        } else {

            return calcPrefix(ps2, ps1);

        }
    }*/

    /*private static String calcPrefix(String[] shorterOne, String[] longerOne){

        String prefix = "";

        if (shorterOne.length <= 1){
            return prefix;
        }

        for (int i = 0; i< shorterOne.length; i++){
            if (!shorterOne[i].equals(longerOne[i])) {
                return prefix;
            } else {

                if (i > 0 ){
                    prefix += ".";
                }
                    prefix += shorterOne[i];

            }
        }

        return prefix;
    }*/

    private static String typeNameToPackageName(String typeName) {
        if (typeName.startsWith("[")) typeName = typeName.substring(1); // arrays
        if (!typeName.contains("/")) return "<default>"; // default package
        return typeName.substring(1, typeName.lastIndexOf('/')).replaceAll("/", ".");
    }
}
