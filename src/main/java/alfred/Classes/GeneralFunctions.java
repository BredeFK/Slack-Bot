package alfred.Classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class GeneralFunctions {

    public GeneralFunctions() {

    }

    public String getBody(HttpServletRequest req) throws IOException {

        StringBuilder jb = new StringBuilder();
        String line = null;
        BufferedReader reader = req.getReader();


        while ((line = reader.readLine()) != null) {
            jb.append(line);
        }

        return jb.toString();
    }

    public SlackResponse postSlackMessage(String postURL, String token, String message) {
        SlackResponse slackResponse = new SlackResponse();
        HttpResponse<JsonNode> response = null;
        Gson g = new GsonBuilder().create();
        SlackResponse msgResponse;

        // Try to post request to send message
        try {

            response = Unirest.post(postURL)
                    .header("content-type", "application/json; charset=utf-8")
                    .header("Authorization", String.format("Bearer %s", token))
                    .body(message)
                    .asJson();
        } catch (UnirestException e) {

            // Get error message and return response if error occurred
            slackResponse.setOk(false);
            slackResponse.setError(e.getMessage());
            return slackResponse;
        }

        // Convert received response to object of SlackResponse
        slackResponse = g.fromJson(String.valueOf(response.getBody()), SlackResponse.class);

        return slackResponse;
    }
}
