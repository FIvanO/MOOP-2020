package rest.client;

import com.google.api.client.http.GenericUrl;

public class Request extends GenericUrl {
    public Request(String request) {
        super("http://localhost:8080/processRequest?request=" + request);
    }
}