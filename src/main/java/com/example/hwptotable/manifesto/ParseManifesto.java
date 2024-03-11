package com.example.hwptotable.manifesto;

import com.example.hwptotable.hwp.Document;
import com.example.hwptotable.hwp.HwpToText2;
import com.example.hwptotable.hwp.Table;
import com.example.hwptotable.hwp.TableRow;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class ParseManifesto {
    private static String DATA_DIRECTORY = "C:/Users/SSAFY/Downloads/21대 국회의원 공약이행현황/";   // window
    //    private static String DATA_DIRECTORY = "/Users/kanggeon/Downloads/01.서울지역/";  // mac

    private final PledgeFulfillmentRateRepository pledgeFulfillmentRateRepository;

    public void parseAll() throws Exception {

        File dir = new File(DATA_DIRECTORY);
        for (String filepath : dir.list()) {
            HwpToText2 h2 = new HwpToText2();
            if (!filepath.matches("\\b[가-힣\\w\\s`~!@#$%^&*()-_+=\\[{\\]};:'\",<.>/?]+\\.hwp\\b")) continue;

            try {

                Document document = h2.hwpToText(DATA_DIRECTORY + filepath);

                PledgeFulfillmentRate pledgeFulfillmentRate = createPledgeFulfillmentRate(document);
                pledgeFulfillmentRateRepository.save(pledgeFulfillmentRate);

                addDetailPledges(filepath, document, pledgeFulfillmentRate);

            } catch (Exception e) {
                System.out.println("오류발생: " + filepath);
                e.printStackTrace();
            }
        }
    }

    private static void addDetailPledges(String filepath, Document document, PledgeFulfillmentRate pledgeFulfillmentRate) {
        for (int i = 4; i < document.getTables().size(); i++) {
            if (!document.getTables().get(i).getRows().get(0).getCells().get(0).equals("순번")) break;
            for (int j = 3; j < document.getTables().get(i).getRows().size(); j += 6) {

//                if (document.getTables().get(i).getRows().get(j).getCells().size() > 1) {
//                    String pledgeName;
//                    if (document.getTables().get(i).getRows().get(j).getCells().get(1).equals("공약명 : ")) {
//                        pledgeName = document.getTables().get(i).getRows().get(j).getCells().get(2);
//                    } else {
//                        pledgeName = document.getTables().get(i).getRows().get(j).getCells().get(1);
//                    }
                try {

                    String pledgeName = document.getTables().get(i).getRows().get(j).getCells().get(1);

                    DetailedPledgeExecutionStatus detailedPledgeExecutionStatus = DetailedPledgeExecutionStatus.builder()
                            .turn(Integer.parseInt(document.getTables().get(i).getRows().get(j).getCells().get(0)))
                            .pledgeName(document.getTables().get(i).getRows().get(j).getCells().get(1))
                            .build();
                    System.out.println("성격별구분_국정/지역: " + document.getTables().get(i).getRows().get(j).getCells().get(2));
                    System.out.println("성격별구분_입법/재정: " + document.getTables().get(i).getRows().get(j).getCells().get(3));
                    System.out.println("내용별구분_임기내/임기후: " + document.getTables().get(i).getRows().get(j).getCells().get(4));
                    System.out.println("내용별구분_지속/신규: " + document.getTables().get(i).getRows().get(j).getCells().get(5));
                    System.out.println("-----");
                } catch (Exception e) {
                    System.out.println("오류 발생:" + filepath);
                    break;
                }

            }
//            }
        }
    }

    public static void main(String[] args) throws Exception {
        HwpToText2 h2 = new HwpToText2();
        Document document = h2.hwpToText(DATA_DIRECTORY + "004.[권영세의원실] 대국회의원_공약이행및의정활동 질의서(최종).hwp");
        int i = 0;
        for (Table t : document.getTables()) {
            System.out.println("===========");
            System.out.println("Table " + i);
            int j = 0;
            for (TableRow tr : t.getRows()) {
//                if (!document.getTables().get(i).getRows().get(0).getCells().get(0).equals("순번")) break;
                if (i == 8 && j == 0) {
                    System.out.println("@@@" + document.getTables().get(i).getRows().get(0).getCells().get(0));
                    System.out.println(!document.getTables().get(i).getRows().get(0).getCells().get(0).equals("순번"));
                }
                System.out.println("-----------");
                System.out.println("Row " + j);
                System.out.println(tr);
                j++;
            }
            System.out.println(t);
            i++;
        }
    }


    private static PledgeFulfillmentRate createPledgeFulfillmentRate(Document document) {

        PledgeFulfillmentRate pledgeFulfillmentRate = PledgeFulfillmentRate.builder()
                .legislatorName(document.getTables().get(1).getRows().get(0).getCells().get(1))
                .standingCommittee(document.getTables().get(1).getRows().get(0).getCells().get(3))
                .affiliatedParty(document.getTables().get(1).getRows().get(1).getCells().get(1))
                .electoralDistrict(document.getTables().get(1).getRows().get(1).getCells().get(3))
                .totalPledges(document.getTables().get(2).getRows().get(2).getCells().get(0))
                .completedPledges(document.getTables().get(2).getRows().get(2).getCells().get(1))
                .ongoingPledges(document.getTables().get(2).getRows().get(2).getCells().get(2))
                .pendingPledges(document.getTables().get(2).getRows().get(2).getCells().get(3))
                .discardedPledges(document.getTables().get(2).getRows().get(2).getCells().get(4))
                .otherPledges(document.getTables().get(2).getRows().get(2).getCells().get(5))
                .nationalPledges(document.getTables().get(2).getRows().get(2).getCells().get(6))
                .regionalPledges(document.getTables().get(2).getRows().get(2).getCells().get(7))
                .legislativePledges(document.getTables().get(2).getRows().get(2).getCells().get(8))
                .financialPledges(document.getTables().get(2).getRows().get(2).getCells().get(9))
                .inTermPledges(document.getTables().get(2).getRows().get(2).getCells().get(10))
                .outTermPledges(document.getTables().get(2).getRows().get(2).getCells().get(11))
                .ongoingProjects(document.getTables().get(2).getRows().get(2).getCells().get(12))
                .newProjects(document.getTables().get(2).getRows().get(2).getCells().get(13))
                .totalRequiredLegislativePledges(document.getTables().get(3).getRows().get(2).getCells().get(0))
                .totalLegislativeResolutionCompletedPledges(document.getTables().get(3).getRows().get(2).getCells().get(1))
                .totalRequiredBudget(document.getTables().get(3).getRows().get(2).getCells().get(2))
                .totalSecuredBudget(document.getTables().get(3).getRows().get(2).getCells().get(3))
                .totalExecutedBudget(document.getTables().get(3).getRows().get(2).getCells().get(4))
                .build();

        return pledgeFulfillmentRate;
    }
}

//        String 의원명 = document.getTables().get(1).getRows().get(0).getCells().get(1);
//        String 상임위원회 = document.getTables().get(1).getRows().get(0).getCells().get(3);
//        String 소속정당 = document.getTables().get(1).getRows().get(1).getCells().get(1);
//        String 지역구 = document.getTables().get(1).getRows().get(1).getCells().get(3);
//        String 공약이행현황_총공약 = document.getTables().get(2).getRows().get(2).getCells().get(0);
//        String 공약이행현황_완료 = document.getTables().get(2).getRows().get(2).getCells().get(1);
//        String 공약이행현황_추진중 = document.getTables().get(2).getRows().get(2).getCells().get(2);
//        String 공약이행현황_보류 = document.getTables().get(2).getRows().get(2).getCells().get(3);
//        String 공약이행현황_폐기 = document.getTables().get(2).getRows().get(2).getCells().get(4);
//        String 공약이행현황_기타 = document.getTables().get(2).getRows().get(2).getCells().get(5);
//
//        String 성격별완료현황_국정공약 = document.getTables().get(2).getRows().get(2).getCells().get(6);
//        String 성격별완료현황_지역공약 = document.getTables().get(2).getRows().get(2).getCells().get(7);
//        String 성격별완료현황_입법공약 = document.getTables().get(2).getRows().get(2).getCells().get(8);
//        String 성격별완료현황_재정공약 = document.getTables().get(2).getRows().get(2).getCells().get(9);
//        String 성격별완료현황_임기내 = document.getTables().get(2).getRows().get(2).getCells().get(10);
//        String 성격별완료현황_임기후 = document.getTables().get(2).getRows().get(2).getCells().get(11);
//        String 성격별완료현황_지속사업 = document.getTables().get(2).getRows().get(2).getCells().get(12);
//        String 성격별완료현황_신규사업 = document.getTables().get(2).getRows().get(2).getCells().get(13);
//
//        String 입법현황_필요입법공약총수 = document.getTables().get(3).getRows().get(2).getCells().get(0);
//        String 입법현황_입법의결완료공약총수 = document.getTables().get(3).getRows().get(2).getCells().get(1);
//        String 재정현황_필요재정총액 = document.getTables().get(3).getRows().get(2).getCells().get(2);
//        String 재정현황_확보재정총액 = document.getTables().get(3).getRows().get(2).getCells().get(3);
//        String 재정현황_집행재정총액 = document.getTables().get(3).getRows().get(2).getCells().get(4);
//
//        for (int i = 4; i < document.getTables().size(); i++) {
//            if (!document.getTables().get(i).getRows().get(0).getCells().get(0).equals("순번")) break;
//            for (int j = 3; j < document.getTables().get(i).getRows().size(); j += 6) {
//                if (document.getTables().get(i).getRows().get(j).getCells().size() > 1) {
//                    if (document.getTables().get(i).getRows().get(j).getCells().get(1).equals("공약명 : ")) {
//                        System.out.println(document.getTables().get(i).getRows().get(j).getCells().get(2));
//                    } else {
//                        System.out.println(document.getTables().get(i).getRows().get(j).getCells().get(1));
//                    }
//                    try {
//                        System.out.println("성격별구분_국정/지역: " + document.getTables().get(i).getRows().get(j).getCells().get(2));
//                        System.out.println("성격별구분_입법/재정: " + document.getTables().get(i).getRows().get(j).getCells().get(3));
//                        System.out.println("내용별구분_임기내/임기후: " + document.getTables().get(i).getRows().get(j).getCells().get(4));
//                        System.out.println("내용별구분_지속/신규: " + document.getTables().get(i).getRows().get(j).getCells().get(5));
//                        System.out.println("-----");
//                    } catch (Exception e) {
//                        System.out.println("오류 발생:" + filepath);
//                        break;
//                    }
//
//                }
//            }
//        }
//        System.out.println("의원명: " + 의원명);
//        System.out.println("상임위원회: " + 상임위원회);
//        System.out.println("소속정당: " + 소속정당);
//        System.out.println("지역구: " + 지역구);
//        System.out.println("공약이행현황_총공약: " + 공약이행현황_총공약);
//        System.out.println("공약이행현황_완료: " + 공약이행현황_완료);
//        System.out.println("성격별완료현황_입법공약: " + 성격별완료현황_입법공약);
//        System.out.println("입법현황_입법의결완료공약총수: " + 입법현황_입법의결완료공약총수);
//        System.out.println("재정현황_집행재정총액: " + 재정현황_집행재정총액);
//        System.out.println("=================================================================");
