package rest.client;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lab.L8Server.ServerResponse;

import java.io.*;


public class Client {
    private static final Gson GSON = new GsonBuilder().create();
    private static final HttpTransport HTTP_TRANSPORT = new ApacheHttpTransport();
    private static final GsonFactory GSON_FACTORY = new GsonFactory();
    private static final HttpRequestFactory REQUEST_FACTORY = HTTP_TRANSPORT.createRequestFactory();

    public static String sendRequest(String command) throws IOException {
        HttpRequest req = REQUEST_FACTORY.buildRequest("GET", new Request(command), null);

        HttpResponse httpResponse = req.execute();
        ServerResponse response = GSON.fromJson(httpResponse.parseAsString(), ServerResponse.class);
        return response.getResponse();
    }

    public static void main(String[] args) {
        while (true) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                System.out.println("Input your query:");
                String word = reader.readLine();

                if (word.equals("exit")) {
                    break;
                }

                String response = sendRequest(word);
                System.out.println(response);

            } catch (Exception ex) {

            }
        }

    }
}
