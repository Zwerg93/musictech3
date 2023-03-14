package at.ac.htl.resources;

import at.ac.htl.entity.SongEntity;
import at.ac.htl.repo.SongRepo;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

@Produces("application/json")
@Path("/youtube")
@Consumes("application/json")
public class YoutubeResource {
    private String postURL = "http://localhost:8080/stream/download/";
    private static final String DEVELOPER_KEY = "AIzaSyDDd_3IHYSGqMpzuybFRnirJrVeRIl4i5Y";
    private static final String APPLICATION_NAME = "Musictech";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    @Inject
    SongRepo repo;

    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null).setApplicationName(APPLICATION_NAME).build();
    }

    @GET
    @Path("/search/{searchstring}")
    public ArrayList<SearchListResponse> getAllSongs(@PathParam("searchstring") String searchstring) throws GeneralSecurityException, IOException {
        return getYoutubeVideoIdsByString(searchstring);
    }

    public ArrayList<SearchListResponse> getYoutubeVideoIdsByString(String searchstring) throws GeneralSecurityException, IOException {
        YouTube youtubeService = getService();

        YouTube.Search.List request = youtubeService.search().list("snippet");
        SearchListResponse response = request.setKey(DEVELOPER_KEY).setMaxResults(10L).setQ(searchstring).execute();

        ArrayList<SearchListResponse> responselist = new ArrayList<>();


        for (int i = 0; i < response.size() - 1; i++) {
            System.out.println(response.getItems().get(i).getId().get("kind"));
            if (response.getItems().get(i).getId().get("kind").equals("youtube#video")) {
                System.out.println((String) response.getItems().get(i).getId().get("videoId"));
                responselist.add(response);
            } else {
                System.out.println("you cant download a Youtube Channel :)");
            }
        }

        return responselist;
    }

    @GET
    @Transactional
    @Path("/download/mp3/{id}/{title}")
    public Response downloadSongByYTID(@PathParam("id") String id, @PathParam("title") String title) {
        StringBuilder sbf1 = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://api.vevioz.com/api/button/mp3/" + id);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException ex) {
            System.out.println("erreor " + ex);
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        } catch (IOException ex) {
            System.out.println("error " + ex);
        }
        String line = "";
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException ex) {
                System.out.println("error " + ex);
            }
            sbf1.append(line);
        }
        URL website;
        try {
            website = new URL(sbf1.substring(sbf1.indexOf(id) - 35, sbf1.indexOf(id) + 97));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ReadableByteChannel rbc = null;
        try {
            rbc = Channels.newChannel(website.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream("src/main/resources/files/" + title + ".mp3");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            postURL += title + ".mp3";


            SongEntity song = new SongEntity(title, postURL, "");
            repo.persist(song);
            System.out.println("Donwload succes " + title);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok().build();
    }

}
