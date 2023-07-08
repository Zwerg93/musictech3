package at.ac.htl.resources;

import at.ac.htl.entity.SongEntity;
import at.ac.htl.model.YoutubeDownloadDTO;
import at.ac.htl.repo.SongRepo;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import io.quarkus.narayana.jta.QuarkusTransaction;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

@Produces("application/json")
@Path("/youtube")

@Consumes("application/json")
public class YoutubeResource {


    String postURL;
    //private String postURL = "http://localhost:8080/api/stream/download/";
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

    @POST

    @Path("/download/mp3/")
    public Response downloadSongByYTID(YoutubeDownloadDTO data) throws IOException {
        this.postURL = "http://83.215.72.88:8080/api/stream/download/";
        StringBuilder sbf1 = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://api.vevioz.com/api/button/mp3/" + data.id);
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




        URL mp3Url = new URL(sbf1.substring(sbf1.indexOf(data.id) - 35, sbf1.indexOf(data.id) + 97));
        URLConnection connection = mp3Url.openConnection();
        int totalSize = connection.getContentLength();
        int downloadedSize = 0;

        try (InputStream in = connection.getInputStream();
             OutputStream out = new FileOutputStream("src/main/resources/files/" + data.title + ".mp3")) {

            byte[] buffer = new byte[4096*2];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                downloadedSize += bytesRead;
                // Fortschritt anzeigen
                System.out.printf("Downloading: %d/%d bytes%n", downloadedSize, totalSize);
            }
        }

        System.out.println("Download abgeschlossen!");






        try {
           // fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            QuarkusTransaction.begin();
            postURL = postURL + data.title + ".mp3";
            SongEntity song = new SongEntity(data.title, postURL, data.artist, data.thumbnailUrl);
            repo.persist(song);
            QuarkusTransaction.commit();
            System.out.println("Donwload succes " + postURL + " titel " + data.title);
        } catch (Exception e) {
            e.printStackTrace();
            QuarkusTransaction.rollback();
        }

        return Response.ok().build();
    }

}
