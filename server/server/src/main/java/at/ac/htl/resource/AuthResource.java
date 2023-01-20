package at.ac.htl.resource;

import at.ac.htl.entity.User;
import at.ac.htl.model.CredentialsDTO;
import at.ac.htl.model.PasswordSaltModel;
import at.ac.htl.repository.UserRepo;
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
//@RolesAllowed("user")
@Path("/api/auth")
public class AuthResource {

    @Inject
    UserRepo userRepo;
    @Inject
    @ConfigProperty(name = "smallrye.jwt.new-token.lifespan")
    long lifespan;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(CredentialsDTO credentials) {

        User user = this.userRepo.findByUsername(credentials.getUsername());
        if (user == null) {
            return Response.status(404).build();
        }

        PasswordSaltModel hashedData = this.userRepo.getPaswordByUsername(credentials.getUsername());

        String hashedPassword = BCrypt.hashpw(hashedData.password, hashedData.salt);
        System.out.println(hashedPassword + " HasshedpwakjÖKJNDSIJDSÖF");
        System.out.println(hashedData.password + " pw form db");
        System.out.println(credentials.getPassword() + " Password");
        if (BCrypt.checkpw(credentials.getPassword(), hashedPassword)) {
            System.out.println("The password is correct." + hashedPassword);
            System.out.println(hashedData.salt);
        } else {
            System.out.println(hashedData.salt);
            System.out.println("The password is incorrect." + hashedPassword);

        }

        long exp = Instant.now().getEpochSecond() + lifespan;
        String token = Jwt.claim(Claims.upn.name(), credentials.getPassword()).groups(Set.of("user", "admin")).sign();
        String entity = Json.createObjectBuilder().add("token", token).add("expires_at", exp).build().toString();

        return Response.ok().entity(entity).build();
    }

}
