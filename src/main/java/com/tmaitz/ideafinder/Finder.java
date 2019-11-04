package com.tmaitz.ideafinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Finder {

    public List<String> find(List<String> classes, String pattern) {
        return find1(classes, pattern);
    }

    private List<String> find1(List<String> classes, String pattern) {
        final Map<String, String> resultMap = new HashMap<>();
        if (pattern.equals(pattern.toLowerCase())) {
            pattern = pattern.toUpperCase();
        }

        for (String classWithPackage : classes) {
            final String className = extractClassName(classWithPackage);
            resultMap.put(classWithPackage, className);
        }

        boolean masked = false;
        for (int i = 0; i < pattern.length(); i++) {
            char ch = pattern.charAt(i);
            if (ch == '*') {
                // if current char is '*' mean that next chars in classname may be different than the next character pattern
                // set masked flag true and iterate to next char in pattern
                masked = true;
                continue;
            }
            for (String classNameWithPackage : resultMap.keySet()) {
                final String className = resultMap.get(classNameWithPackage);
                resultMap.put(classNameWithPackage, processClassName(ch, className, masked));
            }
            masked = false;
        }

        return extractSortedPackages(resultMap);
    }

    private List<String> extractSortedPackages(Map<String, String> resultMap) {
        return resultMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .sorted((entry1, entry2) -> {
                    var cn1 = extractClassName(entry1.getKey());
                    var cn2 = extractClassName(entry2.getKey());
                    return cn1.compareToIgnoreCase(cn2);
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private String extractClassName(String classWithPackage) {
        return classWithPackage.substring(classWithPackage.lastIndexOf('.') + 1);
    }

    private String processClassName(char ch, String className, boolean masked) {
        // not matched -> skip
        if (className == null) return null;
        if (ch == ' ') {
            // in case of whitespace -> skip if word not finished
            // otherwise -> save this word
            return className.length() == 0 ? className : null;
        }
        for (int j = 0; j < className.length(); j++) {
            if (className.charAt(j) == ch) {
                return className.substring(j + 1);
            }
            if (Character.isUpperCase(ch) || masked) continue;
            return null;
        }
        return null;
    }

}
