package com.porodkin.timeloggerbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
public class TimeLoggerApiRestClient {

    private static final Logger log = LoggerFactory.getLogger(TimeLoggerApiRestClient.class);
    private final RestTemplate restTemplate;

    public TimeLoggerApiRestClient(RestTemplate timeloggerRestTemplate) {
        this.restTemplate = timeloggerRestTemplate;
    }

    public void sendStartRequest(Long userId) {
        try {
            StartSessionEntityRequest startSessionEntityRequest = new StartSessionEntityRequest();
            startSessionEntityRequest.setUserId(userId);
            startSessionEntityRequest.setWorkSession(OffsetDateTime.now(ZoneId.of("Canada/Newfoundland")));

            restTemplate.postForEntity("/api/v1/work-session/start", startSessionEntityRequest, StartSessionEntityResponse.class);
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
    }

    public void sendEndRequest(Long userId) {

        ResponseEntity<CurrentSessionEntityResponse> forEntity =
                restTemplate.getForEntity("/api/v1/work-session/".concat(String.valueOf(userId)).concat("/current"), CurrentSessionEntityResponse.class);

        String sessionId;
        if (forEntity.getStatusCode().is2xxSuccessful()) {
            sessionId = forEntity.getBody().getWorkSessionId();
        } else {
            log.error(forEntity.getBody().getMessage());
            throw new RestClientException(forEntity.getStatusCode().toString());
        }

        EndSessionEntityRequest request = new EndSessionEntityRequest();
        request.setUserId(userId);
        request.setSessionId(sessionId);
        request.setEndTime(OffsetDateTime.now(ZoneId.of("Canada/Newfoundland")).toOffsetTime());
        restTemplate.put("/api/v1/work-session/end", request);
    }
}
