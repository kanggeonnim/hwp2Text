package com.example.hwptotable.hwp;

import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.bodytext.control.*;
import kr.dogfoot.hwplib.object.bodytext.control.table.Cell;
import kr.dogfoot.hwplib.object.bodytext.control.table.Row;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.reader.HWPReader;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HwpToText2 {
    public static Document hwpToText(String filename) throws Exception {
        HWPFile hwpFile = HWPReader.fromFile(filename);
        Document document = new Document();
        if (!hwpFile.getBodyText().getSectionList().isEmpty()) {
            Paragraph[] paragraphs = hwpFile.getBodyText().getSectionList().get(0).getParagraphs();
            document = getHwpTexts(paragraphs);
        }
        return document;
    }

    private static Document getHwpTexts(Paragraph[] paragraphs) throws UnsupportedEncodingException {
        Document document = new Document();
        for (Paragraph p : paragraphs) {
            if (p.getText() == null || p.getControlList() == null) continue;
            ArrayList<Control> controls = p.getControlList();
            for (Control c : controls) {
                if (c.getType() == ControlType.Table) {
                    document.addTable(getTableTexts((ControlTable) c));
                }
            }
        }
        return document;
    }

    private static Table getTableTexts(ControlTable c) throws UnsupportedEncodingException {
        Table table = new Table();
        ControlTable controlTable = c;
        List<Row> rows = controlTable.getRowList();
        for (Row r : rows) {
            table.addRow(getTableRowTexts(r));
        }
        return table;
    }

    /**
     * 테이블 row에 해당하는 NormalString을 담아서 배열 형태로 리턴
     */
    private static TableRow getTableRowTexts(Row r) throws UnsupportedEncodingException {
        TableRow tableRow = new TableRow();
        List<Cell> cells = r.getCellList();
        for (Cell c : cells) {
            System.out.println(c);
            StringBuilder str = new StringBuilder();
            for (Paragraph p : c.getParagraphList()) {
                str.append(p.getNormalString());
            }
            tableRow.addCell(str.toString());
        }
        return tableRow;
    }
}

