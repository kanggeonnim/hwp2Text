package manifesto;

import hwp.Document;
import hwp.HwpToText2;
import hwp.Table;
import hwp.TableRow;
import kr.dogfoot.hwplib.object.bodytext.control.table.Row;

import java.io.File;

public class ParseManifesto {
    private static String DATA_DIRECTORY = "C:/Users/SSAFY/Downloads/21대 국회의원 공약이행현황/";

    public static void main(String[] args) throws Exception {
        HwpToText2 h2 = new HwpToText2();
//        File dir = new File(DATA_DIRECTORY);
//        for (String filepath : dir.list()) {
//            System.out.println(filepath);
//            Document document = h2.hwpToText(DATA_DIRECTORY + filepath);
//            String name = document.getTables().get(1).getRows().get(0).getCells().get(1);
//            String standingCommittee = document.getTables().get(1).getRows().get(0).getCells().get(3);
//            System.out.println("의원명: " + name);
//            System.out.println("상임위원회: " + standingCommittee);
//            System.out.println("=================================================================");

//        }
        Document document = h2.hwpToText(DATA_DIRECTORY + "057.220831_더불어민주당 부산북강서갑_전재수 의원실_21대 국회의원 공약이행 및 의정활동 평가.hwp");
        String name = document.getTables().get(1).getRows().get(0).getCells().get(1);
        String standingCommittee = document.getTables().get(1).getRows().get(0).getCells().get(3);
        System.out.println("테이블 수: " + document.getTables().size());
        System.out.println("의원명: " + name);
        System.out.println("상임위원회: " + standingCommittee);
        System.out.println("=================================================================");

        int i = 1;
        for (Table t : document.getTables()) {
            System.out.println("===========");
            System.out.println("Table " + i);
            int j = 1;
            for (TableRow tr : t.getRows()) {
                System.out.println("-----------");
                System.out.println("Row " + j);
                System.out.println(tr);
                j++;
            }
            System.out.println(t);
            i++;
        }
    }

}
