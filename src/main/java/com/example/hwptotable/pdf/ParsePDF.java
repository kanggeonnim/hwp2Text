package com.example.hwptotable.pdf;

import com.example.hwptotable.hwp.HwpToText2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class ParsePDF {
    private static String DATA_DIRECTORY = "C:/Users/SSAFY/Downloads/21대 국회의원 본회의 출결현황/";   // window
    //    private static String DATA_DIRECTORY = "/Users/kanggeon/Downloads/01.서울지역/";  // mac

    public void parseAll() throws Exception {

        File dir = new File(DATA_DIRECTORY);
        for (String filepath : dir.list()) {
            HwpToText2 h2 = new HwpToText2();

            if (!filepath.substring(filepath.length() - 3).equals("pdf")) {
                continue;
            }
            try {
                File file = new File(DATA_DIRECTORY + filepath);
                PDDocument document = Loader.loadPDF(file);


                PDFTextStripper pdfStripper = new PDFTextStripper();
                String pages = pdfStripper.getText(document);

                String[] lines = pages.split("\r\n|\r|\n");

                for (int i = 0; i < lines.length; i++) {
                    int idx = lines[i].indexOf(" ");
                    String name = lines[i].substring(0, idx);
                    Pattern p = Pattern.compile("[0-9]+[0-9\\s]+");
                    Matcher m = p.matcher(lines[i]);
                    if (m.find()) {
                        System.out.println(name + " " + m.group());
                    } else {
                        System.out.println(lines[i]);
                    }
                }

                int count = 1;   //Just to indicate line number
                for (String temp : lines) {
//                    System.out.println(count + " " + temp);
                    count++;
                }

            } catch (Exception e) {
                log.info("오류발생: " + filepath);
                e.printStackTrace();
            }
            break;
        }
    }

    public static void main(String[] args) throws Exception {
        ParsePDF pdf = new ParsePDF();
        pdf.parseAll();
    }
}