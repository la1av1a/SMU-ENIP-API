package com.smu.smuenip.domain.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity(name = "nick_name_example")
@NoArgsConstructor
public class NickNameExample {

    @Id
    private Long id;

    @Column
    private String nick;

    @Column
    private int depth;

    public NickNameExample(Long id, String nick, int depth) {
        this.id = id;
        this.nick = nick;
        this.depth = depth;
    }
}
