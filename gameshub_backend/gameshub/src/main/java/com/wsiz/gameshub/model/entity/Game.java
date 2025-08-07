package com.wsiz.gameshub.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "GAME")
@Table(indexes = {
        @Index(name = "game_name_idx", columnList = "name"),
        @Index(name = "game_loaded_idx", columnList = "marketplaceName, loadedDetailsFromExternalApi")
})
@Indexed
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_seq")
    @SequenceGenerator(name = "game_seq", sequenceName = "game_seq", allocationSize = 1)
    private Long id;

    @KeywordField(name = "nameSort", sortable = Sortable.YES)
    @FullTextField(name = "name")
    @Column(length = 2000)
    private String name;

    @KeywordField
    @Column
    private String externalAppId;

    @KeywordField
    @Column
    private String marketplaceName;

    @Column
    private BigDecimal priceInitial;

    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
    @Column
    private BigDecimal priceFinal;

    @Column
    private BigDecimal discountPercent;

    @Column
    private Boolean loadedDetailsFromExternalApi;

    @JsonIgnore
    @Column
    private LocalDateTime addedAt;

    @JsonIgnore
    @Column
    private LocalDateTime updatedAt;

    @Column
    private String currency;

    @Column
    private Boolean isReleased;

    @Column
    private Boolean isGame;

    @Column(length = 4000)
    private String mainImageUrl;

    @OneToMany(cascade = CascadeType.ALL)
    private List<GameImage> gameImages;

    @Column
    @Lob
    private String description;

    @Column(length = 4000)
    private String shortDescription;

    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @IndexedEmbedded
    @ManyToMany
    private List<Category> categories;

    @FullTextField
    @Column(length = 2000)
    private String developer;

    @FullTextField
    @Column(length = 2000)
    private String publisher;

    @Column(length = 2000)
    private String slug;

    @Column(length = 4000)
    private String redirectUrl;
}
