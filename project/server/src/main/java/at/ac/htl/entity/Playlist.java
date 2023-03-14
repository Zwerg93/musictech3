package at.ac.htl.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Playlist extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String name;

    @ManyToOne
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "username")
    @JsonIdentityReference(alwaysAsId = true)
    public UserEntity user;

    @OneToMany
    public List<SongEntity> songList = new LinkedList<>();

    public Playlist(String name, UserEntity user) {
        this.name = name;
        this.user = user;
    }

    public Playlist() {
    }
}
