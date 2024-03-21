package com.example.hwptotable.parse;

import com.example.hwptotable.assembly.AssemblyRepository;
import com.example.hwptotable.assembly.SimpleAssembly;
import com.example.hwptotable.assembly.entity.AttendanceRate;
import com.example.hwptotable.assembly.entity.SpecialCommitteeAttendanceRate;
import com.example.hwptotable.assembly.entity.StandingCommitteeAttendanceRate;
import com.example.hwptotable.assembly.repository.AttendanceRateRepository;
import com.example.hwptotable.assembly.repository.SpecialCommitteeAttendanceRateRepository;
import com.example.hwptotable.assembly.repository.StandingCommitteeAttendanceRateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class ParseAttendance2 {

    private final AssemblyRepository assemblyRepository;
    private final AttendanceRateRepository attendanceRateRepository;
    private final StandingCommitteeAttendanceRateRepository standingCommitteeAttendanceRateRepository;
    private final SpecialCommitteeAttendanceRateRepository specialCommitteeAttendanceRateRepository;

    private static final String MAIN_ATTENDANCE_DIRECTORY = "C:/Users/SSAFY/Downloads/21대 국회의원 본회의 출결현황/";
    private static final String STANDING_ATTENDANCE_DIRECTORY = "C:/Users/SSAFY/Downloads/21대 국회의원 상임위 출결현황/";
    private static final String SPECIAL_ATTENDANCE_DIRECTORY = "C:/Users/SSAFY/Downloads/21대 국회의원 특위 출결현황/";

    public void parseAll() {
        parseAttendanceDirectory(MAIN_ATTENDANCE_DIRECTORY, this::parseMainAttendance);
        parseAttendanceDirectory(STANDING_ATTENDANCE_DIRECTORY, this::parseStandingAttendance);
        parseAttendanceDirectory(SPECIAL_ATTENDANCE_DIRECTORY, this::parseSpecialAttendance);
    }

    private void parseAttendanceDirectory(String directory, Consumer<String[]> parseFunction) {
        File dir = new File(directory);
        for (String filepath : Objects.requireNonNull(dir.list())) {
            if (filepath.endsWith("pdf")) {
                try {
                    File file = new File(directory + filepath);
                    PDDocument document = Loader.loadPDF(file);
                    PDFTextStripper pdfStripper = new PDFTextStripper();
                    String pages = pdfStripper.getText(document);
                    String[] lines = pages.split("\r\n|\r|\n");
                    parseFunction.accept(lines);
                } catch (IOException e) {
                    log.error("PDF 파일을 읽을 수 없습니다: {}", filepath, e);
                }
            }
        }
    }

    private void parseMainAttendance(String[] lines) {
        Pattern p = Pattern.compile("[0-9]+[0-9\\s]+");

        for (String line : lines) {
            int nameIdx = line.indexOf(" ");
            if (nameIdx <= 0) continue;

            String name = line.substring(0, nameIdx);
            String party = line.substring(nameIdx + 1).split("\\s+")[0]; // 정당 추출

            // 정당이 특정 문자열로 시작하면 해당 줄은 건너뜁니다.
            if (party.startsWith("2차") || party.startsWith("(202") || party.startsWith("202")) continue;

            Matcher m = p.matcher(line);
            if (m.find()) {
                Long assemblyId = getAssemblyId(name, party);
                if (assemblyId == null) {
                    log.info("can't find: " + name + " " + party);
                    continue;
                }

                StringTokenizer st = new StringTokenizer(m.group());
                int[] counts = new int[6];
                for (int i = 0; i < counts.length; i++) {
                    counts[i] = Integer.parseInt(st.nextToken());
                }

                Optional<AttendanceRate> findRate = attendanceRateRepository.findByAssemblyId(assemblyId);
                if (findRate.isPresent()) {
                    findRate.get().addDays(counts[0], counts[1], counts[2], counts[3], counts[4], counts[5]);
                } else {
                    AttendanceRate attendanceRate = AttendanceRate.builder()
                            .assemblyId(assemblyId)
                            .meetingDays(counts[0])
                            .attendance(counts[1])
                            .absence(counts[2])
                            .leaves(counts[3])
                            .businessTrip(counts[4])
                            .absenceReport(counts[5])
                            .build();
                    attendanceRateRepository.save(attendanceRate);
                }
            }
        }
    }


    private void parseStandingAttendance(String[] lines) {
        int tableSize = 0;

        for (int i = 0; i < lines.length; i++) {
            String name = extractName(lines[i]);
            String party = extractParty(lines[i]);
            if (party.startsWith("회의일수.*") || party.matches("^\\([0-9]+.*") || party.matches("[0-9]+.*") || party.matches("제[0-9]+.*")) {
                tableSize = 0;
                continue;
            }

            String chinese = extractChineseName(name);
            Long assemblyId = getAssemblyHJ(chinese, name);

            if (assemblyId != null) {
                Optional<StandingCommitteeAttendanceRate> findRate = standingCommitteeAttendanceRateRepository.findByAssemblyId(assemblyId);
                if (findRate.isPresent()) {
                    int[] counts = extractAttendanceCounts(lines[i], tableSize);
                    findRate.get().addDays(counts[0], counts[1], counts[2], counts[3], counts[4], counts[5]);
                } else {
                    StandingCommitteeAttendanceRate attendanceRate = createSCAttendanceRate(lines[i], tableSize, assemblyId);
                    standingCommitteeAttendanceRateRepository.save(attendanceRate);
                }
            }
        }
    }

    private void parseSpecialAttendance(String[] lines) {
        int tableSize = 0;

        for (int i = 0; i < lines.length; i++) {
            String name = extractName(lines[i]);
            String party = extractParty(lines[i]);
            if (party.startsWith("회의일수.*") || party.matches("^\\([0-9]+.*") || party.matches("[0-9]+.*") || party.matches("제[0-9]+.*")) {
                tableSize = 0;
                continue;
            }

            String chinese = extractChineseName(name);
            Long assemblyId = getAssemblyHJ(chinese, name);

            if (assemblyId != null) {
                Optional<SpecialCommitteeAttendanceRate> findRate = specialCommitteeAttendanceRateRepository.findByAssemblyId(assemblyId);
                if (findRate.isPresent()) {
                    int[] counts = extractAttendanceCounts(lines[i], tableSize);
                    findRate.get().addDays(counts[0], counts[1], counts[2], counts[3], counts[4], counts[5]);
                } else {
                    SpecialCommitteeAttendanceRate attendanceRate = createSPAttendanceRate(lines[i], tableSize, assemblyId);
                    specialCommitteeAttendanceRateRepository.save(attendanceRate);
                }
            }
        }
    }

    private String extractTextFromPDF(File file) throws IOException {
        PDDocument document = Loader.loadPDF(file);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        return pdfStripper.getText(document);
    }

    private String extractName(String line) {
        int nameIdx = line.indexOf(" ");
        return nameIdx > 0 ? line.substring(0, nameIdx) : "";
    }

    private String extractParty(String line) {
        int nameIdx = line.indexOf(" ");
        if (nameIdx > 0) {
            int partyIdx = line.substring(nameIdx + 1).indexOf(" ") + nameIdx + 1;
            return partyIdx - nameIdx > 0 ? line.substring(nameIdx + 1, partyIdx) : "";
        }
        return "";
    }

    private String extractChineseName(String name) {
        int chIdx = name.indexOf("(");
        return chIdx != -1 ? name.substring(chIdx + 1, name.length() - 1) : null;
    }

    private int[] extractAttendanceCounts(String line, int tableSize) {
        StringTokenizer st = new StringTokenizer(line);
        if (1 <= st.countTokens() && st.countTokens() <= 5) {
            if (tableSize == 0) {
                for (int t = 1; ; t++) {
                    int col = line.charAt(t) - '0';
                    if (0 <= col && col <= 9) {
                        tableSize = t;
                        break;
                    }
                }
            }
            int[] counts = new int[6];
            int c = 0;
            for (; c < counts.length; c++) {
                counts[c] = Integer.parseInt(st.nextToken());
            }
            st = new StringTokenizer(line + tableSize);
            int size = st.countTokens();
            for (c += 1; c < size; c++) {
                counts[c] = Integer.parseInt(st.nextToken());
            }
            return counts;
        } else {
            int[] counts = new int[6];
            for (int c = 0; c < counts.length; c++) {
                counts[c] = Integer.parseInt(st.nextToken());
            }
            return counts;
        }
    }

    private <T> T createAttendanceRate(String line, int tableSize, Long assemblyId, Class<T> clazz) {
        StringTokenizer st = new StringTokenizer(line);
        int[] counts = extractAttendanceCounts(line, tableSize);
        try {
            return clazz.getConstructor(Long.class, int.class, int.class, int.class, int.class, int.class, int.class)
                    .newInstance(assemblyId, counts[0], counts[1], counts[2], counts[3], counts[4], counts[5]);
        } catch (Exception e) {
            log.error("Failed to create attendance rate object", e);
            return null;
        }
    }

    private StandingCommitteeAttendanceRate createSCAttendanceRate(String line, int tableSize, Long assemblyId) {
        return createAttendanceRate(line, tableSize, assemblyId, StandingCommitteeAttendanceRate.class);
    }

    private SpecialCommitteeAttendanceRate createSPAttendanceRate(String line, int tableSize, Long assemblyId) {
        return createAttendanceRate(line, tableSize, assemblyId, SpecialCommitteeAttendanceRate.class);
    }

    private Long getAssemblyHJ(String chinese, String name) {
        // 정당과 이름 함께 검색
        List<SimpleAssembly> findAssemblyList = assemblyRepository.findAllByHgNameAndHjName(name, chinese);

        if (findAssemblyList.isEmpty()) {
            findAssemblyList = assemblyRepository.findAllByHgName(name);
        }

        if (findAssemblyList.isEmpty()) {
            return null;
        } else if (findAssemblyList.size() == 1) {
            return findAssemblyList.get(0).getId();
        } else {
            throw new RuntimeException("DUPLICATE ERROR: " + name + " " + chinese);
        }
    }

    private Long getAssemblyId(String name, String party) {
        Optional<SimpleAssembly> findAssembly = assemblyRepository.findByHgNameAndPolyName(name, party);

        if (findAssembly.isPresent()) {
            return findAssembly.get().getId();
        } else {
            findAssembly = assemblyRepository.findByHgName(name);
            return findAssembly.map(SimpleAssembly::getId).orElse(null);
        }
    }

}
