package at.ac.htl.resources;

import at.ac.htl.entity.Playlist;
import at.ac.htl.entity.UserEntity;
import at.ac.htl.model.UserModel;
import at.ac.htl.repo.PlaylistRepo;
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

    @Inject
    PlaylistRepo playlistRepo;

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
        } else {
            return Response.status(400).build();
        }
        return Response.ok().build();
    }

    @PUT
    @Path("/addPlaylist/{username}/{playlistID}")
    public Response addPlaylistToUser(@PathParam("username") String username, @PathParam("playlistID") long playlistid) {
        UserEntity user = this.userRepo.findByUsername(username);
        if (user == null) {
            return Response.status(404).build();
        }
        Playlist playlist = this.playlistRepo.findById(playlistid);
        if (playlist == null) {
            return Response.status(404).build();

        }

        user.playlists.add(playlist);
        return Response.ok().build();
    }


}