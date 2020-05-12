package ac.project.Robal;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

    public static byte[] convertObjectToJsonBytes(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper.writeValueAsBytes(object);
    }

	public static HttpHeaders getAuthorizationBasic(String email, String password) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(email, password);
		return headers;
	}
	public static HttpHeaders getAuthorizationOwner(String email, String password) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(email, password);
		return headers;
	}

}
