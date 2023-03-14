package at.ac.htl.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
public class SongEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String title;
    @Column(nullable = false)
    public String streamUrl;
    public String Artist;

    public long streams;

    public SongEntity(String title, String streamUrl, String artist) {
        this.title = title;
        this.streamUrl = streamUrl;
        this.Artist = artist;
    }

    public SongEntity() {
    }
}
