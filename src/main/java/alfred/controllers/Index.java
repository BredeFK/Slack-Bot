package alfred.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class Index {
    private static final Logger logger = Logger.getLogger(Index.class.getName());

    public Index() {

    }

    @GetMapping(value = "/")
    public ResponseEntity<String> indexGET(@RequestParam(value = "id", defaultValue = "1337") int id) {
        logger.log(Level.INFO, "GET request on /");

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "text/html;charset=UTF-8");

        return new ResponseEntity<>(
                "<h1>Welcome to my index page</h1><h2>This is all</h2>", headers, HttpStatus.ACCEPTED);
    }
}
