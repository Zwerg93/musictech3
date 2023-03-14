package at.ac.htl.repo;

import at.ac.htl.entity.SongEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

public class SongRepo implements PanacheRepositoryBase<SongEntity, Long> {


}
