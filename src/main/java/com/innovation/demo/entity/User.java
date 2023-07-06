package com.innovation.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.innovation.demo.entity.User.Role.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role = USER;

    @Column
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Board> boardList = new HashSet<>();

    @Column
    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();

    public void addBoard(Board board) {
        this.getBoardList().add(board);
        board.setUser(this);
    }

    public void addComment(Comment comment) {
        this.getCommentList().add(comment);
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void changeAdminRole() {
        this.role = ADMIN;
    }

    public static enum Role {
        USER, ADMIN
    }

}
