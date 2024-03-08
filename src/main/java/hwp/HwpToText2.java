package hwp;

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
        HWPFile hwpFile = HWPReader.fromFile(fullPath(filename));
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
            for (Paragraph p : c.getParagraphList()) {
                tableRow.addCell(p.getNormalString());
            }
        }
        return tableRow;
    }

    private static String fullPath(String filename) {
        return "C:/Users/SSAFY/Downloads/21대 국회의원 공약이행현황/" + filename;
    }

    public static void main(String[] args) throws Exception {
        Document document = hwpToText("057.220831_더불어민주당 부산북강서갑_전재수 의원실_21대 국회의원 공약이행 및 의정활동 평가.hwp");

        System.out.println(document.getTables().get(1).getRows().get(0).getCells().get(1));
        System.out.println("=================================================================");
        System.out.println(document);

    }
}

