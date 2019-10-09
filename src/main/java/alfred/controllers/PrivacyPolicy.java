package alfred.controllers;

import alfred.models.general.EnvVars;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PrivacyPolicy {

    @GetMapping("/api/slack/privacy")
    public ResponseEntity<String> privacy() throws IOException {

        // Get html body
        String body = new GeneralFunctions().getFileAsString("files/privacy.html");

        EnvVars envVars = new EnvVars();

        // Fill in dynamic variables
        body = body.replace("{{ websiteName }}", envVars.getWebsiteName()).
                replace("{{ url }}", envVars.getWebsiteURL()).
                replace("{{ email }}", envVars.getContactEmail());

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "text/html;charset=UTF-8");

        return new ResponseEntity<>(
                body, headers, HttpStatus.OK
        );
    }
}
