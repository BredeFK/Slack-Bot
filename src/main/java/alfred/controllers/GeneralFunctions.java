package alfred.controllers;

import alfred.models.general.EnvVars;
import alfred.models.slack.SlackResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class GeneralFunctions {
    private EnvVars envVars;

    public GeneralFunctions() {
        envVars = new EnvVars();
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

    // Verify that the request is from an authenticated source (https://api.slack.com/docs/verifying-requests-from-slack)
    public String authenticatedRequest(long timestamp, String body, String slackSignature) throws NoSuchAlgorithmException, InvalidKeyException {
        // Get relevant dates
        Date slackDate = new Date(timestamp * 1000);
        Date now = new Date();

        long secondsGone = (now.getTime() - slackDate.getTime()) / 1000;

        // Give error if request is older than 1min (60 seconds)
        if (secondsGone > 60) {
            return String.format("Request is too old, it's %d seconds old", secondsGone);
        }

        String temp = String.format("v0:%d:%s", timestamp, body);

        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(envVars.getSlackSigningSecret().getBytes(), "HmacSHA256");
        sha256HMAC.init(secretKeySpec);

        String mySignature = "v0=" + Hex.encodeHexString(sha256HMAC.doFinal(temp.getBytes()));

        // Give error if the signature is incorrect
        if (!mySignature.equals(slackSignature)) {
            return "X-Slack-Signature is incorrect";
        }

        return "";
    }
}
