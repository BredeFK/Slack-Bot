package Handlers;

import Classes.InteractiveResponse;
import com.google.gson.GsonBuilder;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

// Handlers.Interactive handles interactivity from slack users (https://api.slack.com/messaging/interactivity#components)
@WebServlet("/interactive")
public class Interactive extends HttpServlet {
    private static final Logger logger = Logger.getLogger(Interactive.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.INFO, "POST request to /interactive");

        StringBuilder jb = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        try {

            // Reason I found out how to parse the request: http://www.herongyang.com/Encoding/URLEncoding-application-x-www-form-urlencoded-Java.html
            // Decode x-www-form-urlencoded and remove 'payload=' at the beginning (Convert to json string)
            String payload = URLDecoder.decode(jb.toString(), StandardCharsets.UTF_8).replace("payload=", "");

            // Convert json string to object of InteractiveResponse
            InteractiveResponse response = new GsonBuilder().create().fromJson(payload, InteractiveResponse.class);

            if(response.getType() == null || response.getType().isEmpty()){
                logger.log(Level.WARNING, "Error: Something went wrong from parsing 'x-www-form-urlencoded' to InterActiveResponse object");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } else{
                logger.log(Level.INFO, "'x-www-form-urlencoded' was parsed successfully");
                resp.setStatus(HttpServletResponse.SC_OK);
            }

        } catch (JSONException e) {
            // crash and burn
            logger.log(Level.WARNING, "Error parsing json: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
