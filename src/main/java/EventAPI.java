import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.github.cdimascio.dotenv.Dotenv;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/event")
public class EventAPI extends HttpServlet {
    private static final Logger logger = Logger.getLogger(EventAPI.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO : get path to .env file more dynamically
        Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\BredeKlausen\\OneDrive - ITverket AS\\Dokumenter\\Kompetanseheving\\Java\\alfred\\").load();

        // Get the quote of the day
        QuoteOfTheDay quote = getQuoteOfTheDay(dotenv.get("CHANNEL-ID-GENERAL"));

        // Only proceed if there are no errors
        if (quote.getError() == null) {
            HttpResponse<JsonNode> response = null;
            Gson g = new GsonBuilder().create();
            POSTMessageResponse msgResponse;


            // Post request to send message
            try {
                response = Unirest.post("https://slack.com/api/chat.postMessage")
                        .header("content-type", "application/json")
                        .header("Authorization", "Bearer " + dotenv.get("SLACK-BOT-TOKEN"))
                        .body(quote.toJson())
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            // Convert received response to object of POSTMessageResponse
            msgResponse = g.fromJson(String.valueOf(response.getBody()), POSTMessageResponse.class);

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
            logger.log(Level.WARNING, "Error " + quote.getError().toString());
            resp.getWriter().write("<h1>Error " + HttpServletResponse.SC_SERVICE_UNAVAILABLE + "</h1><br><p>" + quote.getError().getMessage() + "</p>");
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get content
        StringBuilder jb = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error: " + e);
        }

        // Convert json string to object
        if (req.getContentType().toLowerCase().contains("json")) {
            SlackEvent event = new GsonBuilder().create().fromJson(jb.toString(), SlackEvent.class);

            System.out.println(event.toString());
        }
    }

    private QuoteOfTheDay getQuoteOfTheDay(String channelID) {

        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get("https://quotes.rest/qod")
                    .header("accept", "application/json")
                    .asJson();
        } catch (UnirestException e) {
            logger.log(Level.WARNING, "Error : " + e.getMessage());
            return new QuoteOfTheDay();
        }

        Gson g = new GsonBuilder().create();

        return g.fromJson(String.valueOf(response.getBody()), QuoteOfTheDay.class);

    }
}
