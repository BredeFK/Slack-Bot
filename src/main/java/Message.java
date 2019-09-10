import DailyQuote.DailyQuote;
import com.google.gson.Gson;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/message")
public class Message extends HttpServlet {
    private static final Logger logger = Logger.getLogger(Message.class.getName());

    // Get environment variables
    private EnvVars envVars = new EnvVars();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.INFO, "GET method was attempted");
        resp.getWriter().write("<h1>Get method does nothing, enjoy this picture of a dog</h1>" +
                "<br><img src=\"https://i.pinimg.com/originals/7d/d2/fe/7dd2fe7c208bff009ed16fa3803a716a.jpg\" alt=\"Cute dog\" style=\"max-width: 520px; max-height: 520px\">" +
                "<br><a href=\"https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwj3o4uA5bTkAhUewsQBHYLoA6IQjRx6BAgBEAQ&url=https%3A%2F%2Fwww.pinterest.com%2Fpin%2F296182112979951852%2F&psig=AOvVaw2WT22zk7BJJ8BBN10K_9tv&ust=1567604766799294\">Source here</a>");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.INFO, "POST method was attempted");

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
                default:
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    break;
            }
        } else {
            logger.log(Level.WARNING, "Not authorized");
        }
    }

    // sendQuote Sends the daily quote to the channel
    private void sendQuote(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Get the quote of the day
        DailyQuote quote = getQuoteOfTheDay();

        // Set to correct channelID
        quote.setChannelID(envVars.getChannelGeneral());

        // Only proceed if there are no errors
        if (quote.getError() == null) {
            HttpResponse<JsonNode> response = null;
            Gson g = new GsonBuilder().create();
            SlackResponse msgResponse;


            // Post request to send message
            try {

                // Try to send message with POST
                response = Unirest.post("https://slack.com/api/chat.postMessage")
                        .header("content-type", "application/json; charset=utf-8")
                        .header("Authorization", "Bearer " + envVars.getTOKEN())
                        .body(quote.toJson())
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            // Convert received response to object of POSTMessageResponse
            msgResponse = g.fromJson(String.valueOf(response.getBody()), SlackResponse.class);

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

            // Log status code
            logger.log(Level.FINE, "Status code: " + response.getStatus());
        } else {

            // Give error to user and logger and set status
            logger.log(Level.WARNING, "Error " + quote.getError().toString());
            resp.getWriter().write("<h1>Error " + HttpServletResponse.SC_SERVICE_UNAVAILABLE + "</h1><br><p>" + quote.getError().getMessage() + "</p>");
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

    }

    // sendGithubUser sends the github user to the channel
    private void sendGithubUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("text");

        if(username == null || username.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Get the quote of the day
        GithubUser githubUser = getGithubUser(username);

        // Set to correct channelID
        githubUser.setChannelID(envVars.getChannelGeneral());

        logger.log(Level.INFO, githubUser.toJson());



        // Only proceed if there are no errors
        if (githubUser.getMessage() == null) {
            HttpResponse<JsonNode> response = null;
            Gson g = new GsonBuilder().create();
            SlackResponse msgResponse;


            // Post request to send message
            try {
                // Try to send message with POST
                response = Unirest.post("https://slack.com/api/chat.postMessage")
                        .header("content-type", "application/json; charset=utf-8")
                        .header("Authorization", "Bearer " + envVars.getTOKEN())
                        .body(githubUser.toJson())
                        .asJson();
            } catch (UnirestException e) {
                logger.log(Level.WARNING, "Something went wrong with sending githubuser to channel: " + e.getMessage());
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            // Convert received response to object of POSTMessageResponse
            msgResponse = g.fromJson(String.valueOf(response.getBody()), SlackResponse.class);

            // Check for errors and log them
            if (!msgResponse.isOk()) {
                logger.log(Level.WARNING, "SlackResponse Error: " + msgResponse.getError());
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            // Check for warnings and log them
            if (msgResponse.getWarning() != null && !msgResponse.getWarning().isEmpty()) {
                logger.log(Level.WARNING, "SlackResponse Warning : " + msgResponse.getWarning());
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);

            // Log status code
            logger.log(Level.FINE, "Status code: " + response.getStatus());
        } else {

            // Give error to user and logger and set status
            logger.log(Level.WARNING, "Error " + githubUser.getMessage());
            resp.getWriter().write("<h1>Error " + HttpServletResponse.SC_SERVICE_UNAVAILABLE + "</h1><br><p>" + githubUser.getMessage() + "</p>");
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }


    }

    private GithubUser getGithubUser(String username) {
        HttpResponse<JsonNode> response = null;

        try {
            // Get the quote in json format
            response = Unirest.get("https://api.github.com/users/" + username)
                    .header("accept", "application/json")
                    .asJson();
        } catch (UnirestException e) {
            logger.log(Level.WARNING, "Error : " + e.getMessage());
            return new GithubUser();
        }

        // Convert from json to class
        return new GsonBuilder().create().fromJson(String.valueOf(response.getBody()), GithubUser.class);
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
