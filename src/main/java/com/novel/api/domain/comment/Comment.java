package com.novel.api.domain.comment;

import com.novel.api.domain.AuditingFields;
import com.novel.api.domain.episode.Episode;
import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString(callSuper = true)
@Table
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Comment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String comment;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY, optional = false)
    private User user;
    @JoinColumn(name = "novel_id")
    @ManyToOne(fetch = LAZY, optional = false)
    private Novel novel;
    @JoinColumn(name = "episode_id")
    @ManyToOne(fetch = LAZY, optional = false)
    private Episode episode;

    @Builder
    private Comment(String comment, User user, Novel novel, Episode episode) {
        this.comment = comment;
        this.user = user;
        this.novel = novel;
        this.episode = episode;
    }
}
