package com.example.hwptotable.parse;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class ParseAttendanceTest {
    @Autowired
    ParseAttendance parseAttendance;

    @Test
    public void Test() throws Exception {
        parseAttendance.parseAll();
    }
}