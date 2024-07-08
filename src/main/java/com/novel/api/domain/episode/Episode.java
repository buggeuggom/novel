package com.novel.api.domain.episode;

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
public class Episode extends AuditingFields {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;
    private String detail;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "novel_id")
    private Novel novel;

    @Builder
    private Episode(Long id, String title, String detail, Novel novel) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.novel = novel;
    }
}
