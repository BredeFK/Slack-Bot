package alfred.controllers;

import alfred.models.slack.SlackResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeneralFunctions {

    public GeneralFunctions() {

    }

    public SlackResponse postSlackMessage(String postURL, String token, String message) {
        SlackResponse slackResponse = new SlackResponse();
        HttpResponse<JsonNode> response = null;
        Gson g = new GsonBuilder().create();

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

    public String getFileAsString(String fileName) throws IOException {

        // Get file path
        Path filePath = Paths.get(fileName);

        // Get file
        File file = new File(filePath.toString());

        // Check if file exists
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }
}
