import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Before;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class TestIndex {

    @Before
    public void setUp() throws Exception {

    }

    // I'm still figuring out how to write test for this the correct way.
    @Test
    public void TestDoGet() throws ServletException, IOException, UnirestException {

        /* TODO : The test passes this way and gives 100% test coverage, and the server doesn't need to run, but it's hardcoded
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(pw);
        when(response.getStatus()).thenReturn(202);

        Index index = new Index();
        index.doGet(request, response);

        assertEquals(202, response.getStatus());
         */

        // TODO : get the URLr dynamically
        // TODO : this method actually tests the function, but gives 0% test coverage and the server has to run
        HttpResponse<String> response = Unirest.get("http://23.97.209.118/").asString();

        // Check that response is not null
        assertTrue("Index: Failed getting response", (response != null));

        // Check status code
        assertEquals("Index: failed asserting status codes are equal",
                HttpServletResponse.SC_ACCEPTED, response.getStatus());

        // Check content-type
        assertTrue("Index: Failed asserting correct content-type",
                (response.getHeaders().get("Content-Type").toString().contains("text/html")));
    }
}
