package com.novel.api.domain.novel;

import com.novel.api.domain.AuditingFields;
import com.novel.api.domain.episode.Episode;
import com.novel.api.domain.user.User;
import com.novel.api.dto.request.novel.EditNovelRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Table
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Novel extends AuditingFields {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;
    private String explanation;
    @Enumerated(STRING)
    private NovelStatus novelStatus;
    @Enumerated(STRING)
    private Genre genre;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY, optional = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "novel")
    private List<Episode> episodes;

    @Builder
    private Novel(Long id, String title, String explanation, NovelStatus novelStatus, Genre genre, User user) {
        this.id = id;
        this.title = title;
        this.explanation = explanation;
        this.novelStatus = novelStatus;
        this.genre = genre;
        this.user = user;
    }

    public void edit(EditNovelRequest request){
        explanation = request.getExplanation();
        novelStatus = request.getNovelStatus();
        genre = request.getGenre();
    }
}
