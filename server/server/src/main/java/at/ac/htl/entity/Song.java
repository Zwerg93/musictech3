package at.ac.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Song extends PanacheEntity {

    public String name;
    public String url;
    public String artist;

    public Song(String name, String url, String artist) {
        this.name = name;
        this.url = url;
        this.artist = artist;
    }

    public Song() {
    }
}
