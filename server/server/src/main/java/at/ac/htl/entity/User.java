package at.ac.htl.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "[user]")
public class User extends PanacheEntity {


    public String username;
    public String firstname;
    public String lastname;
    public String password;
    public String mail;
    public String salt;

    @OneToMany
    @JoinColumn(name = "playlists")
    public List<Playlist> playlistList = new LinkedList<>();

    public User(String username, String name, String lastname, String password, String mail, String salt) {
        this.username = username;
        this.firstname = name;
        this.lastname = lastname;
        this.password = password;
        this.mail = mail;
        this.salt = salt;
    }

    public User() {
    }
}
