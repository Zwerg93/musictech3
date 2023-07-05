package at.ac.htl.test;

import at.ac.htl.entity.SongEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.ws.rs.core.Response;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;

public class TheMain {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String youtubeID = sc.nextLine();
        downloadSong(youtubeID);


    }


    public static void downloadSong(String youtubeID) {
        String postURL = "http://localhost:8080/api/stream/download/";
        postURL = "http://localhost:8080/api/stream/download/";
        StringBuilder sbf1 = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://api.vevioz.com/api/button/mp3/" + youtubeID);
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

        // sbf1.substring(sbf1.indexOf(youtubeID) - 35, sbf1.indexOf(youtubeID) + 97)
        try (BufferedInputStream in = new BufferedInputStream(new URL( sbf1.substring(sbf1.indexOf(youtubeID) - 35, sbf1.indexOf(youtubeID) + 97)).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(youtubeID + ".mp3")) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // handle exception
        }

    }
}
