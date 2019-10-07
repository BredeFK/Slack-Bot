package alfred.controllers;

import alfred.models.general.EnvVars;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class QuoteComponent {

    private static final Logger logger = Logger.getLogger(QuoteComponent.class.getName());

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");

    // 60 sec, 60min, 24h, to milliseconds
    private static final int day = 60 * 60 * 24 * 1000;

    private EnvVars envVars = new EnvVars();


    //@Scheduled(fixedRate = 5000)
    public void sendQuote() {

        // 500 error, service is null
        
        // Get and send quote
        ResponseEntity<String> responseEntity = new SlashCommands().sendQuote(envVars);

        // Check if it was sent successfully
        if (responseEntity.getStatusCode() == HttpStatus.OK)
            logger.log(Level.INFO, "Automatically sent quote at: {0}", dateFormat.format(new Date()));
        else
            logger.log(Level.WARNING, "Something went wrong with automatically sending the quote. Error : {0}", responseEntity.getStatusCodeValue());
    }
}
