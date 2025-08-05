package com.portifolio.Raven.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_RAVEN_GENERO")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Genero {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name = "nome",nullable = false)
    private String nome;

    @ManyToMany(mappedBy = "generos")
    @ToString.Exclude
    @JsonIgnore
    private Set<Artist> artists;



}
