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
import java.util.logging.Level;
import java.util.logging.Logger;

// alfred.Handlers.Event handles events from slack (https://api.slack.com/events-api)
@RestController
public class Event {
    private static final Logger logger = Logger.getLogger(Event.class.getName());

    @PostMapping(value = "/api/slack/event")
    public ResponseEntity<String> eventPOST(@RequestHeader("X-Slack-Signature") String header, @RequestBody EventRequest request, HttpServletRequest httpServletRequest) {
        logger.log(Level.INFO, "POST request on {0}", httpServletRequest.getRequestURL());

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "text/plain;charset=UTF-8");

        return new ResponseEntity<>(
                request.getChallenge(), headers, HttpStatus.OK);
    }
}
