package at.ac.htl.resources;

import at.ac.htl.entity.SongEntity;
import at.ac.htl.model.SongModel;
import at.ac.htl.repo.SongRepo;


import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/song")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class SongResource {

    @Inject
    SongRepo songRepo;

    @GET
    @Path("/all")
    public List<SongEntity> getAllSongs() {
        return this.songRepo.findAll().stream().toList();
    }

    @GET
    @Path("/{id}")
    public SongEntity getSongById(@PathParam("id") long id) {
        return this.songRepo.findById(id);
    }

    @POST
    @Transactional
    @Path("/add")
    public Response addSong(SongModel data) {
        SongEntity song = new SongEntity(data.title, data.url, data.artist);
        this.songRepo.persist(song);
        return Response.ok().build();
    }


}
