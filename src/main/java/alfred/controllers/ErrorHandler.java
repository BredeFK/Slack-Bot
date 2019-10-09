package alfred.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ErrorHandler implements ErrorController {
    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());
    private static final String PATH = "/error";


    @RequestMapping(PATH)
    public ResponseEntity<String> error(HttpServletRequest httpServletRequest,
                                        @RequestAttribute("javax.servlet.error.status_code") Integer statusCode) throws IOException {

        // Log event
        logger.log(Level.WARNING, "request on {0}", httpServletRequest.getRequestURL());

        // Get status code
        HttpStatus status = HttpStatus.valueOf(statusCode);

        // Get html body
        String error = new GeneralFunctions().getFileAsString("error.html");
        error = error.replace("{{ status }}", Integer.toString(statusCode));
        error = error.replace("{{ error }}", status.getReasonPhrase());

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "text/html;charset=UTF-8");


        return new ResponseEntity<>(
                error, headers, status
        );
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
