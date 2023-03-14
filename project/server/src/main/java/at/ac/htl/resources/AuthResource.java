package at.ac.htl.resources;


import at.ac.htl.entity.UserEntity;
import at.ac.htl.model.CredentialsDTO;
import at.ac.htl.model.PasswordSaltModel;
import at.ac.htl.repo.UserRepo;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import javax.json.Json;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.Set;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    @Inject
    @ConfigProperty(name = "smallrye.jwt.new-token.lifespan")
    long lifespan;
    @Inject
    UserRepo userRepo;


    @POST
    public Response login(CredentialsDTO credentials) {

        UserEntity user = this.userRepo.findByUsername(credentials.getUsername());
        if (user == null) {
            return Response.status(404).build();
        }
        PasswordSaltModel hashedData = this.userRepo.getPaswordByUsername(credentials.getUsername());
        if (!BCrypt.checkpw(credentials.getPassword(), hashedData.password)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        long exp = Instant.now().getEpochSecond() + lifespan;
        String token = Jwt.claim(Claims.upn.name(), credentials.getPassword()).groups(Set.of("user", "admin")).sign();
        String entity = Json.createObjectBuilder().add("token", token).add("expires_at", exp).build().toString();

        return Response.ok().entity(entity).build();
    }

}

