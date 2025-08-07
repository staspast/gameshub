package com.wsiz.gameshub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderService {

    public void sendEmail(String emailTo, String subject, String text){

        final MultiValueMap<String, String> sendMailParams = new LinkedMultiValueMap<>();
        sendMailParams.add("from", "noreply@gamehub.com");
        sendMailParams.add("to", emailTo);
        sendMailParams.add("subject", subject);
        sendMailParams.add("text", text);

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(sendMailParams, createHeaders("api", "ed663114dee35b0b63c68906bd1edb79-e49cc42c-599e411e"));

        ResponseEntity<String> response = restTemplate.postForEntity("https://api.mailgun.net/v3/sandboxefc7996723fc47c09b83553156006ed1.mailgun.org/messages",
            entity, String.class);

        log.info(response.getBody());
    }

    private HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
            set( "Content-Type", "application/x-www-form-urlencoded" );
        }};
    }
}
