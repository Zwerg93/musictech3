package at.ac.htl.resource;

import at.ac.htl.entity.Song;
import at.ac.htl.model.SongModel;
import at.ac.htl.repository.SongRepo;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/song")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SongResource {

    @Inject
    SongRepo songRepo;

    @GET
    @Path("/all")
    public List<Song> getAll() {
        return this.songRepo.findAll().stream().toList();
    }

    @POST
    @Transactional
    public Response addSong(SongModel data) {
        Song song = this.songRepo.checkName(data.name);

        if (song != null) {
            return Response.ok(404).build();
        }
        song = new Song(data.name, data.url, data.artist);
        this.songRepo.persist(song);

        return Response.ok().build();
    }


}
