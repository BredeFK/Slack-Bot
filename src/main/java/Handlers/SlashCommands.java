package Handlers;

import Classes.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Handlers.SlashCommands handles commands from slack-users (https://api.slack.com/slash-commands)
@WebServlet("/slashcommands")
public class SlashCommands extends HttpServlet {
    // Get logger
    private static final Logger logger = Logger.getLogger(SlashCommands.class.getName());

    private static final String SLACK_MSG_URL = "https://slack.com/api/chat.postMessage";

    // Get environment variables
    private EnvVars envVars = new EnvVars();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.INFO, "POST request on /slashcommands");

        if (!req.getHeader("X-Slack-Signature").isEmpty() && req.getParameter("channel_id").contains(envVars.getChannelGeneral())) {

            // get command and remove whitespace
            String command = req.getParameter("command").replaceAll("\\s+", "");

            // CHeck for valid command and respond accordingly
            switch (command) {
                case "/quote":
                    sendQuote(req, resp);
                    break;
                case "/github":
                    sendGithubUser(req, resp);
                    break;
                case "/mannen":
                    mannenFallen(req, resp);
                    break;
                case "/dovre":
                    dovreFallen(req, resp);
                    break;
                default:
                    logger.log(Level.WARNING, "The command '" + command + "' is not valid");
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    break;
            }
        } else {
            logger.log(Level.WARNING, "Not authorized");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void mannenFallen(HttpServletRequest req, HttpServletResponse resp) {
        Mountain mannen = getMountainStatus("https://www.harmannenfalt.no/api");

        if (mannen != null) {
            mannen.setChannelID(envVars.getChannelGeneral());

            SlackResponse msgResponse = new GeneralFunctions().postSlackMessage(SLACK_MSG_URL, envVars.getTOKEN(), mannen.toJson("Mannen"));

            // Check for errors and log them
            if (!msgResponse.isOk()) {
                logger.log(Level.WARNING, "MannenFallen Error: " + msgResponse.getError());
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            // Check for warnings and log them
            if (msgResponse.getWarning() != null && !msgResponse.getWarning().isEmpty()) {
                logger.log(Level.WARNING, "MannenFallen Warning : " + msgResponse.getWarning());
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            logger.log(Level.WARNING, "something went wrong with getting mannen");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void dovreFallen(HttpServletRequest req, HttpServletResponse resp) {
        Mountain dovre = getMountainStatus("https://www.hardovrefalt.no/api");

        if (dovre != null) {
            dovre.setChannelID(envVars.getChannelGeneral());

            SlackResponse msgResponse = new GeneralFunctions().postSlackMessage(SLACK_MSG_URL, envVars.getTOKEN(), dovre.toJson("Dovre"));

            // Check for errors and log them
            if (!msgResponse.isOk()) {
                logger.log(Level.WARNING, "DovreFallen Error: " + msgResponse.getError());
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            // Check for warnings and log them
            if (msgResponse.getWarning() != null && !msgResponse.getWarning().isEmpty()) {
                logger.log(Level.WARNING, "DovreFallen Warning : " + msgResponse.getWarning());
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            logger.log(Level.WARNING, "something went wrong with getting dovre");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Mountain getMountainStatus(String URL) {
        HttpResponse<JsonNode> response = null;
        try {

            // Get the quote in json format
            response = Unirest.get(URL)
                    .header("accept", "application/json")
                    .asJson();
        } catch (UnirestException e) {
            logger.log(Level.WARNING, "getMountainStatus Error : " + e.getMessage());
            return new Mountain();
        }

        // Convert from json to class
        return new GsonBuilder().create().fromJson(String.valueOf(response.getBody()), Mountain.class);

    }

    // sendQuote Sends the daily quote to the channel
    private void sendQuote(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Get the quote of the day
        DailyQuote quote = getQuoteOfTheDay();

        // Set to correct channelID
        quote.setChannelID(envVars.getChannelGeneral());

        // Only proceed if there are no errors
        if (quote.getError() == null) {

            // Post quote to Slack
            SlackResponse msgResponse = new GeneralFunctions().postSlackMessage(SLACK_MSG_URL, envVars.getTOKEN(), quote.toJson());

            // Check for errors and log them
            if (!msgResponse.isOk()) {
                logger.log(Level.WARNING, "Error: " + msgResponse.getError());
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            // Check for warnings and log them
            if (msgResponse.getWarning() != null && !msgResponse.getWarning().isEmpty()) {
                logger.log(Level.WARNING, "Warning : " + msgResponse.getWarning());
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
        } else {

            // Log error and return status code 503
            logger.log(Level.WARNING, "Error " + quote.getError().toString());
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

    }

    // sendGithubUser sends the github user to the channel
    private void sendGithubUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("text");

        if (username == null || username.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Get the quote of the day
        GithubUser githubUser = getGithubUser(username);

        // Set to correct channelID
        githubUser.setChannelID(envVars.getChannelGeneral());

        // Only proceed if there are no errors
        if (githubUser.getMessage() == null) {

            // Try to post message to slack
            SlackResponse msgResponse = new GeneralFunctions().postSlackMessage(SLACK_MSG_URL, envVars.getTOKEN(), githubUser.toJson());

            // Check for errors and log them
            if (!msgResponse.isOk()) {
                logger.log(Level.WARNING, "Classes.SlackResponse Error: " + msgResponse.getError());
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            // Check for warnings and log them
            if (msgResponse.getWarning() != null && !msgResponse.getWarning().isEmpty()) {
                logger.log(Level.WARNING, "Classes.SlackResponse Warning : " + msgResponse.getWarning());
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
        } else {

            // Log error and return status code 404
            logger.log(Level.WARNING, "Error " + githubUser.getMessage());
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private GithubUser getGithubUser(String username) {
        HttpResponse<JsonNode> response = null;

        try {
            // Get the github profile in json format
            response = Unirest.get("https://api.github.com/users/" + username)
                    .header("accept", "application/json")
                    .asJson();
        } catch (UnirestException e) {
            logger.log(Level.WARNING, "Classes.GithubUser Error : " + e.getMessage());
            return new GithubUser();
        }

        Gson g = new GsonBuilder().create();

        // Convert from json to class
        GithubUser user = g.fromJson(String.valueOf(response.getBody()), GithubUser.class);

        // Get repositories to user if they have any public
        if (user.getPublic_repos() > 0) {
            try {
                // Get the repositories in json format
                response = Unirest.get(user.getRepos_url())
                        .header("accept", "application/json")
                        .asJson();
            } catch (UnirestException e) {
                logger.log(Level.WARNING, "Classes.GithubUser Classes.Repository Error : " + e.getMessage());
                return new GithubUser();
            }

            // Get set the repositories to the user
            // I got the solution from this: https://stackoverflow.com/a/12384156/8883030
            user.setRepositories(g.fromJson(String.valueOf(response.getBody()), new TypeToken<List<Repository>>() {
            }.getType()));
        }

        return user;
    }

    // getQuoteOfTheDay returns the quote of the day in an object
    private DailyQuote getQuoteOfTheDay() {

        HttpResponse<JsonNode> response = null;
        try {

            // Get the quote in json format
            response = Unirest.get("https://quotes.rest/qod")
                    .header("accept", "application/json")
                    .asJson();
        } catch (UnirestException e) {
            logger.log(Level.WARNING, "Error : " + e.getMessage());
            return new DailyQuote();
        }

        // Convert from json to class
        return new GsonBuilder().create().fromJson(String.valueOf(response.getBody()), DailyQuote.class);
    }
}
