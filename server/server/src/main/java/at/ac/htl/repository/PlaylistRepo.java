package at.ac.htl.repository;

import at.ac.htl.entity.Playlist;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlaylistRepo implements PanacheRepository<Playlist> {
}
