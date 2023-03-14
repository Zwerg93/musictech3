package at.ac.htl.resources;

import at.ac.htl.entity.Playlist;
import at.ac.htl.entity.SongEntity;
import at.ac.htl.model.PlaylistDTO;
import at.ac.htl.repo.PlaylistRepo;
import at.ac.htl.repo.SongRepo;
import at.ac.htl.repo.UserRepo;
import com.oracle.svm.core.annotate.Inject;

import javax.transaction.Transactional;
import javax.websocket.server.PathParam;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/playlist")
@Produces("application/json")
@Consumes("application/json")

public class PlaylistResource {
    @Inject
    PlaylistRepo playlistRepo;

    @Inject
    UserRepo userRepo;

    @Inject
    SongRepo songRepo;

    @GET
    @Path("/all")
    public List<Playlist> getAllPlaylists() {
        return this.playlistRepo.findAll().stream().toList();
    }

    @GET
    @Path("/{id}")
    public Playlist getPlaylistbyId(@PathParam("id") Long id) {
        return this.playlistRepo.findById(id);
    }

    @POST
    @Transactional
    @Path("/add")
    public Response addPlaylist(PlaylistDTO data) {
        Playlist p = new Playlist(data.name, this.userRepo.findByUsername(data.username));
        this.playlistRepo.persist(p);
        return Response.ok().build();
    }

    @PUT
    @Transactional
    @Path("/add/{playlistID}/{songId}")
    public Response addSongToPlaylist(@PathParam("playlistID") long playlistId, @PathParam("songId") long songid) {
        SongEntity song = this.songRepo.findById(songid);
        if (song == null) {
            return Response.status(404).build();
        }
        Playlist playlist = this.playlistRepo.findById(playlistId);
        if (playlist == null) {
            return Response.status(404).build();
        }
        playlist.songList.add(song);
        return Response.ok().build();
    }

}
