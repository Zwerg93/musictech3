package at.ac.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Playlist extends PanacheEntity {

    public String name;

    @OneToMany
    @JoinColumn(name = "songlist")
    public List<Song> songList = new LinkedList<>();

    public Playlist(String name) {
        this.name = name;
    }

    public Playlist() {
    }
}
