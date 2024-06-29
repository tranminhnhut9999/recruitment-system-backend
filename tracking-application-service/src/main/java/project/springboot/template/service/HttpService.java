package project.springboot.template.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.springboot.template.entity.common.ApiResponse;

import java.util.Map;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class HttpService {
    private final RestTemplate restTemplate;

    public <T> ApiResponse<T> get(String url, Map<String, Object> parameters, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        if (!jwtToken.isEmpty()) {
            headers.set("Authorization", "Bearer " + jwtToken);
        }
        HttpEntity<T> entity = new HttpEntity<>(headers);

        if (!parameters.isEmpty()) {
            String parameterEncoded = this.buildQueryParams(parameters);
            url += "?" + parameterEncoded;
        }
        ResponseEntity<ApiResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, ApiResponse.class);
        return response.getBody();
    }

    private String buildQueryParams(Map<String, Object> map) {
        if (map.isEmpty()) {
            return "";
        }
        // Using StringJoiner to build the query string
        StringJoiner stringJoiner = new StringJoiner("&");

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            stringJoiner.add(key + "=" + value);
        }

        return stringJoiner.toString();
    }
}
