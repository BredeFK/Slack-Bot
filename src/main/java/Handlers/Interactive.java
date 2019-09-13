package Handlers;

import Classes.*;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

// Handlers.Interactive handles interactivity from slack users (https://api.slack.com/messaging/interactivity#components)
@WebServlet("/interactive")
public class Interactive extends HttpServlet {
    private static final Logger logger = Logger.getLogger(Interactive.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.INFO, "POST request to /interactive");


        // Get body from request
        String body = new GeneralFunctions().getBody(req);

        resp.setStatus(HttpServletResponse.SC_OK);


        // Source: https://stackoverflow.com/a/16453677/8883030
        // Decode x-www-form-urlencoded and remove 'payload=' at the beginning (Convert to json string)
        String payload = null;
        try {
            // payload = URLDecoder.decode(body, StandardCharsets.UTF_8).replace("payload=", "");
            payload = new URI(body).getPath().replace("payload=", "");
        } catch (URISyntaxException e) {
            logger.log(Level.WARNING, "Error parsing x-www-form-urlencoded from body: " + e.getMessage());
            return;
        }


        // Convert json string to object of InteractiveResponse
        InteractiveResponse response = new GsonBuilder().create().fromJson(payload, InteractiveResponse.class);

        if (response.getType() == null || response.getType().isEmpty()) {
            logger.log(Level.WARNING, "Error: Something went wrong from parsing 'x-www-form-urlencoded' to InterActiveResponse object");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            logger.log(Level.INFO, "'x-www-form-urlencoded' was parsed successfully");
            respondToUserChoice(req, resp, response);
        }
    }

    private void respondToUserChoice(HttpServletRequest req, HttpServletResponse resp, InteractiveResponse interactiveResponse) {

        // Check if actions List is empty
        if (interactiveResponse.getActions().isEmpty()) {
            logger.log(Level.WARNING, "Actions Error: actions list is empty!");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // Get environment variables
        EnvVars envVars = new EnvVars();

        // Get repository from api url
        Repository repo = getRepository(interactiveResponse.getActions().get(0).getSelected_option().getValue());

        // Check if repository object is empty
        if (repo.getName() == null || repo.getName().isEmpty()) {
            logger.log(Level.WARNING, "Repository Error: Repository object is empty");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        repo.setChannelID(envVars.getChannelGeneral());

        // Try to post message to slack user
        SlackResponse slackResponse = new GeneralFunctions().postSlackMessage(interactiveResponse.getResponse_url(), envVars.getTOKEN(), repo.toJson());

        // Check for errors and log them
        if (!slackResponse.isOk()) {
            logger.log(Level.WARNING, "Interactive Error: " + slackResponse.getError());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // Check for warnings and log them
        if (slackResponse.getWarning() != null && !slackResponse.getWarning().isEmpty()) {
            logger.log(Level.WARNING, "Interactive Warning : " + slackResponse.getWarning());
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
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
