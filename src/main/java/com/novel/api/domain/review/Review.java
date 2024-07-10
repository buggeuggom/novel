package com.novel.api.domain.review;

import com.novel.api.domain.AuditingFields;
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
public class Review extends AuditingFields {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY, optional = false)
    private User user;
    @JoinColumn(name = "novel_id")
    @ManyToOne(fetch = LAZY, optional = false)
    private Novel novel;

    private Integer score;
    private String body;

    @Builder
    private Review(Long id, User user, Novel novel, Integer score, String body) {
        this.id = id;
        this.user = user;
        this.novel = novel;
        this.score = score;
        this.body = body;
    }
}
