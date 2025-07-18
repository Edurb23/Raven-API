package com.portifolio.Raven.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_RAVEN_ARTIST_IMAGE")
public class ArtistImage {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Lob
    @Column(name = "url", nullable = false )
    private String urlImage;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    @JsonIgnore
    private Artist artist;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant created_at;

    @Column(name = "update_at")
    private Instant update_at;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        created_at = now;
        update_at = now;
    }

    @PreUpdate
    public void preUpdate() {
        update_at = Instant.now();
    }


}
