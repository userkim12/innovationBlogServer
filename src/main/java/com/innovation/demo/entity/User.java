package com.innovation.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column
    private String password;

    @Column
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Board> boardList = new HashSet<>();

    public void addBoard(Board board) {
        this.getBoardList().add(board);
        board.setUser(this);
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
