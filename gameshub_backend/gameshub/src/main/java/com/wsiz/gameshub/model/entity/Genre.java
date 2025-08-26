package com.wsiz.gameshub.model.entity;

import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import jakarta.persistence.*;
import java.util.List;

@Entity(name = "GENRE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "GENRE")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_seq")
    @SequenceGenerator(name = "genre_seq", sequenceName = "genre_seq", allocationSize = 1)
    private Long id;

    @KeywordField
    @Column
    private String name;

    @Column
    private String marketplaceName;

    @Column
    private Long externalId;

    @ManyToMany(mappedBy = "genres")
    private List<Game> games;
}
