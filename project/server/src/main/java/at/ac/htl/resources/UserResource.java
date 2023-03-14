package at.ac.htl.resources;

import at.ac.htl.entity.UserEntity;
import at.ac.htl.model.UserModel;
import at.ac.htl.repo.UserRepo;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {


    @Inject
    UserRepo userRepo;

    @GET
    @Path("/all")
    public List<UserEntity> getAllUser() {
        return this.userRepo.findAll().stream().toList();
    }

    @GET
    @Path("/{username}")
    public UserEntity getUserByName(@PathParam("username") String username) {
        return this.userRepo.findByUsername(username);
    }

    @POST
    @Transactional
    public Response addUser(UserModel data) {

        UserEntity user = this.userRepo.findByUsernameOrEmail(data.username, data.mail);
        if (user == null) {
            String salt = BCrypt.gensalt();
            String password = BCrypt.hashpw(data.password, salt);
            user = new UserEntity(data.username, data.firstname, data.lastname, password, data.mail, salt);
            this.userRepo.persist(user);
        }
        return Response.ok().build();
    }
}