package alfred.controllers;

import alfred.models.general.EnvVars;
import alfred.models.ipify.IPInfo;
import alfred.models.ipify.Location;
import alfred.models.logs.Logs;
import alfred.services.IPInfoService;
import alfred.services.LogsService;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ErrorHandler implements ErrorController {
    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());
    private static final String PATH = "/error";
    private EnvVars envVars = new EnvVars();

    @Autowired
    private IPInfoService ipInfoService;

    @Autowired
    private LogsService logsService;

    @RequestMapping(PATH)
    public ResponseEntity<String> error(HttpServletRequest httpServletRequest,
                                        @RequestAttribute(RequestDispatcher.ERROR_STATUS_CODE) Integer statusCode,
                                        @RequestAttribute(RequestDispatcher.ERROR_REQUEST_URI) String path,
                                        @RequestHeader("User-Agent") String userAgent) throws IOException, UnirestException, InterruptedException {

        // Get status code
        HttpStatus status = HttpStatus.valueOf(statusCode);

        // Get users IP address
        String ip = httpServletRequest.getRemoteAddr();


        int nuberOfTriesToday = (1 + logsService.getNumberOfTodaysErrorsByIp(ip));
        // Sleep for 1min if it has been more than 15 request on the same day from the same user
        if (nuberOfTriesToday > 15) {
            logger.warning("Request has reached more than 15 from one use on one day. Sleeping for 1min...");
            Thread.sleep(60 * 1000);
        }


        // Init location and log
        IPInfo iPinfo;

        // Get location info if possible, below is for testing on localhost
        if (ip.equals("0:0:0:0:0:0:0:1") || envVars.getIpifyToken().isEmpty()) {
            iPinfo = new IPInfo(ip, "TST", "Test Region", "Test City", "+00:00");
        } else if (ipInfoService.exist(ip)) {
            // Get information from db
            iPinfo = ipInfoService.getByID(ip);
        } else {
            // Ip is not from testing or in db, get ip info and add to db

            try {
                iPinfo = getInfoFromIP(ip);
            } catch (Exception e) {
                logger.log(Level.WARNING, e.getMessage());
                return new ResponseEntity<>(
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
            ipInfoService.add(iPinfo);
        }

        logsService.add(new Logs(ip, statusCode, path, userAgent, new Date()));


        // Get country name from code
        Location location = iPinfo.getLocation();
        Locale locale = new Locale("", location.getCountryCode());

        // Format user's location
        String userLocation;
        if (location.getRegion().isEmpty() && location.getCity().isEmpty()) {
            userLocation = String.format("[%s]%s", location.getCountryCode(), locale.getDisplayCountry());
        } else {
            userLocation = String.format("[%s]%s: %s, %s", location.getCountryCode(), locale.getDisplayCountry(), location.getRegion(), location.getCity());
        }

        // Create detailed error message
        String logMessage = String.format("%s (%s) | Error: %d - %s | Url suffix: %s | User-Agent: %s", userLocation, ip, statusCode, status.getReasonPhrase(), path, userAgent);

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
    private IPInfo getInfoFromIP(String IP) throws Exception {
        String url = String.format("https://geo.ipify.org/api/v1?apiKey=%s&ipAddress=%s", envVars.getIpifyToken(), IP);
        HttpResponse<JsonNode> response = null;


        // Get info about ip in json format
        response = Unirest.get(url)
                .header("accept", "application/json")
                .asJson();

        if (response.getBody().toString().contains("code")) {
            JSONObject jsonObject = new JSONObject(response.getBody().toString());
            String msg = String.format("Error Code %s \"%s\" (%s)", jsonObject.get("code"), jsonObject.get("messages"), IP);
            throw new Exception(msg);
        }

        // Convert from json to class
        return new GsonBuilder().create().fromJson(String.valueOf(response.getBody()), IPInfo.class);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
