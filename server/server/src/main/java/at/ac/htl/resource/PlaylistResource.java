package at.ac.htl.resource;

import at.ac.htl.entity.Playlist;
import at.ac.htl.repository.PlaylistRepo;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/playlist")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlaylistResource {

    @Inject
    PlaylistRepo playlistRepo;

    @Path("/all")
    @GET
    public List<Playlist> getAll() {
        return this.playlistRepo.findAll().stream().toList();
    }


}
