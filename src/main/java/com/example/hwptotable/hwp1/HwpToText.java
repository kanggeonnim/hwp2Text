package com.example.hwptotable.hwp1;

import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.bodytext.control.Control;
import kr.dogfoot.hwplib.object.bodytext.control.ControlTable;
import kr.dogfoot.hwplib.object.bodytext.control.ControlType;
import kr.dogfoot.hwplib.object.bodytext.control.table.Cell;
import kr.dogfoot.hwplib.object.bodytext.control.table.Row;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.reader.HWPReader;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class HwpToText {
    public static void hwpToText(String filename) throws Exception {
        HWPFile hwpFile = HWPReader.fromFile(fullPath(filename));

        if (!hwpFile.getBodyText().getSectionList().isEmpty()) {
            Paragraph[] paragraphs = hwpFile.getBodyText().getSectionList().get(0).getParagraphs();
        }
    }

    private static ArrayList<ArrayList<ArrayList<String>>> getHwpTexts(Paragraph[] paragraphs) throws UnsupportedEncodingException {
        ArrayList<ArrayList<ArrayList<String>>> hwpTexts = new ArrayList<>();
        for (Paragraph p : paragraphs) {
            if (p.getText() == null || p.getControlList() == null) continue;
            ArrayList<Control> controls = p.getControlList();
            for (Control c : controls) {
                if (c.getType() == ControlType.Table) {
                    hwpTexts.add(getTableTexts((ControlTable) c));
                }
            }
        }
        return hwpTexts;
    }

    private static ArrayList<ArrayList<String>> getTableTexts(ControlTable c) throws UnsupportedEncodingException {
        ControlTable table = c;
        ArrayList<Row> rows = table.getRowList();
        ArrayList<ArrayList<String>> tableTexts = new ArrayList<>();
        for (Row r : rows) {
            tableTexts.add(getTableRowTexts(r));
        }
        return tableTexts;
    }

    /**
     * 테이블 row에 해당하는 NormalString을 담아서 배열형태로 리턴
     */
    private static ArrayList<String> getTableRowTexts(Row r) throws UnsupportedEncodingException {
        ArrayList<String> rowTexts = new ArrayList<>();
        ArrayList<Cell> cells = r.getCellList();
        for (Cell c : cells) {
            for (Paragraph p : c.getParagraphList()) {
                rowTexts.add(p.getNormalString());
            }
        }
        return rowTexts;
    }

    private static String fullPath(String filename) {
        return "C:/Users/SSAFY/Downloads/21대 국회의원 공약이행현황/" + filename;
    }
}
