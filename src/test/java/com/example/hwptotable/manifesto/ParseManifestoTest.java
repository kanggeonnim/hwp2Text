package com.example.hwptotable.manifesto;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class ParseManifestoTest {

    @Autowired
    ParseManifesto parseManifesto;

    @Test
    public void Test() throws Exception {
        parseManifesto.parseAll();
    }
}