package com.wsiz.gameshub.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "CATEGORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1)
    private Long id;

    @Column
    private String name;

    @Column
    private String marketplaceName;

    @Column
    private Long externalId;
}
