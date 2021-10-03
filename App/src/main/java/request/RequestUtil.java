package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * This class will make any Http Request to any server
 * It is reusable
 * Returns the response from a server as a String
 */

public class RequestUtil implements IRequest {
    private final String urlString;
    private final String json;
    private final RequestType requestType;
    private int responseCode;

    public RequestUtil(String url, String json, RequestType requestType) {
        this.urlString = url;
        this.json = json;
        this.requestType = requestType;
    }

    @Override
    public String makeRequest() throws HTTPRequestException {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestType.toString());
            connection.setRequestProperty("Accept", "application/*");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);
            if (json.length() != 0) {
                convertToBytes(connection);
            }
            String responeFetched = fetchResponse(connection);
            responseCode = connection.getResponseCode();
            return responeFetched;
        } catch (MalformedURLException e) {
            throw new HTTPRequestException(500, "Url not valid", e);
        } catch (IOException e) {
            throw new HTTPRequestException(500, "Could not make request", e);
        }
    }

    private void convertToBytes(HttpURLConnection connection) {
        try {
            OutputStream os = connection.getOutputStream();
            byte[] input = json.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }  catch (IOException ignored) {

        }
    }

    private String fetchResponse(HttpURLConnection connection) throws IOException{

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            br.close();
            return response.toString();
    }
}
