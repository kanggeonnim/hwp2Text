package manifesto;

import hwp.Document;
import hwp.HwpToText2;

public class ParseManifesto {
    public static void main(String[] args) throws Exception {
        HwpToText2 h2 = new HwpToText2();
        Document document = h2.hwpToText("057.220831_더불어민주당 부산북강서갑_전재수 의원실_21대 국회의원 공약이행 및 의정활동 평가.hwp");
        String name = document.getTables().get(1).getRows().get(0).getCells().get(1);
        String standingCommittee = document.getTables().get(1).getRows().get(0).getCells().get(3);
        System.out.println("의원명: " + name);
        System.out.println("상임위원회: " + standingCommittee);
        System.out.println("=================================================================");
        System.out.println(document);

    }
}
