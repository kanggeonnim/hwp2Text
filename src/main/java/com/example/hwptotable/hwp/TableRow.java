package com.example.hwptotable.hwp;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TableRow {
    private List<String> cells;

    public TableRow() {
        this.cells = new ArrayList<>();
    }

    public void addCell(String cell) {
        cells.add(cell);
    }

    @Override
    public String toString() {
        return cells.toString();
    }
}
