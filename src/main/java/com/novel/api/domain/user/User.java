package com.novel.api.domain.user;

import com.novel.api.domain.AuditingFields;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Getter
@ToString(callSuper = true)
@Table
@Entity
@NoArgsConstructor(access = PROTECTED)
public class User  extends AuditingFields {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    @Enumerated(STRING)
    private UserRole userRole;

    @Builder
    private User(Long id, String name, String email, String password, UserRole userRole) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }
}
