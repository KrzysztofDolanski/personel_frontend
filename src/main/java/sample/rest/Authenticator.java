package sample.rest;

import sample.dto.OperatorCredentialsDto;

public interface Authenticator {

    void authenticate(OperatorCredentialsDto operatorCredentialsDTO, AuthenticationResultHandler authenticationResultHandler);
}
