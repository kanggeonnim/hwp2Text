package com.example.hwptotable.assembly;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssemblyRepository extends JpaRepository<SimpleAssembly, Long> {

    // 한자
    Optional<SimpleAssembly> findByHgNameAndHjName(String hgName, String hjName);

    List<SimpleAssembly> findAllByHgName(String name);

    // 한글
    Optional<SimpleAssembly> findByHgName(String hgName);

    Optional<SimpleAssembly> findByHgNameAndPolyName(String hgName, String polyName);

    List<SimpleAssembly> findAllByHgNameAndHjName(String name, String chinese);
}
