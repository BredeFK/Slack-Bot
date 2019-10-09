package alfred.controllers;

import alfred.models.general.EnvVars;
import alfred.models.github.Repository;
import alfred.models.slack.InteractiveResponse;
import alfred.models.slack.SlackResponse;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

// alfred.Handlers.Interactive handles interactivity from slack users (https://api.slack.com/messaging/interactivity#components)
@RestController
public class Interactive {
    private static final Logger logger = Logger.getLogger(Interactive.class.getName());

    @PostMapping(value = "/api/slack/interactive", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody
    ResponseEntity<String> interactivePOST(HttpEntity<String> request, HttpServletRequest httpServletRequest) {
        logger.log(Level.INFO, "POST request on {0}", httpServletRequest.getRequestURL());


        if (request.getBody() == null) {
            logger.log(Level.WARNING, "Interactive Error: request is null");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Source: https://stackoverflow.com/a/16453677/8883030
        // Decode x-www-form-urlencoded and remove 'payload=' at the beginning (Convert to json string)
        try {
            String payload = new URI(request.getBody()).getPath().replace("payload=", "");

            // Convert json string to object of InteractiveResponse
            InteractiveResponse response = new GsonBuilder().create().fromJson(payload, InteractiveResponse.class);

            if (response.getType() == null || response.getType().isEmpty()) {
                logger.log(Level.WARNING, "Error: Something went wrong from parsing 'x-www-form-urlencoded' to InterActiveResponse object");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                logger.log(Level.INFO, "'x-www-form-urlencoded' was parsed successfully");
                return respondToUserChoice(response);
            }

        } catch (URISyntaxException e) {
            logger.log(Level.WARNING, "Error parsing x-www-form-urlencoded from body: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<String> respondToUserChoice(InteractiveResponse interactiveResponse) {

        // Check if actions List is empty
        if (interactiveResponse.getActions().isEmpty()) {
            logger.log(Level.WARNING, "Actions Error: actions list is empty!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Get environment variables
        EnvVars envVars = new EnvVars();

        String prefixURL = "https://api.github.com/repos/";
        String githubRepoName = interactiveResponse.getActions().get(0).getSelected_option().getValue();

        // Get repository from api url
        Repository repo = getRepository(prefixURL + githubRepoName);

        // Check if repository object is empty
        if (repo.getName() == null || repo.getName().isEmpty()) {
            logger.log(Level.WARNING, "Repository Error: Repository object is empty");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        repo.setChannelID(envVars.getChannelGeneral());

        // Try to post message to slack user
        SlackResponse slackResponse = new GeneralFunctions().postSlackMessage(interactiveResponse.getResponse_url(), envVars.getToken(), repo.toJson());

        // Check for errors and log them
        if (!slackResponse.isOk()) {
            logger.log(Level.WARNING, "Interactive Error: " + slackResponse.getError());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Check for warnings and log them
        if (slackResponse.getWarning() != null && !slackResponse.getWarning().isEmpty()) {
            logger.log(Level.WARNING, "Interactive Warning : " + slackResponse.getWarning());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Repository getRepository(String URL) {
        HttpResponse<JsonNode> response = null;
        try {

            // Get the repository in json format
            response = Unirest.get(URL)
                    .header("accept", "application/json")
                    .asJson();
        } catch (UnirestException e) {
            logger.log(Level.WARNING, "getRepository() Error : " + e.getMessage());
            return new Repository();
        }

        // Convert from json to class
        return new GsonBuilder().create().fromJson(String.valueOf(response.getBody()), Repository.class);
    }
}
