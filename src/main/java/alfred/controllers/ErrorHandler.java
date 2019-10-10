package alfred.controllers;

import alfred.models.general.EnvVars;
import alfred.models.ipify.IPinfo;
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

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ErrorHandler implements ErrorController {
    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());
    private static final String PATH = "/error";
    private EnvVars envVars = new EnvVars();


    @RequestMapping(PATH)
    public ResponseEntity<String> error(HttpServletRequest httpServletRequest,
                                        @RequestAttribute(RequestDispatcher.ERROR_STATUS_CODE) Integer statusCode,
                                        @RequestAttribute(RequestDispatcher.ERROR_REQUEST_URI) String path) throws IOException, UnirestException {

        // Get status code
        HttpStatus status = HttpStatus.valueOf(statusCode);

        // Get users IP address
        String ip = httpServletRequest.getRemoteAddr();

        // Init location
        IPinfo iPinfo;

        // Get location info if possible
        if (ip.equals("0:0:0:0:0:0:0:1") || envVars.getIpifyToken().isEmpty()) {
            iPinfo = new IPinfo(ip, "TST", "Test Region", "Test City", "+00:00");
        } else {
            iPinfo = getInfoFromIP(ip);
        }

        // Get country name from code
        Location location = iPinfo.getLocation();
        Locale locale = new Locale("", location.getCountryCode());

        // TODO get users local time and log : https://stackoverflow.com/questions/2375222/java-simpledateformat-for-time-zone-with-a-colon-separator

        // Format user's location
        String userLocation;
        if (location.getRegion().isEmpty() && location.getCity().isEmpty()) {
            userLocation = String.format("(%s)%s", location.getCountryCode(), locale.getDisplayCountry());
        } else {
            userLocation = String.format("(%s)%s: %s, %s", location.getCountryCode(), locale.getDisplayCountry(), location.getRegion(), location.getCity());
        }

        // Create detailed error message
        String logMessage = String.format("Error: %d - %s | Url suffix: %s | %s (%s)", statusCode, status.getReasonPhrase(), path, userLocation, ip);

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

    // Returns information about ip address from Ip address
    private IPinfo getInfoFromIP(String IP) throws UnirestException {
        String url = String.format("https://geo.ipify.org/api/v1?apiKey=%s&ipAddress=%s", envVars.getIpifyToken(), IP);
        HttpResponse<JsonNode> response = null;


        // Get info about ip in json format
        response = Unirest.get(url)
                .header("accept", "application/json")
                .asJson();

        // Convert from json to class
        return new GsonBuilder().create().fromJson(String.valueOf(response.getBody()), IPinfo.class);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
