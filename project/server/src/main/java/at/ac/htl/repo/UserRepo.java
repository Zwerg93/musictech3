package at.ac.htl.repo;

import at.ac.htl.entity.UserEntity;
import at.ac.htl.model.PasswordSaltModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;

import static io.quarkus.hibernate.orm.panache.Panache.getEntityManager;

@ApplicationScoped
public class UserRepo implements PanacheRepository<UserEntity> {
    public UserEntity findByUsername(String username) {
        TypedQuery<UserEntity> query = getEntityManager().createQuery("" +
                "" + "select u from UserEntity u where lower(u.username)  = :username", UserEntity.class);
        query.setParameter("username", username.toLowerCase());
        return query.getResultStream().findFirst().orElse(null);
    }

    public UserEntity findByUsernameOrEmail(String username, String mail) {
        TypedQuery<UserEntity> query = getEntityManager().createQuery("" +
                "" + "select u from UserEntity u where lower(u.username) like :username or lower(u.mail) like: mail ", UserEntity.class);
        query.setParameter("username", "%" + username.toLowerCase() + "%");
        query.setParameter("mail", "%" + mail.toLowerCase() + "%");

        return query.getResultStream().findFirst().orElse(null);
    }


    public PasswordSaltModel getPaswordByUsername(String username) {
        TypedQuery<PasswordSaltModel> query = getEntityManager().createQuery("" +
                "" + "select new at.ac.htl.model.PasswordSaltModel(u.password, u.salt) from UserEntity u where lower(u.username) like :username", PasswordSaltModel.class);
        query.setParameter("username", username.toLowerCase());
        return query.getSingleResult();
    }
}
