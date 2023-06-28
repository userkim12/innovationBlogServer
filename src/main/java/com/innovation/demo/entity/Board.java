package com.innovation.demo.entity;


import com.innovation.demo.audit.BaseTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Board extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;

    @Column
    private String title;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @Column
    private String content;

    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

}

