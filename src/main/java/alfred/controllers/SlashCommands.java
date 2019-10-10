package alfred.controllers;

import alfred.models.dailyquote.DailyQuote;
import alfred.models.general.EnvVars;
import alfred.models.github.GithubUser;
import alfred.models.github.Repository;
import alfred.models.mannendovre.Mountain;
import alfred.models.slack.SlackResponse;
import alfred.services.DailyQuoteService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// SlashCommands handles commands from slack-users (https://api.slack.com/slash-commands)

@RestController
public class SlashCommands {
    // Get logger
    private static final Logger logger = Logger.getLogger(SlashCommands.class.getName());

    // API urls
    private static final String SLACK_MSG_URL = "https://slack.com/api/chat.postMessage";
    private static final String MANNEN_URL = "https://www.harmannenfalt.no/api";
    private static final String DOVRE_URL = "https://www.hardovrefalt.no/api";
    private static final String GITHUB_USER_URL = "https://api.github.com/users/";
    private static final String QUOTE_URL = "https://quotes.rest/qod";

    private static final String ACCEPT = "accept";

    @Autowired // Service for DB
    private DailyQuoteService dailyQuoteService;

    @PostMapping(value = "/api/slack/slashcommands")
    public ResponseEntity<String> slashCommandsPOST(@RequestHeader("X-Slack-Signature") String slackSignature,
                                                    @RequestHeader("X-Slack-Request-Timestamp") long timestamp,
                                                    @RequestBody String body,
                                                    @RequestParam("channel_id") String channelID,
                                                    @RequestParam("command") String command,
                                                    @RequestParam("text") String text,
                                                    HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, InvalidKeyException {

        logger.log(Level.INFO, "POST request on {0}", httpServletRequest.getRequestURL());

        // Get environment variables
        EnvVars envVars = new EnvVars();

        // Check if if the request is authenticated
        String errorMessage = new GeneralFunctions().authenticatedRequest(timestamp, body, slackSignature);
        if (!errorMessage.isEmpty()) {
            logger.log(Level.WARNING, errorMessage);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


        // Remove all whitespace
        channelID = channelID.replaceAll("\\s+", "");
        command = command.replaceAll("\\s+", "");
        text = text.replaceAll("\\s+", "");

        // Check for valid command and respond accordingly
        if (channelID.equals(envVars.getChannelGeneral())) {
            switch (command) {
                case "/quote":
                    return sendQuote(envVars, dailyQuoteService);
                case "/github":
                    return sendGithubUser(text, envVars);
                case "/mannen":
                    return mountainFallen(envVars, MANNEN_URL, "Mannen");
                case "/dovre":
                    return mountainFallen(envVars, DOVRE_URL, "Dovre");
                default:
                    logger.log(Level.WARNING, "The command \"{0}\" is not valid", command);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    private ResponseEntity<String> mountainFallen(EnvVars envVars, String url, String name) {
        Mountain mountain = getMountainStatus(url);

        if (mountain != null) {
            mountain.setChannelID(envVars.getChannelGeneral());

            SlackResponse msgResponse = new GeneralFunctions().postSlackMessage(SLACK_MSG_URL, envVars.getSlackToken(), mountain.toJson(name));

            // Check for errors and log them
            if (!msgResponse.isOk()) {
                logger.log(Level.WARNING, "Mountain Error: {0}", msgResponse.getError());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Check for warnings and log them
            if (msgResponse.getWarning() != null && !msgResponse.getWarning().isEmpty()) {
                logger.log(Level.WARNING, "Mountain Warning: {0}", msgResponse.getWarning());
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.log(Level.WARNING, "Something went wrong with getting {0}", name);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Mountain getMountainStatus(String URL) {
        HttpResponse<JsonNode> response = null;
        try {

            // Get the quote in json format
            response = Unirest.get(URL)
                    .header(ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .asJson();
        } catch (UnirestException e) {
            logger.log(Level.WARNING, "getMountainStatus Error : {0}", e.getMessage());
            return new Mountain();
        }

        // Convert from json to class
        return new GsonBuilder().create().fromJson(String.valueOf(response.getBody()), Mountain.class);

    }

    // sendQuote Sends the daily quote to the channel
    public ResponseEntity<String> sendQuote(EnvVars envVars, DailyQuoteService dailyQuoteService) {

        // Try to get the quote of the day
        DailyQuote quote = null;
        try {
            quote = getQuoteOfTheDay(dailyQuoteService);
        } catch (Exception e) {

            // Log error and return correct response
            logger.log(Level.WARNING, "GetQuoteOfTheDay Error: {0}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Set to correct channelID
        quote.setChannelID(envVars.getChannelGeneral());

        // Only proceed if there are no errors
        if (quote.getError() == null) {

            // Post quote to Slack
            SlackResponse msgResponse = new GeneralFunctions().postSlackMessage(SLACK_MSG_URL, envVars.getSlackToken(), quote.toJson());

            // Check for errors and log them
            if (!msgResponse.isOk()) {
                logger.log(Level.WARNING, "SlackResponse Error: {0}", msgResponse.getError());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Check for warnings and log them
            if (msgResponse.getWarning() != null && !msgResponse.getWarning().isEmpty()) {
                logger.log(Level.WARNING, "SlackResponse Warning : {0}", msgResponse.getWarning());
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }

            return new ResponseEntity<>(HttpStatus.OK);

        } else {

            // Log error and return status code 500
            logger.log(Level.WARNING, "Quote Error: {0}", quote.getError());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // sendGithubUser sends the github user to the channel
    private ResponseEntity<String> sendGithubUser(String username, EnvVars envVars) {

        // Get the quote of the day
        GithubUser githubUser = getGithubUser(username);

        // Set to correct channelID
        githubUser.setChannelID(envVars.getChannelGeneral());

        // Only proceed if there are no errors
        if (githubUser.getMessage() == null) {

            // Try to post message to slack
            SlackResponse msgResponse = new GeneralFunctions().postSlackMessage(SLACK_MSG_URL, envVars.getSlackToken(), githubUser.toJson());

            // Check for errors and log them
            if (!msgResponse.isOk()) {
                logger.log(Level.WARNING, "SlackResponse Error: {0}", msgResponse.getError());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Check for warnings and log them
            if (msgResponse.getWarning() != null && !msgResponse.getWarning().isEmpty()) {
                logger.log(Level.WARNING, "SlackResponse Warning : {0}", msgResponse.getWarning());
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } else {

            // Log error and return status code 404
            logger.log(Level.WARNING, "GitHubUser Error: {0}", githubUser.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private GithubUser getGithubUser(String username) {

        HttpResponse<JsonNode> response = null;

        try {
            // Get the github profile in json format
            response = Unirest.get(GITHUB_USER_URL + username)
                    .header(ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .asJson();
        } catch (UnirestException e) {
            logger.log(Level.WARNING, "GithubUser Error : {0}", e.getMessage());
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
                        .header(ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .asJson();
            } catch (UnirestException e) {
                logger.log(Level.WARNING, "GithubUser Repository Error : {0}", e.getMessage());
                return new GithubUser();
            }

            // Get set the repositories to the user
            // I got the solution from this: https://stackoverflow.com/a/12384156/8883030
            ArrayList<Repository> repositories = g.fromJson(String.valueOf(response.getBody()), new TypeToken<List<Repository>>() {
            }.getType());

            user.setRepositories(repositories);
        }

        return user;
    }

    // getQuoteOfTheDay returns the quote of the day in an object
    public DailyQuote getQuoteOfTheDay(DailyQuoteService dailyQuoteService) throws Exception {

        List<DailyQuote> dailyQuotes = dailyQuoteService.list();

        DailyQuote quote = null;
        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");

        // Check if there already is a quote from today in the db
        for (DailyQuote dailyQuote : dailyQuotes) {
            if (fmt.format(dailyQuote.getContents().getSingleQuote().getDate()).equals(fmt.format(new Date()))) {
                quote = dailyQuote;
            }
        }

        // Return the quote from db if it's from today
        if (quote != null) {
            logger.log(Level.INFO, "Using quote of the day from DB");
            return quote;
        }


        // If the quote wasn't in db, then try to get the quote from external API

        // Get the quote in json format
        HttpResponse<JsonNode> response = Unirest.get(QUOTE_URL)
                .header(ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .asJson();

        // Convert response to DailyQuote Object
        DailyQuote dailyQuote = new GsonBuilder().create().fromJson(String.valueOf(response.getBody()), DailyQuote.class);

        // Add to db and get new id
        long id = dailyQuoteService.add(dailyQuote);

        // Log events
        logger.log(Level.INFO, "New DailyQuote is added to DB with ID {0} | Using quote of the day from external API", id);
        return dailyQuote;
    }
}
