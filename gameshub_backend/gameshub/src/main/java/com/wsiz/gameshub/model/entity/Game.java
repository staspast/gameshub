package com.wsiz.gameshub.model.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "GAME")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_seq")
    @SequenceGenerator(name = "game_seq", sequenceName = "game_seq", allocationSize = 1)
    private Long id;

    @Column
    private String name;

    @Column
    private Long externalAppId;

    @Column
    private String marketplaceName;

    @Column
    private BigDecimal priceInitial;

    @Column
    private BigDecimal priceFinal;

    @Column
    private BigDecimal discountPercent;

    @Column
    private Boolean loadedDetailsFromExternalApi;

    @Column
    private LocalDateTime addedAt;

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
    @Type(type="org.hibernate.type.TextType")
    private String description;

    @Column(length = 4000)
    private String shortDescription;

    @ManyToMany
    private List<Category> categories;

    @Column
    private String developer;

    @Column
    private String publisher;
}
