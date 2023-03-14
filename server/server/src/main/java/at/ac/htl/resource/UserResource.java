package at.ac.htl.resource;


import at.ac.htl.entity.User;
import at.ac.htl.model.UserModel;
import at.ac.htl.repository.UserRepo;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserRepo userRepo;

    @GET
    @Path("/all")
    public List<User> getAllUser() {
        return this.userRepo.findAll().stream().toList();
    }

    @GET
    @Path("/{username}")
    public User getUserByName(@PathParam("username") String username) {
        return this.userRepo.findByUsername(username);
    }

    @POST
    @Transactional
    public Response addUser(UserModel data) {

        User user = this.userRepo.findByUsernameOrEmail(data.username, data.mail);
        if (user == null) {
            String salt = BCrypt.gensalt();
            String password = BCrypt.hashpw(data.password, salt);
            user = new User(data.username, data.firstname, data.lastname, password, data.mail, salt);
            this.userRepo.persist(user);
        }
        return Response.ok().build();
    }




}
