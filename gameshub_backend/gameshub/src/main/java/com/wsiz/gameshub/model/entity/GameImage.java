package com.wsiz.gameshub.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity(name = "GAME_IMAGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "game_image_idx", columnList = "game_id")
})
public class GameImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_image_seq")
    @SequenceGenerator(name = "game_image_seq", sequenceName = "game_image_seq", allocationSize = 1)
    private Long id;

    @JsonIgnore
    @OneToOne
    private Game game;

    @Column(length = 4000)
    private String imageUrl;

    @Column(length = 4000)
    private String imageThumbnailUrl;
}
