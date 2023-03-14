package at.ac.htl.resource;

import at.ac.htl.entity.Song;
import at.ac.htl.repository.SongRepo;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Path("/api/stream")
public class StreamResource {

    @Inject
    SongRepo songRepo;
    private final String UPLOADED_FILE_PATH = "src/main/resources/songs/";

    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    @Transactional
    public Response uploadFile(MultipartFormDataInput input) {


        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        for (InputPart inputPart : inputParts) {

            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String fileName = getFileName(header);
                String artist = getArtist(header);
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                String postURL = "http://localhost:8080/api/stream/" + fileName;
                Song song = new Song(fileName, postURL, artist);
                songRepo.persist(song);
                byte[] bytes = IOUtils.toByteArray(inputStream);
                fileName = UPLOADED_FILE_PATH + fileName;
                writeFile(bytes, fileName);
                System.out.println("Done");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Response.status(200).entity("Succes :)").build();
    }

    private String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    private String getArtist(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("artist"))) {
                String[] name = filename.split("=");
                String finalArtist = name[1].trim().replaceAll("\"", "");
                return finalArtist;
            }
        }
        return "unknown";
    }

    private void writeFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);

        if (!file.exists()) {
            file.createNewFile();
            System.out.println("succes");
        }
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(content);
        fop.flush();
        fop.close();

    }
}
