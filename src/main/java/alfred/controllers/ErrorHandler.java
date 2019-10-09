package alfred.controllers;

import alfred.models.general.EnvVars;
import alfred.models.ipify.Location;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
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
    private EnvVars envVars = new EnvVars();


    @RequestMapping(PATH)
    public ResponseEntity<String> error(HttpServletRequest httpServletRequest,
                                        @RequestAttribute("javax.servlet.error.status_code") Integer statusCode) throws IOException, UnirestException {

        // Get status code
        HttpStatus status = HttpStatus.valueOf(statusCode);

        // Get users IP address
        String IP = httpServletRequest.getRemoteAddr();

        // Init location
        Location location = null;

        // Get location info if possible
        if (IP.equals("0:0:0:0:0:0:0:1") || envVars.getIpifyToken().isEmpty()) {
            location = new Location(IP, "testcountry", "testRegion", "testCity", "+00:00");
        } else {
            location = getInfoFromIP(IP);
        }

        // Create detailed error message
        String logMessage = String.format("Error: %d - %s | %s: %s, %s (%s)", statusCode, status.getReasonPhrase(), location.getCountry(), location.getRegion(), location.getCity(), IP);

        // Log event
        logger.log(Level.WARNING, logMessage);

        // Get html body
        String body = new GeneralFunctions().getFileAsString("files/error.html");

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "text/html;charset=UTF-8");

        // Fill in dynamic variables
        body = body.replace("{{ status }}", Integer.toString(statusCode)).
                replace("{{ error }}", status.getReasonPhrase()).
                replace("{{ image }}", "https://picsum.photos/1920/1080");

        // Return error page to user
        return new ResponseEntity<>(
                body, headers, status
        );
    }

    // Returns locations from Ip address
    private Location getInfoFromIP(String IP) throws UnirestException {
        String url = String.format("https://geo.ipify.org/api/v1?apiKey=%s&ipAddress=%s", envVars.getIpifyToken(), IP);
        HttpResponse<JsonNode> response = null;


        // Get info about ip in json format
        response = Unirest.get(url)
                .header("accept", "application/json")
                .asJson();


        // Convert from json to class
        return new GsonBuilder().create().fromJson(String.valueOf(response.getBody()), Location.class);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
