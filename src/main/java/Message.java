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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get environment variables
        EnvVars envVars = new EnvVars();

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
                        .header("content-type", "application/json")
                        .header("Authorization", "Bearer " + envVars.getTOKEN())
                        .body(quote.toJson())
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            // Convert received response to object of POSTMessageResponse
            msgResponse = g.fromJson(String.valueOf(response.getBody()), SlackResponse.class);

            // Check for errors and log them
            if (!msgResponse.isOk()) {
                logger.log(Level.WARNING, "Error: " + msgResponse.getError());
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            // Check for warnings and log them
            if (msgResponse.getWarning() == null || !msgResponse.getWarning().isEmpty()) {
                logger.log(Level.WARNING, "Warning : " + msgResponse.getWarning());
            }

            // Log status code
            logger.log(Level.FINE, "Status code: " + response.getStatus());
        } else {

            // Give error to user and logger and set status
            logger.log(Level.WARNING, "Error " + quote.getError().toString());
            resp.getWriter().write("<h1>Error " + HttpServletResponse.SC_SERVICE_UNAVAILABLE + "</h1><br><p>" + quote.getError().getMessage() + "</p>");
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.INFO, "POST method was attempted");
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
