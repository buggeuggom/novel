package com.novel.api.domain.novel;

import com.novel.api.domain.AuditingFields;
import com.novel.api.domain.genre.Genre;
import com.novel.api.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString(callSuper = true)
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

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY, optional = false)
    private User user;
    @JoinColumn(name = "genre_id")
    @ManyToOne(fetch = LAZY, optional = false)
    private Genre genre;

    @Builder
    private Novel(String title, String explanation, NovelStatus novelStatus, User user, Genre genre) {
        this.title = title;
        this.explanation = explanation;
        this.novelStatus = novelStatus;
        this.user = user;
        this.genre = genre;
    }
}
