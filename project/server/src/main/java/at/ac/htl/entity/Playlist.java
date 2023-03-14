package at.ac.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Playlist extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    public String name;

    @ManyToOne
    public UserEntity user;

    @OneToMany
    public List<SongEntity> songList = new LinkedList<>();

}
