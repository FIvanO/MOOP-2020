package com.lab.L8Server;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @RequestMapping("/processRequest")
    public ServerResponse processRequest(@RequestParam(value = "request", defaultValue = "Hi!") String request) throws Exception {
        Server server = new Server();
        return new ServerResponse(server.processClientMessage(request));
    }

}
