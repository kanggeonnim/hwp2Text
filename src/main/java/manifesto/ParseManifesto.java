package manifesto;

import hwp.Document;
import hwp.HwpToText2;
import hwp.Table;
import hwp.TableRow;
import kr.dogfoot.hwplib.object.bodytext.control.table.Row;

import java.io.File;

public class ParseManifesto {
    //    private static String DATA_DIRECTORY = "C:/Users/SSAFY/Downloads/21대 국회의원 공약이행현황/";   // window
    private static String DATA_DIRECTORY = "/Users/kanggeon/Downloads/01.서울지역/";  // mac

    public static void main(String[] args) throws Exception {
        HwpToText2 h2 = new HwpToText2();
        File dir = new File(DATA_DIRECTORY);
        for (String filepath : dir.list()) {
            System.out.println(filepath);
            if (filepath.matches("^\\..+")) continue;
            parse(filepath, h2);
        }

//        parse("046.배현진국회의원_공약이행 및 의정활동관련질의서_최종.hwp", h2);

//        Document document = h2.hwpToText(DATA_DIRECTORY + "046.배현진국회의원_공약이행 및 의정활동관련질의서_최종.hwp");
//        int i = 0;
//        for (Table t : document.getTables()) {
//            System.out.println("===========");
//            System.out.println("Table " + i);
//            int j = 0;
//            for (TableRow tr : t.getRows()) {
//                if (document.getTables().get(i).getRows().get(0).getCells().get(0).equals("국정현안과제 내용")) break;
//                if (i == 8 && j == 0) {
//                    System.out.println("@@@" + document.getTables().get(i).getRows().get(0).getCells().get(0));
//                    System.out.println(document.getTables().get(i).getRows().get(0).getCells().get(0).equals("국정현안과제 내용"));
//                }
//                System.out.println("-----------");
//                System.out.println("Row " + j);
//                System.out.println(tr);
//                j++;
//            }
//            System.out.println(t);
//            i++;
//        }
    }

    private static void parse(String filepath, HwpToText2 h2) throws Exception {
        Document document = h2.hwpToText(DATA_DIRECTORY + filepath);
        String 의원명 = document.getTables().get(1).getRows().get(0).getCells().get(1);
        String 상임위원회 = document.getTables().get(1).getRows().get(0).getCells().get(3);
        String 소속정당 = document.getTables().get(1).getRows().get(1).getCells().get(1);
        String 지역구 = document.getTables().get(1).getRows().get(1).getCells().get(3);
        String 공약이행현황_총공약 = document.getTables().get(2).getRows().get(2).getCells().get(0);
        String 공약이행현황_완료 = document.getTables().get(2).getRows().get(2).getCells().get(1);
        String 공약이행현황_추진중 = document.getTables().get(2).getRows().get(2).getCells().get(2);
        String 공약이행현황_보류 = document.getTables().get(2).getRows().get(2).getCells().get(3);
        String 공약이행현황_폐기 = document.getTables().get(2).getRows().get(2).getCells().get(4);
        String 공약이행현황_기타 = document.getTables().get(2).getRows().get(2).getCells().get(5);

        String 성격별완료현황_국정공약 = document.getTables().get(2).getRows().get(2).getCells().get(6);
        String 성격별완료현황_지역공약 = document.getTables().get(2).getRows().get(2).getCells().get(7);
        String 성격별완료현황_입법공약 = document.getTables().get(2).getRows().get(2).getCells().get(8);
        String 성격별완료현황_재정공약 = document.getTables().get(2).getRows().get(2).getCells().get(9);
        String 성격별완료현황_임기내 = document.getTables().get(2).getRows().get(2).getCells().get(10);
        String 성격별완료현황_임기후 = document.getTables().get(2).getRows().get(2).getCells().get(11);
        String 성격별완료현황_지속사업 = document.getTables().get(2).getRows().get(2).getCells().get(12);
        String 성격별완료현황_신규사업 = document.getTables().get(2).getRows().get(2).getCells().get(13);

        String 입법현황_필요입법공약총수 = document.getTables().get(3).getRows().get(2).getCells().get(0);
        String 입법현황_입법의결완료공약총수 = document.getTables().get(3).getRows().get(2).getCells().get(1);
        String 재정현황_필요재정총액 = document.getTables().get(3).getRows().get(2).getCells().get(2);
        String 재정현황_확보재정총액 = document.getTables().get(3).getRows().get(2).getCells().get(3);
        String 재정현황_집행재정총액 = document.getTables().get(3).getRows().get(2).getCells().get(4);

        for (int i = 4; i < document.getTables().size(); i++) {
            if (document.getTables().get(i).getRows().get(0).getCells().get(0).equals("국정현안과제 내용")) break;
            for (int j = 3; j < document.getTables().get(i).getRows().size(); j += 6) {
                if (document.getTables().get(i).getRows().get(j).getCells().size() > 1) {
                    System.out.println(document.getTables().get(i).getRows().get(j).getCells().get(1));
                    System.out.println("-----");
                }
            }
        }
        System.out.println("의원명: " + 의원명);
        System.out.println("상임위원회: " + 상임위원회);
        System.out.println("소속정당: " + 소속정당);
        System.out.println("지역구: " + 지역구);
        System.out.println("공약이행현황_총공약: " + 공약이행현황_총공약);
        System.out.println("공약이행현황_완료: " + 공약이행현황_완료);
        System.out.println("성격별완료현황_입법공약: " + 성격별완료현황_입법공약);
        System.out.println("입법현황_입법의결완료공약총수: " + 입법현황_입법의결완료공약총수);
        System.out.println("재정현황_집행재정총액: " + 재정현황_집행재정총액);
        System.out.println("=================================================================");
    }

}
