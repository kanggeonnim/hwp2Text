package com.example.hwptotable.assembly;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Getter
@Immutable
@NoArgsConstructor
@AllArgsConstructor
@Transactional
@Table(name = "simple_assembly")
public class SimpleAssembly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assembly_id")
    private Long id;

    private String hgName;
    private String hjName;
    private String polyName;


    @Builder
    public SimpleAssembly(String hgName, String hjName, String polyName) {
        this.hgName = hgName;
        this.hjName = hjName;
        this.polyName = polyName;
    }
}
