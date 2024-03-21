package com.example.hwptotable.parse;

import com.example.hwptotable.assembly.AssemblyRepository;
import com.example.hwptotable.assembly.SimpleAssembly;
import com.example.hwptotable.assembly.entity.AttendanceRate;
import com.example.hwptotable.assembly.entity.SpecialCommitteeAttendanceRate;
import com.example.hwptotable.assembly.entity.StandingCommitteeAttendanceRate;
import com.example.hwptotable.assembly.repository.AttendanceRateRepository;
import com.example.hwptotable.assembly.repository.SpecialCommitteeAttendanceRateRepository;
import com.example.hwptotable.assembly.repository.StandingCommitteeAttendanceRateRepository;
import com.example.hwptotable.hwp.HwpToText2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
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

    private final AssemblyRepository assemblyRepository;

    private final AttendanceRateRepository attendanceRateRepository;

    private final StandingCommitteeAttendanceRateRepository standingCommitteeAttendanceRateRepository;

    private final SpecialCommitteeAttendanceRateRepository specialCommitteeAttendanceRateRepository;
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
                // 국회의원 아이디 가져오기
                Long assemblyId = getAssemblyId(name, party);
                if (assemblyId == null) {
                    log.info("can't find: " + name + " " + party);
                    continue;
                }

                StringTokenizer st = new StringTokenizer(m.group());
                // 기존값 가져오기
                Optional<AttendanceRate> findRate = attendanceRateRepository.findByAssemblyId(assemblyId);

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
                            .assemblyId(assemblyId)
                            .meetingDays(Integer.parseInt(st.nextToken()))
                            .attendance(Integer.parseInt(st.nextToken()))
                            .absence(Integer.parseInt(st.nextToken()))
                            .leaves(Integer.parseInt(st.nextToken()))
                            .businessTrip(Integer.parseInt(st.nextToken()))
                            .absenceReport(Integer.parseInt(st.nextToken()))
                            .build();
                    attendanceRateRepository.save(attendanceRate);
                }
            }
        }
    }


    public void parseStandingAtd() {
        DATA_DIRECTORY = "C:/Users/SSAFY/Downloads/21대 국회의원 상임위 출결현황/";   // window
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

                    // 한자명 빼기
                    int chIdx = name.indexOf("(");
                    String chinese = null;
                    if (chIdx != -1) {
                        chinese = name.substring(chIdx + 1, name.length() - 1);
                        name = name.substring(0, chIdx);
                    }

                    Long assemblyId;

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

                                    if (0 <= col && col <= 9) {
                                        tableSize = t;
                                        break;
                                    }
                                }
                            }
                            // 표의 크기를 구해논 경우
                            {
                                // 토큰 빼내기
                                int[] counts = new int[6];
                                int c = 0;
                                for (; c < st.countTokens(); c++) {
                                    counts[c] = Integer.parseInt(st.nextToken());
                                }
                                st = new StringTokenizer(lines[i + tableSize]);
                                int size = st.countTokens();
                                for (c += 1; c < size; c++) {
                                    counts[c] = Integer.parseInt(st.nextToken());
                                }
                                assemblyId = getAssemblyHJ(chinese, name);
                                if (assemblyId == null) continue;
                                // 기존값 가져오기
                                Optional<StandingCommitteeAttendanceRate> findRate = standingCommitteeAttendanceRateRepository.findByAssemblyId(assemblyId);

                                if (findRate.isPresent()) {
                                    findRate.get().addDays(
                                            Integer.parseInt(st.nextToken()),
                                            Integer.parseInt(st.nextToken()),
                                            Integer.parseInt(st.nextToken()),
                                            Integer.parseInt(st.nextToken()),
                                            Integer.parseInt(st.nextToken()),
                                            Integer.parseInt(st.nextToken()));
                                } else {
                                    StandingCommitteeAttendanceRate attendanceRate = StandingCommitteeAttendanceRate.builder()
                                            .assemblyId(assemblyId)
                                            .meetingDays(counts[0])
                                            .attendance(counts[1])
                                            .absence(counts[2])
                                            .leaves(counts[3])
                                            .businessTrip(counts[4])
                                            .absenceReport(counts[5])
                                            .build();
                                    standingCommitteeAttendanceRateRepository.save(attendanceRate);
                                }
                            }
                        } else {
                            assemblyId = getAssemblyHJ(chinese, name);
                            if (assemblyId == null) continue;
                            // 기존값 가져오기
                            Optional<StandingCommitteeAttendanceRate> findRate = standingCommitteeAttendanceRateRepository.findByAssemblyId(assemblyId);

                            if (findRate.isPresent()) {
                                findRate.get().addDays(
                                        Integer.parseInt(st.nextToken()),
                                        Integer.parseInt(st.nextToken()),
                                        Integer.parseInt(st.nextToken()),
                                        Integer.parseInt(st.nextToken()),
                                        Integer.parseInt(st.nextToken()),
                                        Integer.parseInt(st.nextToken()));
                            } else {
                                StandingCommitteeAttendanceRate attendanceRate = StandingCommitteeAttendanceRate.builder()
                                        .assemblyId(assemblyId)
                                        .meetingDays(Integer.parseInt(st.nextToken()))
                                        .attendance(Integer.parseInt(st.nextToken()))
                                        .absence(Integer.parseInt(st.nextToken()))
                                        .leaves(Integer.parseInt(st.nextToken()))
                                        .businessTrip(Integer.parseInt(st.nextToken()))
                                        .absenceReport(Integer.parseInt(st.nextToken()))
                                        .build();
                                standingCommitteeAttendanceRateRepository.save(attendanceRate);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                log.info("오류발생: " + filepath);
                e.printStackTrace();
            }
        }
    }

    public void parseSpecialAtd() {
        DATA_DIRECTORY = "C:/Users/SSAFY/Downloads/21대 국회의원 특위 출결현황/";   // window
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

                    // 한자명 빼기
                    int chIdx = name.indexOf("(");
                    String chinese = null;
                    if (chIdx != -1) {
                        chinese = name.substring(chIdx + 1, name.length() - 1);
                        name = name.substring(0, chIdx);
                    }
                    // 정당
                    int partyIdx = lines[i].substring(nameIdx + 1).indexOf(" ") + nameIdx + 1;
                    if (partyIdx - nameIdx <= 0) continue;

                    String party = lines[i].substring(nameIdx + 1, partyIdx);
                    if (party.startsWith("회의일수.*") || party.matches("^\\([0-9]+.*") || party.matches("[0-9]+.*") || party.matches("제[0-9]+.*")) {
                        tableSize = 0;  // 표가 끝난 경우, 기존의 표 크기 초기화
                        continue;
                    }

                    // 국회의원 아이디 찾기
                    Long assemblyId;

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

                                    if (0 <= col && col <= 9) {
                                        tableSize = t;
                                        break;
                                    }
                                }
                            }
                            // 표의 크기를 구해논 경우
                            {
                                // 토큰 빼내기
                                int[] counts = new int[6];
                                int c = 0;
                                for (; c < st.countTokens(); c++) {
                                    counts[c] = Integer.parseInt(st.nextToken());
                                }
                                st = new StringTokenizer(lines[i + tableSize]);
                                int size = st.countTokens();
                                for (c += 1; c < size; c++) {
                                    counts[c] = Integer.parseInt(st.nextToken());
                                }
                                assemblyId = getAssemblyHJ(chinese, name);
                                if (assemblyId == null) continue;
                                Optional<SpecialCommitteeAttendanceRate> findRate = specialCommitteeAttendanceRateRepository.findByAssemblyId(assemblyId);

                                if (findRate.isPresent()) {
                                    findRate.get().addDays(
                                            Integer.parseInt(st.nextToken()),
                                            Integer.parseInt(st.nextToken()),
                                            Integer.parseInt(st.nextToken()),
                                            Integer.parseInt(st.nextToken()),
                                            Integer.parseInt(st.nextToken()),
                                            Integer.parseInt(st.nextToken()));
                                } else {
                                    SpecialCommitteeAttendanceRate attendanceRate = SpecialCommitteeAttendanceRate.builder()
                                            .assemblyId(assemblyId)
                                            .meetingDays(counts[0])
                                            .attendance(counts[1])
                                            .absence(counts[2])
                                            .leaves(counts[3])
                                            .businessTrip(counts[4])
                                            .absenceReport(counts[5])
                                            .build();
                                    specialCommitteeAttendanceRateRepository.save(attendanceRate);
                                }
                            }
                        } else {
                            assemblyId = getAssemblyHJ(chinese, name);
                            if (assemblyId == null) continue;
                            Optional<SpecialCommitteeAttendanceRate> findRate = specialCommitteeAttendanceRateRepository.findByAssemblyId(assemblyId);

                            if (findRate.isPresent()) {
                                findRate.get().addDays(
                                        Integer.parseInt(st.nextToken()),
                                        Integer.parseInt(st.nextToken()),
                                        Integer.parseInt(st.nextToken()),
                                        Integer.parseInt(st.nextToken()),
                                        Integer.parseInt(st.nextToken()),
                                        Integer.parseInt(st.nextToken()));
                            } else {

                                SpecialCommitteeAttendanceRate attendanceRate = SpecialCommitteeAttendanceRate.builder()
                                        .assemblyId(assemblyId)
                                        .meetingDays(Integer.parseInt(st.nextToken()))
                                        .attendance(Integer.parseInt(st.nextToken()))
                                        .absence(Integer.parseInt(st.nextToken()))
                                        .leaves(Integer.parseInt(st.nextToken()))
                                        .businessTrip(Integer.parseInt(st.nextToken()))
                                        .absenceReport(Integer.parseInt(st.nextToken()))
                                        .build();
                                specialCommitteeAttendanceRateRepository.save(attendanceRate);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                log.info("오류발생: " + filepath);
                e.printStackTrace();
            }
        }
    }

    private Long getAssemblyHJ(String chinese, String name) {
        // 정당과 이름 함께 검색

        // 정당 변경시
        List<SimpleAssembly> findAssembly = assemblyRepository.findAllByHgNameAndHjName(name, chinese);
        if (findAssembly.isEmpty())
            findAssembly = assemblyRepository.findAllByHgName(name);

        if (findAssembly.isEmpty()) {
            return null;
        } else if (findAssembly.size() == 2) {
            throw new RuntimeException("DUPLICATE ERROR: " + name + " " + chinese);
        } else {
            return findAssembly.get(0).getId();
        }

    }

    private Long getAssemblyId(String name, String party) {
        // 정당과 이름 함께 검색
        Optional<SimpleAssembly> findAssembly = assemblyRepository.findByHgNameAndPolyName(name, party);

        // 정당 변경시
        if (findAssembly.isEmpty())
            findAssembly = assemblyRepository.findByHgName(name);

        return findAssembly.map(SimpleAssembly::getId).orElse(null);
    }
}
