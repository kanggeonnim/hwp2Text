package com.example.hwptotable.assembly;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssemblyRepository extends JpaRepository<SimpleAssembly, Long> {

    // 한자
    Optional<SimpleAssembly> findByHjName(String hjName);

    // 한글
    Optional<SimpleAssembly> findByHgName(String hgName);

    Optional<SimpleAssembly> findByHgNameAndPolyName(String hgName, String polyName);

}
