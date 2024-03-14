package com.example.hwptotable.assembly.repository;

import com.example.hwptotable.assembly.entity.AttendanceRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRateRepository extends JpaRepository<AttendanceRate, Long> {
    Optional<AttendanceRate> findByLegislatorNameAndAffiliatedParty(String name, String party);
}
