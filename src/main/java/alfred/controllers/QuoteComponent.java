package alfred.controllers;

import alfred.models.general.EnvVars;
import alfred.services.DailyQuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class QuoteComponent {

    // Logger
    private static final Logger logger = Logger.getLogger(QuoteComponent.class.getName());

    // Date format
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss Z");

    // 60 sec, 60min, 24h, to milliseconds
    private static final int day = 60 * 60 * 24 * 1000;

    // Environmental variables
    private EnvVars envVars = new EnvVars();

    @Autowired // Service for DB
    private DailyQuoteService dailyQuoteService;


    @Scheduled(fixedRate = day)
    public void sendQuote() {

        // Get and send quote
        ResponseEntity<String> responseEntity = new SlashCommands().sendQuote(envVars, dailyQuoteService);

        // Check if it was sent successfully
        if (responseEntity.getStatusCode() == HttpStatus.OK)
            logger.log(Level.INFO, "Automatically sent quote at: {0}", dateFormat.format(new Date()));
        else
            logger.log(Level.WARNING, "Something went wrong with automatically sending the quote. Error : {0}", responseEntity.getStatusCodeValue());
    }
}
