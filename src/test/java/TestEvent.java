import alfred.Main;
import alfred.controllers.Event;
import alfred.models.slack.EventRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(Event.class)
@ContextConfiguration(classes = {Main.class})
public class TestEvent {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void postEvent() throws Exception {
        EventRequest request = new EventRequest("TEST-token", "TEST-challenge", "TEST-token");
        String xSlackSignature = "TEST-xSlackSignature";

        Gson g = new GsonBuilder().create();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/slack/event")                             // Post to correct page
                        .content(g.toJson(request))                             // Pass correct request body as json
                        .header("X-SLack-Signature", xSlackSignature)    // Add correct header
                        .contentType(MediaType.APPLICATION_JSON)                // Establish content type
                        .accept(MediaType.TEXT_PLAIN))                          // Expect body in plain text
                //.andDo(print())                                               // Uncomment for debugging
                .andExpect(content().string(request.getChallenge()))            // Expect body to be challenge from request
                .andExpect(status().isOk());                                    // Expect status to be OK - 200
    }

}