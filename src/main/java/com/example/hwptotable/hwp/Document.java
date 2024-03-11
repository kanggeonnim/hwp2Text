package com.example.hwptotable.hwp;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Document {
    private List<Table> tables;

    public Document() {
        this.tables = new ArrayList<>();
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Table table : tables) {
            result.append(table).append("\n");
        }
        return result.toString();
    }
}
