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
            System.out.println("Checkpoint 1");
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error: " + e.getMessage());
        }

        try {

            // TODO : remove when error is found
            System.out.println("Headers: " + req.getHeaderNames());
            System.out.println("Content type: " + req.getContentType());
            System.out.println("Request to string: " + req.toString());
            System.out.println("Attributes: " + req.getAttributeNames());
            System.out.println("Parameters: " + req.getParameterNames());
            System.out.println("Body: " + jb.toString());

            InteractiveResponse response = new GsonBuilder().create().fromJson(jb.toString(), InteractiveResponse.class);
            logger.log(Level.INFO, response.toString());

        } catch (JSONException e) {
            // crash and burn
            logger.log(Level.WARNING, "Error parsing json: " + e.getMessage());
        }

        System.out.println("Checkpoint 3");
    }
}
