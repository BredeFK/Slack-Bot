package alfred.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ErrorHandler implements ErrorController {
    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());
    private static final String PATH = "/error";


    @RequestMapping(PATH)
    public ResponseEntity<String> error(HttpServletRequest httpServletRequest) throws IOException {
        logger.log(Level.WARNING, "request on {0}", httpServletRequest.getRequestURL());
        Integer statusCode = (Integer) httpServletRequest.getAttribute("javax.servlet.error.status_code");

        String error = getHTMLError();

        return new ResponseEntity<String>(error, HttpStatus.valueOf(statusCode));
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    // TODO make only one functionn and implement vars
    private String getHTMLError() throws IOException {

        // Get file path
        Path filePath = Paths.get("error.html");

        // Get file
        File file = new File(filePath.toString());

        // Check if file exists
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }
}
