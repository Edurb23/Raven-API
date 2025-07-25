package com.portifolio.Raven.entity;


import com.portifolio.Raven.dto.artistDto.RegisterArtistDto;
import com.portifolio.Raven.dto.artistDto.UpdateArtistDto;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "TB_RAVEN_ARTISTS")
public class Artist {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String nomeArtist;


    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "tb_raven_genero_artist",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    private Set<Genero> generos;

    @Lob
    @Column(name= "bio", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String biografia;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<ArtistImage>artistImages;

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


    public void updateArtist(UpdateArtistDto dto) {
        if(dto.nomeArtist() != null)
            nomeArtist = dto.nomeArtist();
        if(dto.biografia() != null)
            biografia = dto.biografia();

    }
}
