package com.example.hwptotable.assembly.repository;

import com.example.hwptotable.assembly.entity.SpecialCommitteeAttendanceRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialCommitteeAttendanceRateRepository extends JpaRepository<SpecialCommitteeAttendanceRate, Long> {
    Optional<SpecialCommitteeAttendanceRate> findByAssemblyId(Long assemblyId);

}
