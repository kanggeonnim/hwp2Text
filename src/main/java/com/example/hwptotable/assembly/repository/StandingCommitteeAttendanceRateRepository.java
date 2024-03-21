package com.example.hwptotable.assembly.repository;

import com.example.hwptotable.assembly.entity.StandingCommitteeAttendanceRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StandingCommitteeAttendanceRateRepository extends JpaRepository<StandingCommitteeAttendanceRate, Long> {
    Optional<StandingCommitteeAttendanceRate> findByAssemblyId(Long assemblyId);
}
