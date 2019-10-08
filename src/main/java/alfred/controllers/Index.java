package alfred.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class Index {
    private static final Logger logger = Logger.getLogger(Index.class.getName());

    public Index() {

    }

    @GetMapping(value = "/api/slack/")
    public ResponseEntity<String> indexGET(HttpServletRequest httpServletRequest) {
        logger.log(Level.INFO, "GET request on {0}", httpServletRequest.getRequestURL());

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "text/html;charset=UTF-8");

        return new ResponseEntity<>(
                "<h1>Welcome to my index page for api/slack/</h1><h2>This is all</h2>", headers, HttpStatus.ACCEPTED);
    }
}
