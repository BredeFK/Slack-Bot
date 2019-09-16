import Handlers.Index;
import org.junit.Before;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestIndex {

    @Before
    public void setUp() throws Exception {

    }

    // I haven't written any tests for HTTPServlet before so I had to google a ton of errors and different ways of doing this.
    // The following sources helped me the most:
    // https://examples.javacodegeeks.com/core-java/junit/junit-httpservletrequest-example/
    // http://johannesbrodwall.com/2009/10/24/testing-servlets-with-mockito/
    @Test
    public void TestDoGet() throws ServletException, IOException {

        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(pw);

        Index index = new Index();
        index.doGet(request, response);


        //assertEquals("text/html", response.getContentType());
        //assertEquals(HttpServletResponse.SC_ACCEPTED, response.getStatus());

    }
}
