package com.example.hwptotable.parse;

import com.example.hwptotable.assembly.entity.AttendanceRate;
import com.example.hwptotable.assembly.repository.AttendanceRateRepository;
import com.example.hwptotable.hwp.HwpToText2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class ParseAttendance {
    private final AttendanceRateRepository attendanceRateRepository;
    private static String DATA_DIRECTORY = "C:/Users/SSAFY/Downloads/21대 국회의원 본회의 출결현황/";   // window
    //    private static String DATA_DIRECTORY = "/Users/kanggeon/Downloads/01.서울지역/";  // mac

    public void parseAll() throws Exception {

        File dir = new File(DATA_DIRECTORY);
        HwpToText2 h2 = new HwpToText2();
        String[] lines = null;

        for (String filepath : Objects.requireNonNull(dir.list())) {
            if (!filepath.endsWith("pdf")) {
                continue;
            }
            try {
                File file = new File(DATA_DIRECTORY + filepath);
                PDDocument document = Loader.loadPDF(file);


                PDFTextStripper pdfStripper = new PDFTextStripper();
                String pages = pdfStripper.getText(document);

                lines = pages.split("\r\n|\r|\n");

                parseAttendance(lines);

            } catch (Exception e) {
                log.info("오류발생: " + filepath);
                e.printStackTrace();
            }
        }
    }

    private void parseAttendance(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            // 의원명
            int nameIdx = lines[i].indexOf(" ");
            if (nameIdx <= 0) continue;
            String name = lines[i].substring(0, nameIdx);
            // 정당
            int partyIdx = lines[i].substring(nameIdx + 1).indexOf(" ") + nameIdx + 1;
            if (partyIdx - nameIdx <= 0) continue;

            String party = lines[i].substring(nameIdx + 1, partyIdx);
            if (party.startsWith("2차") || party.startsWith("(202") || party.startsWith("202")) continue;

            // 출석률
            Pattern p = Pattern.compile("[0-9]+[0-9\\s]+");
            Matcher m = p.matcher(lines[i]);

            // 회의일수 출석 결석 청가 출장 결석신고서

            if (m.find()) {
                StringTokenizer st = new StringTokenizer(m.group());
                // 기존값 가져오기
                Optional<AttendanceRate> findRate = attendanceRateRepository.findByLegislatorNameAndAffiliatedParty(name, party);
                if (findRate.isPresent()) {
                    findRate.get().addDays(
                            Integer.parseInt(st.nextToken()),
                            Integer.parseInt(st.nextToken()),
                            Integer.parseInt(st.nextToken()),
                            Integer.parseInt(st.nextToken()),
                            Integer.parseInt(st.nextToken()),
                            Integer.parseInt(st.nextToken()));
                } else {
                    AttendanceRate attendanceRate = AttendanceRate.builder()
                            .legislatorName(name)
                            .affiliatedParty(party)
                            .meetingDays(Integer.parseInt(st.nextToken()))
                            .attendance(Integer.parseInt(st.nextToken()))
                            .absence(Integer.parseInt(st.nextToken()))
                            .leave(Integer.parseInt(st.nextToken()))
                            .businessTrip(Integer.parseInt(st.nextToken()))
                            .absenceReport(Integer.parseInt(st.nextToken()))
                            .build();
                    attendanceRateRepository.save(attendanceRate);
                }
            }
        }
    }

    public static void main(String[] args) {
        DATA_DIRECTORY = "C:/Users/SSAFY/Downloads/21대 국회의원 상임위 출결현황/문제아/";   // window
        File dir = new File(DATA_DIRECTORY);
        HwpToText2 h2 = new HwpToText2();
        String[] lines = null;

        for (String filepath : Objects.requireNonNull(dir.list())) {
            if (!filepath.endsWith("2020년 6월 상임위 출결 현황.pdf")) {
                continue;
            }
            try {
                File file = new File(DATA_DIRECTORY + filepath);
                PDDocument document = Loader.loadPDF(file);


                PDFTextStripper pdfStripper = new PDFTextStripper();
                String pages = pdfStripper.getText(document);

                lines = pages.split("\r\n|\r|\n");

                for (int i = 0; i < lines.length; i++) {
                    System.out.println("lines" + lines[i]);
                    // 의원명
                    int nameIdx = lines[i].indexOf(" ");
                    if (nameIdx <= 0) continue;
                    String name = lines[i].substring(0, nameIdx);
                    // 정당
                    int partyIdx = lines[i].substring(nameIdx + 1).indexOf(" ") + nameIdx + 1;
                    if (partyIdx - nameIdx <= 0) continue;

                    String party = lines[i].substring(nameIdx + 1, partyIdx);
                    if (party.startsWith("회의일수.*") || party.matches("^\\([0-9]+.*") || party.matches("[0-9]+.*") || party.matches("제[0-9]+.*"))
                        continue;

                    // 출석률
                    Pattern p = Pattern.compile("[0-9]+[0-9\\s]+");
                    Matcher m = p.matcher(lines[i]);

                    // 회의일수 출석 결석 청가 출장 결석신고서
                    if (m.find()) {
                        StringTokenizer st = new StringTokenizer(m.group());
                        System.out.println(st.countTokens());
                        System.out.println("@@" + name + "@@" + party + "@@" + m.group());
//                        AttendanceRate attendanceRate = AttendanceRate.builder()
//                                .legislatorName(name)
//                                .affiliatedParty(party)
//                                .meetingDays(Integer.parseInt(st.nextToken()))
//                                .attendance(Integer.parseInt(st.nextToken()))
//                                .absence(Integer.parseInt(st.nextToken()))
//                                .leave(Integer.parseInt(st.nextToken()))
//                                .businessTrip(Integer.parseInt(st.nextToken()))
//                                .absenceReport(Integer.parseInt(st.nextToken()))
//                                .build();

//                        System.out.println(attendanceRate);
                    }
                }

            } catch (Exception e) {
                log.info("오류발생: " + filepath);
                e.printStackTrace();
            }
        }
    }
}