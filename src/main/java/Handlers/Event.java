package Handlers;

import Classes.GeneralFunctions;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

// Handlers.Event handles events from slack (https://api.slack.com/events-api)
@WebServlet("/event")
public class Event extends HttpServlet {
    private static final Logger logger = Logger.getLogger(Event.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.INFO, "POST request on /event");

        if (!req.getHeader("X-Slack-Signature").isEmpty()) {

            // Get body from request
            String body = new GeneralFunctions().getBody(req);

            try {
                JSONObject jsonObject = new JSONObject(body);

                if (!jsonObject.isNull("challenge")) {
                    resp.setContentType("text/plain;charset=utf-8");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write(jsonObject.getString("challenge"));
                    return;
                }
            } catch (JSONException e) {
                // crash and burn
                logger.log(Level.WARNING, e.getMessage());
                throw new IOException("Error parsing JSON request string");
            }
        }

        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
