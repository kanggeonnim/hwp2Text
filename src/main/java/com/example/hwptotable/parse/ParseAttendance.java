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
import java.util.Arrays;
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

    public void parseAttendance2() {
        DATA_DIRECTORY = "C:/Users/SSAFY/Downloads/21대 국회의원 상임위 출결현황/문제아/";   // window
//        DATA_DIRECTORY = "/Users/kanggeon/Downloads/";
        File dir = new File(DATA_DIRECTORY);
        HwpToText2 h2 = new HwpToText2();
        String[] lines;

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

                int tableSize = 0;
                for (int i = 0; i < lines.length; i++) {
//                    System.out.println("lines" + lines[i]);
                    // 의원명
                    int nameIdx = lines[i].indexOf(" ");
                    if (nameIdx <= 0) continue;
                    String name = lines[i].substring(0, nameIdx);
                    // 정당
                    int partyIdx = lines[i].substring(nameIdx + 1).indexOf(" ") + nameIdx + 1;
                    if (partyIdx - nameIdx <= 0) continue;

                    String party = lines[i].substring(nameIdx + 1, partyIdx);
                    if (party.startsWith("회의일수.*") || party.matches("^\\([0-9]+.*") || party.matches("[0-9]+.*") || party.matches("제[0-9]+.*")) {
                        tableSize = 0;  // 표가 끝난 경우, 기존의 표 크기 초기화
                        continue;
                    }

                    // 출석률
                    Pattern p = Pattern.compile("[0-9]+[0-9\\s]+");
                    Matcher m = p.matcher(lines[i]);

                    // 회의일수 출석 결석 청가 출장 결석신고서
                    if (m.find()) {
                        StringTokenizer st = new StringTokenizer(m.group());
                        // 표가 짤린 경우
                        if (1 <= st.countTokens() && st.countTokens() <= 5) {
                            // 표의 크기를 안구한 경우
                            if (tableSize == 0) {
                                for (int t = 1; ; t++) {
                                    int col = lines[i + t].toCharArray()[0] - '0';
//                                    System.out.println(lines[i + t]);
//                                    System.out.println("col is " + col);

                                    if (0 <= col && col <= 9) {
                                        tableSize = t;
//                                        System.out.println("tableSize is " + tableSize);
                                        break;
                                    }
                                }
                            }
                            // 표의 크기를 구해논 경우
                            {
//                                System.out.println("@@@ table size is" + tableSize);
//                                System.out.println(lines[i]);
//                                System.out.println(lines[i + tableSize]);
                                // 토큰 빼내기
                                int[] counts = new int[6];
                                int c = 0;
                                for (; c < st.countTokens(); c++) {
                                    counts[c] = Integer.parseInt(st.nextToken());
                                }
//                                System.out.println(c);
                                st = new StringTokenizer(lines[i + tableSize]);
//                                System.out.println(st.countTokens());
                                int size = st.countTokens();
                                for (c += 1; c < size; c++) {
                                    counts[c] = Integer.parseInt(st.nextToken());
                                }

//                                System.out.println(Arrays.toString(counts));
                                AttendanceRate attendanceRate = AttendanceRate.builder()
                                        .legislatorName(name)
                                        .affiliatedParty(party)
                                        .meetingDays(counts[0])
                                        .attendance(counts[1])
                                        .absence(counts[2])
                                        .leave(counts[3])
                                        .businessTrip(counts[4])
                                        .absenceReport(counts[5])
                                        .build();
                                System.out.println(attendanceRate);
                                attendanceRateRepository.save(attendanceRate);
                            }
                        } else {
//                            System.out.println("@@" + name + "@@" + party + "@@" + m.group());
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

                            System.out.println(attendanceRate);
                            attendanceRateRepository.save(attendanceRate);
                        }
                    }
                }

            } catch (Exception e) {
                log.info("오류발생: " + filepath);
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        DATA_DIRECTORY = "C:/Users/SSAFY/Downloads/21대 국회의원 상임위 출결현황/문제아/";   // window
//        DATA_DIRECTORY = "/Users/kanggeon/Downloads/";
        File dir = new File(DATA_DIRECTORY);
        HwpToText2 h2 = new HwpToText2();
        String[] lines = null;

        for (String filepath : Objects.requireNonNull(dir.list())) {
            if (!filepath.endsWith("2020년 7월 상임위 현황.pdf")) {
                continue;
            }
            try {
                File file = new File(DATA_DIRECTORY + filepath);
                PDDocument document = Loader.loadPDF(file);


                PDFTextStripper pdfStripper = new PDFTextStripper();
                String pages = pdfStripper.getText(document);

                lines = pages.split("\r\n|\r|\n");

                int tableSize = 0;
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
                    if (party.startsWith("회의일수.*") || party.matches("^\\([0-9]+.*") || party.matches("[0-9]+.*") || party.matches("제[0-9]+.*")) {
                        tableSize = 0;  // 표가 끝난 경우, 기존의 표 크기 초기화
                        continue;
                    }

                    // 출석률
                    Pattern p = Pattern.compile("[0-9]+[0-9\\s]+");
                    Matcher m = p.matcher(lines[i]);

                    // 회의일수 출석 결석 청가 출장 결석신고서
                    if (m.find()) {
                        StringTokenizer st = new StringTokenizer(m.group());
                        // 표가 짤린 경우
                        if (1 <= st.countTokens() && st.countTokens() <= 5) {
                            // 표의 크기를 안구한 경우
                            if (tableSize == 0) {
                                for (int t = 1; ; t++) {
                                    int col = lines[i + t].toCharArray()[0] - '0';
                                    System.out.println(lines[i + t]);
                                    System.out.println("col is " + col);

                                    if (0 <= col && col <= 9) {
                                        tableSize = t;
                                        System.out.println("tableSize is " + tableSize);
                                        break;
                                    }
                                }
                            }
                            // 표의 크기를 구해논 경우
                            {
                                System.out.println("@@@ table size is" + tableSize);
                                System.out.println(lines[i]);
                                System.out.println(lines[i + tableSize]);
                                // 토큰 빼내기
                                int[] counts = new int[6];
                                int c = 0;
                                System.out.println("before st.CT " + st.countTokens());
                                int size = st.countTokens();
                                for (; c < size; c++) {
                                    counts[c] = Integer.parseInt(st.nextToken());
                                }
                                System.out.println(c);
                                System.out.println("count[] is " + Arrays.toString(counts));
                                st = new StringTokenizer(lines[i + tableSize]);
                                System.out.println("st.CT " + st.countTokens());
                                for (c += 1; c < counts.length; c++) {
                                    counts[c] = Integer.parseInt(st.nextToken());
                                }
                                System.out.println(Arrays.toString(counts));

                                AttendanceRate attendanceRate = AttendanceRate.builder()
                                        .legislatorName(name)
                                        .affiliatedParty(party)
                                        .meetingDays(counts[0])
                                        .attendance(counts[1])
                                        .absence(counts[2])
                                        .leave(counts[3])
                                        .businessTrip(counts[4])
                                        .absenceReport(counts[5])
                                        .build();
                                System.out.println(attendanceRate);
                            }
                        } else {
                            System.out.println("@@" + name + "@@" + party + "@@" + m.group());
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

                            System.out.println(attendanceRate);
                        }
                    }
                }

            } catch (Exception e) {
                log.info("오류발생: " + filepath);
                e.printStackTrace();
            }
        }
    }
}
