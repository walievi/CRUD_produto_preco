package com.walievi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tela {
    private final Map<String, Integer> columns = new LinkedHashMap<>();
    private final Map<String, String> lineValues = new LinkedHashMap<>();
    private final List<Map<String, String>> lines = new ArrayList<>();
    private final List<String> menuOptions = new ArrayList<>();

    public void newColumn(String title, int size) {
        columns.put(title, size);
    }

    public void addValue(String column, String value) {
        if (columns.containsKey(column)) {
            int maxSize = columns.get(column);
            String truncatedValue = value.length() > maxSize ? value.substring(0, maxSize) : value;
            lineValues.put(column, truncatedValue);
        }
    }

    public void addMenuOption(String option) {
        menuOptions.add(option);
    }

    public void addLine() {
        lines.add(new LinkedHashMap<>(lineValues));
        lineValues.clear();
    }

    public void printHeader() {
        for (Map.Entry<String, Integer> entry : columns.entrySet()) {
            String title = entry.getKey();
            int size = entry.getValue();
            System.out.printf("◆ %-" + size + "s ", title);
        }
        System.out.println("◆");
    }

    public void printSeparator() {
        int totalLength = columns.values().stream().mapToInt(Integer::intValue).sum();
        totalLength += columns.size() * 2;
        totalLength += columns.size();

        for (int i = 0; i < totalLength; i++) {
            System.out.print("◆");
        }
        System.out.println("◆");
    }


    public void printMenu() {
        // Calcula o tamanho total da tabela
        int totalLength = columns.values().stream().mapToInt(Integer::intValue).sum();
        totalLength += columns.size() * 2; // Espaços em ambos os lados de cada valor
        totalLength += columns.size(); // Caracteres "◆" entre as colunas e nas extremidades


        // Imprime as opções do menu
        for (int i = 0; i < menuOptions.size(); i++) {
            String option = (i + 1) + ". " + menuOptions.get(i);
            String truncatedOption = option.length() > totalLength ? option.substring(0, totalLength) : option;
            System.out.printf("◆ %-" + (totalLength - 3) + "s ◆\n", truncatedOption);
        }
        printSeparator();
    }


    public void print() {
        printSeparator();
        printHeader();
        printSeparator();

        for (Map<String, String> line : lines) {
            for (Map.Entry<String, Integer> entry : columns.entrySet()) {
                String title = entry.getKey();
                int size = entry.getValue();
                String value = line.getOrDefault(title, "---");
                System.out.printf("◆ %-" + size + "s ", value);
            }
            System.out.println("◆");
        }

        lines.clear();
        printSeparator();

        printMenu();
    }
}