package at.ac.htl.repository;

import at.ac.htl.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;

@ApplicationScoped
public class UserRepo implements PanacheRepository<User> {
    public User findByUsername(String username) {
        TypedQuery<User> query = getEntityManager().createQuery("" + "select u from User u where lower(u.username)  = :username", User.class);
        query.setParameter("username", username.toLowerCase());
        return query.getResultStream().findFirst().orElse(null);
    }

    public User findByUsernameOrEmail(String username, String mail) {
        TypedQuery<User> query = getEntityManager().createQuery("" + "select u from User u where lower(u.username) like :username or lower(u.mail) like: mail ", User.class);
        query.setParameter("username", "%" + username.toLowerCase() + "%");
        query.setParameter("mail", "%" + mail.toLowerCase() + "%");

        return query.getResultStream().findFirst().orElse(null);
    }
}