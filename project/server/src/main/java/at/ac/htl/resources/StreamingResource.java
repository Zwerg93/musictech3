package at.ac.htl.resources;

import at.ac.htl.entity.SongEntity;
import at.ac.htl.repo.SongRepo;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


@Path("/stream")

public class StreamingResource {

    @Inject
    SongRepo songRepo;

    private final String UPLOADED_FILE_PATH = "src/main/resources/files/";

    private String postURL = "http://localhost:8080/steam/download/";

    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    @Transactional
    public Response uploadFile(MultipartFormDataInput input) {
        String fileName = "";
        String artist;

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        for (InputPart inputPart : inputParts) {

            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                fileName = getFileName(header);
                artist = getArtist(header);
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                System.out.println(fileName);
                postURL += fileName;
                System.out.println(postURL);
                SongEntity song = new SongEntity(fileName, postURL, artist);
                songRepo.persist(song);
                byte[] bytes = IOUtils.toByteArray(inputStream);
                fileName = UPLOADED_FILE_PATH + fileName;
                writeFile(bytes, fileName);
                System.out.println("Done");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Response.status(200).entity("{\"message\":\"uploadFile is called, Uploaded file name : " + fileName + "\"}").build();
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

    @GET
    @Path("/download/{fileName}")
    @Produces({"audio/mpeg"})
    public Response downloadFile(@PathParam("fileName") String fileName) {

        File audioFile = new File(UPLOADED_FILE_PATH + fileName);
        if (!audioFile.exists()) {
            throw new RuntimeException("File not found: " + UPLOADED_FILE_PATH + fileName);
        }
        Response.ResponseBuilder res = Response.ok((Object) audioFile);
        res.header("Content-Disposition", "inline;filename=" + fileName);
        return res.build();
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
