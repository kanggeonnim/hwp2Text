package com.example.hwptotable.parse;

import com.example.hwptotable.assembly.entity.DetailedPledgeExecutionStatus;
import com.example.hwptotable.assembly.entity.PledgeFulfillmentRate;
import com.example.hwptotable.assembly.repository.PledgeFulfillmentRateRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback(false)
class ParseManifestoTest {

    @Autowired
    ParseManifesto parseManifesto;

    @Autowired
    PledgeFulfillmentRateRepository pledgeFulfillmentRateRepository;

    @Test
    public void Test() throws Exception {
        parseManifesto.parseAll();
    }


    @Test
    public void getRate() {
        Optional<PledgeFulfillmentRate> find = pledgeFulfillmentRateRepository.findById(1L);
        List<DetailedPledgeExecutionStatus> list = find.get().getPledges();

        for (DetailedPledgeExecutionStatus pledge : list) {
            System.out.println(pledge);
        }
    }
}