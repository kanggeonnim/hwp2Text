package com.example.hwptotable.parse;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Transactional
@Rollback(false)
class ParseAttendance2Test {
    @Autowired
    ParseAttendance parseAttendance;

    @Autowired
    ParseAttendance2 parseAttendance2;

    @Autowired
    ParseManifesto parseManifesto;

    @Test
    public void Test() throws Exception {
        parseManifesto.parseAll();
        parseAttendance.parseAll();
        parseAttendance.parseStandingAtd();
        parseAttendance.parseSpecialAtd();
    }

    @Test
    public void Test2() {
        parseAttendance2.parseAll();
    }
}