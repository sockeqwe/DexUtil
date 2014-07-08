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
        for (Map.Entry<String, AtomicInteger> entry : packageToCount.entrySet()) {
            System.out.printf("% 8d %s%n", entry.getValue().get(), entry.getKey());
        }
    }

    private static String typeNameToPackageName(String typeName) {
        if (typeName.startsWith("[")) typeName = typeName.substring(1); // arrays
        if (!typeName.contains("/")) return "<default>"; // default package
        return typeName.substring(1, typeName.lastIndexOf('/')).replaceAll("/", ".");
    }
}
