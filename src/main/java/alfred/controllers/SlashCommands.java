package alfred.controllers;

import alfred.models.dailyquote.DailyQuote;
import alfred.models.db.DBquote;
import alfred.models.general.EnvVars;
import alfred.models.github.GithubUser;
import alfred.models.github.Repository;
import alfred.models.mannendovre.Mountain;
import alfred.models.slack.SlackResponse;
import alfred.services.DBquoteService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// alfred.Handlers.SlashCommands handles commands from slack-users (https://api.slack.com/slash-commands)

@RestController
public class SlashCommands {
    // Get logger
    private static final Logger logger = Logger.getLogger(SlashCommands.class.getName());

    private static final String SLACK_MSG_URL = "https://slack.com/api/chat.postMessage";

    @Autowired // Service for DB
    private DBquoteService dBquoteService;

    @PostMapping(value = "/slashcommands")
    public ResponseEntity<String> slashCommandsPOST(@RequestHeader("X-Slack-Signature") String xSlackHeader,
                                                    @RequestParam("channel_id") String channelID,
                                                    @RequestParam("command") String command,
                                                    @RequestParam("text") String text) {

        logger.log(Level.INFO, "POST request on /slashcommands");

        // Get environment variables
        EnvVars envVars = new EnvVars();

        // Remove all whitespace
        channelID = channelID.replaceAll("\\s+", "");
        command = command.replaceAll("\\s+", "");
        text = text.replaceAll("\\s+", "");

        // Check for valid command and respond accordingly
        if (channelID.equals(envVars.getChannelGeneral())) {
            switch (command) {
                case "/quote":
                    return sendQuote(envVars);
                case "/github":
                    return sendGithubUser(text, envVars);
                case "/mannen":
                    return mannenFallen(envVars);
                case "/dovre":
                    return dovreFallen(envVars);
                default:
                    logger.log(Level.WARNING, "The command '" + command + "' is not valid");
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<String> mannenFallen(EnvVars envVars) {
        Mountain mannen = getMountainStatus("https://www.harmannenfalt.no/api");

        if (mannen != null) {
            mannen.setChannelID(envVars.getChannelGeneral());

            SlackResponse msgResponse = new GeneralFunctions().postSlackMessage(SLACK_MSG_URL, envVars.getTOKEN(), mannen.toJson("Mannen"));

            // Check for errors and log them
            if (!msgResponse.isOk()) {
                logger.log(Level.WARNING, "MannenFallen Error: " + msgResponse.getError());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Check for warnings and log them
            if (msgResponse.getWarning() != null && !msgResponse.getWarning().isEmpty()) {
                logger.log(Level.WARNING, "MannenFallen Warning : " + msgResponse.getWarning());
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.log(Level.WARNING, "something went wrong with getting mannen");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<String> dovreFallen(EnvVars envVars) {
        Mountain dovre = getMountainStatus("https://www.hardovrefalt.no/api");

        if (dovre != null) {
            dovre.setChannelID(envVars.getChannelGeneral());

            SlackResponse msgResponse = new GeneralFunctions().postSlackMessage(SLACK_MSG_URL, envVars.getTOKEN(), dovre.toJson("Dovre"));

            // Check for errors and log them
            if (!msgResponse.isOk()) {
                logger.log(Level.WARNING, "DovreFallen Error: " + msgResponse.getError());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Check for warnings and log them
            if (msgResponse.getWarning() != null && !msgResponse.getWarning().isEmpty()) {
                logger.log(Level.WARNING, "DovreFallen Warning : " + msgResponse.getWarning());
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.log(Level.WARNING, "something went wrong with getting dovre");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
    private ResponseEntity<String> sendQuote(EnvVars envVars) {

        // Try to get the quote of the day
        DailyQuote quote = null;
        try {
            quote = getQuoteOfTheDay();
        } catch (Exception e) {

            // Log error and return correct response
            logger.log(Level.WARNING, "GetQuoteOfTheDay Error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Set to correct channelID
        quote.setChannelID(envVars.getChannelGeneral());

        // Only proceed if there are no errors
        if (quote.getError() == null) {

            // Post quote to Slack
            SlackResponse msgResponse = new GeneralFunctions().postSlackMessage(SLACK_MSG_URL, envVars.getTOKEN(), quote.toJson());

            // Check for errors and log them
            if (!msgResponse.isOk()) {
                logger.log(Level.WARNING, "Error: " + msgResponse.getError());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Check for warnings and log them
            if (msgResponse.getWarning() != null && !msgResponse.getWarning().isEmpty()) {
                logger.log(Level.WARNING, "Warning : " + msgResponse.getWarning());
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }

            return new ResponseEntity<>(HttpStatus.OK);

        } else {

            // Log error and return status code 500
            logger.log(Level.WARNING, "Error " + quote.getError().toString());
            //resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
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
            SlackResponse msgResponse = new GeneralFunctions().postSlackMessage(SLACK_MSG_URL, envVars.getTOKEN(), githubUser.toJson());

            // Check for errors and log them
            if (!msgResponse.isOk()) {
                logger.log(Level.WARNING, "alfred.Classes.SlackResponse Error: " + msgResponse.getError());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Check for warnings and log them
            if (msgResponse.getWarning() != null && !msgResponse.getWarning().isEmpty()) {
                logger.log(Level.WARNING, "alfred.Classes.SlackResponse Warning : " + msgResponse.getWarning());
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } else {

            // Log error and return status code 404
            logger.log(Level.WARNING, "Error " + githubUser.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
            logger.log(Level.WARNING, "alfred.Classes.GithubUser Error : " + e.getMessage());
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
                logger.log(Level.WARNING, "alfred.Classes.GithubUser alfred.Classes.Repository Error : " + e.getMessage());
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
    private DailyQuote getQuoteOfTheDay() throws Exception {

        List<DBquote> dBquotes = dBquoteService.list();

        DBquote dBquote = null;
        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");

        // Check if there already is a quote from today in the db
        for (DBquote quote : dBquotes) {
            if (fmt.format(quote.getQuote().getDate()).equals(fmt.format(new Date()))) {
                dBquote = quote;
            }
        }

        // Return the quote from db if it's from today
        if (dBquote != null) {
            logger.log(Level.INFO, "Using quote of the day from DB");
            return new DailyQuote(dBquote);
        }


        // If the quote wasn't in db, then try to get the quote from external API

        // Get the quote in json format
        HttpResponse<JsonNode> response = Unirest.get("https://quotes.rest/qod")
                .header("accept", "application/json")
                .asJson();

        // Convert response to DailyQuote Object
        DailyQuote dailyQuote = new GsonBuilder().create().fromJson(String.valueOf(response.getBody()), DailyQuote.class);

        // Convert DailyQuote to DBquote to add to db
        dBquote = new DBquote(dailyQuote);

        // Add to db
        dBquoteService.add(dBquote);

        // Log events
        logger.log(Level.INFO, String.format("New DailyQuote is added to DB with ID %d", dBquote.getId()));
        logger.log(Level.INFO, "Using quote of the day from external API");

        return dailyQuote;
    }
}
