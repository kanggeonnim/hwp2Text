package com.example.hwptotable.hwp;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Table {
    private List<TableRow> rows;

    public Table() {
        this.rows = new ArrayList<>();
    }

    public void addRow(TableRow row) {
        rows.add(row);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (TableRow row : rows) {
            result.append(row).append("\n");
        }
        return result.toString();
    }
}
