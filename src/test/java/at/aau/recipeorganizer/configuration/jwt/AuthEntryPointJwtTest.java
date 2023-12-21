package at.aau.recipeorganizer.configuration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.security.core.AuthenticationException;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthEntryPointJwtTest {
    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @Test
    void testCommence() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new DelegatingServletOutputStream(outputStream));
        when(request.getServletPath()).thenReturn("/api");

        authEntryPointJwt.commence(request, response, authException);

        // Verify the response details
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Verify the content of the response body
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> expectedBody = new HashMap<>();
        expectedBody.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        expectedBody.put("error", "Unauthorized");
        expectedBody.put("message", authException.getMessage());
        expectedBody.put("path", "/api");

        assertEquals(objectMapper.writeValueAsString(expectedBody), outputStream.toString());
    }
}
