package sample.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import sample.dto.OperatorAuthenticationResultDto;
import sample.dto.OperatorCredentialsDto;

public class AuthenticatorImpl implements Authenticator {


    private static final String AUTHENTICATION_URL = "https://localhost:8080/verify_operator_credentials";

    private final RestTemplate restTemplate;

    public AuthenticatorImpl(){
        restTemplate = new RestTemplate();
    }

    @Override
    public void authenticate(OperatorCredentialsDto operatorCredentialsDto, AuthenticationResultHandler authenticationResultHandler) {
        Runnable authenticationTask = () -> {
            processAuthentication(operatorCredentialsDto, authenticationResultHandler);
        };
        Thread authenticationThread = new Thread(authenticationTask);
        authenticationThread.setDaemon(true);
        authenticationThread.start();
    }


    public void processAuthentication( OperatorCredentialsDto operatorCredentialsDto, AuthenticationResultHandler authenticationResultHandler) {

        ResponseEntity<OperatorAuthenticationResultDto> responseEntity = restTemplate.postForEntity(AUTHENTICATION_URL, operatorCredentialsDto, OperatorAuthenticationResultDto.class);
        authenticationResultHandler.handle(responseEntity.getBody());
    }

}
