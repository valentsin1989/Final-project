package com.gmail.bsbgroup6.repository.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "empl_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "mail")
    private String mail;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusEnum status;

    @Column(name = "login_failed")
    private Integer loginFailed;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "login_date")
    private String loginDate;

    @Column(name = "logout_date")
    private String logoutDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Session> listSession;

    public void addSession(Session session) {
        listSession.add(session);
        session.setUser(this);
    }

}
