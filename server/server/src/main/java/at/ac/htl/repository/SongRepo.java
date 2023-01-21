package at.ac.htl.repository;

import at.ac.htl.entity.Song;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;

@ApplicationScoped
public class SongRepo implements PanacheRepository<Song> {
    public Song checkName(String name) {
        TypedQuery<Song> query = getEntityManager().createQuery("" + "select s from Song s where lower(s.name)  = :name", Song.class);
        query.setParameter("name", name.toLowerCase());
        return query.getResultStream().findFirst().orElse(null);
    }
}
