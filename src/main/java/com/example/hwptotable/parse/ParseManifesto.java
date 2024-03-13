package com.example.hwptotable.parse;

import com.example.hwptotable.assembly.entity.DetailedPledgeExecutionStatus;
import com.example.hwptotable.assembly.entity.PledgeFulfillmentRate;
import com.example.hwptotable.assembly.repository.PledgeFulfillmentRateRepository;
import com.example.hwptotable.hwp.Document;
import com.example.hwptotable.hwp.HwpToText2;
import com.example.hwptotable.hwp.Table;
import com.example.hwptotable.hwp.TableRow;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            if (!filepath.substring(filepath.length() - 3).equals("hwp")) {
                continue;
            }
            try {

                Document document = h2.hwpToText(DATA_DIRECTORY + filepath);

                PledgeFulfillmentRate pledgeFulfillmentRate = createPledgeFulfillmentRate(document);
                pledgeFulfillmentRateRepository.save(pledgeFulfillmentRate);
                addDetailPledges(filepath, document, pledgeFulfillmentRate);
            } catch (Exception e) {
                log.info("오류발생: " + filepath);
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        HwpToText2 h2 = new HwpToText2();
        Document document = h2.hwpToText(DATA_DIRECTORY + "223.공약이행및의정활동_질의서_김병욱의원(포항).hwp");
        int i = 0;
        for (Table t : document.getTables()) {
            System.out.println("===========");
            System.out.println("Table " + i);
            int j = 0;
            for (TableRow tr : t.getRows()) {
                System.out.println("-----------");
                System.out.println("Row " + j);
                System.out.println(tr);
                j++;
            }
            System.out.println(t);
            i++;
        }

        for (int ii = 4; ii < document.getTables().size(); ii++) {
            if (!document.getTables().get(ii).getRows().get(0).getCells().get(0).equals("순번")) break;
            for (int jj = 3; jj < document.getTables().get(ii).getRows().size(); jj += 6) {
                String pledgeName = document.getTables().get(ii).getRows().get(jj).getCells().get(1);
                System.out.println(pledgeName);
            }
        }
    }

    private static PledgeFulfillmentRate createPledgeFulfillmentRate(Document document) {

        PledgeFulfillmentRate pledgeFulfillmentRate = PledgeFulfillmentRate.builder()
                .legislatorName(getLegislatorName(document))
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

    private static String getLegislatorName(Document document) {
        String name = document.getTables().get(1).getRows().get(0).getCells().get(1);
        if (name.length() > 3) {
            if (name.substring(name.length() - 3).equals("의원실"))
                name = name.substring(0, name.length() - 3);
            else if (name.substring(name.length() - 2).equals("의원")) {
                name = name.substring(0, name.length() - 2);
            }
        }
        return name.replace(" ", "");
    }

    private static void addDetailPledges(String filepath, Document document, PledgeFulfillmentRate pledgeFulfillmentRate) {
        for (int i = 4; i < document.getTables().size(); i++) {
            if (!document.getTables().get(i).getRows().get(0).getCells().get(0).equals("순번")) break;
            for (int j = 3; j < document.getTables().get(i).getRows().size(); j += 6) {
                try {
                    String pledgeName = document.getTables().get(i).getRows().get(j).getCells().get(1);

                    DetailedPledgeExecutionStatus detailedPledgeExecutionStatus = DetailedPledgeExecutionStatus.builder()
                            .turn(Integer.parseInt(document.getTables().get(i).getRows().get(j).getCells().get(0)))
                            .pledgeName(document.getTables().get(i).getRows().get(j).getCells().get(1))
                            .pledgeSummary(document.getTables().get(i).getRows().get(j + 3).getCells().get(0))
                            .natureDivision_NationalRegional(document.getTables().get(i).getRows().get(j).getCells().get(2))
                            .natureDivision_LegislationFinance(document.getTables().get(i).getRows().get(j).getCells().get(3))
                            .contentDivision_InTerm_OutTerm(document.getTables().get(i).getRows().get(j).getCells().get(4))
                            .contentDivision_Continued_New(document.getTables().get(i).getRows().get(j).getCells().get(5))
                            .fulfillmentRate(document.getTables().get(i).getRows().get(j).getCells().get(6))
                            .legislativeName(document.getTables().get(i).getRows().get(j).getCells().get(7))
                            .proposal(document.getTables().get(i).getRows().get(j + 2).getCells().get(0))
                            .standingCommittee(document.getTables().get(i).getRows().get(j + 2).getCells().get(1))
                            .generalScope(document.getTables().get(i).getRows().get(j + 2).getCells().get(1))
                            .plenarySessionResolution(document.getTables().get(i).getRows().get(j + 2).getCells().get(3))
                            .legislativeDetails(document.getTables().get(i).getRows().get(j + 3).getCells().get(1))
                            .requiredBudgetAmount(document.getTables().get(i).getRows().get(j).getCells().get(9))
                            .securedBudgetAmount(document.getTables().get(i).getRows().get(j + 1).getCells().get(5))
                            .executedBudgetAmount(document.getTables().get(i).getRows().get(j + 2).getCells().get(5))
                            .securedDetails(document.getTables().get(i).getRows().get(j + 3).getCells().get(2))
                            .executionDetails(document.getTables().get(i).getRows().get(j + 4).getCells().get(0))
                            .otherImplementationBasis(document.getTables().get(i).getRows().get(j + 5).getCells().get(0))
                            .pledgeFulfillmentRate(pledgeFulfillmentRate)
                            .build();
                    pledgeFulfillmentRate.addPledge(detailedPledgeExecutionStatus);
                } catch (Exception e) {
                    log.info("오류 발생:" + document.getTables().get(i).getRows().get(j).getCells().get(1));
//                    e.printStackTrace();
                }
            }
        }
    }

}

