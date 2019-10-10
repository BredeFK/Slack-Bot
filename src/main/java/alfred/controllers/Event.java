package alfred.controllers;

import alfred.models.slack.EventRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

// alfred.Handlers.Event handles events from slack (https://api.slack.com/events-api)
@RestController
public class Event {
    private static final Logger logger = Logger.getLogger(Event.class.getName());

    @PostMapping(value = "/api/slack/event")
    public ResponseEntity<String> eventPOST(@RequestHeader("X-Slack-Signature") String slackSignature,
                                            @RequestHeader("X-Slack-Request-Timestamp") long timestamp,
                                            @RequestBody String body,
                                            @RequestBody EventRequest request,
                                            HttpServletRequest httpServletRequest) throws InvalidKeyException, NoSuchAlgorithmException {

        logger.log(Level.INFO, "POST request on {0}", httpServletRequest.getRequestURL());

        // Check if if the request is authenticated
        String errorMessage = new GeneralFunctions().authenticatedRequest(timestamp, body, slackSignature);
        if (!errorMessage.isEmpty()) {
            logger.log(Level.WARNING, errorMessage);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "text/plain;charset=UTF-8");

        return new ResponseEntity<>(
                request.getChallenge(), headers, HttpStatus.OK);
    }
}
