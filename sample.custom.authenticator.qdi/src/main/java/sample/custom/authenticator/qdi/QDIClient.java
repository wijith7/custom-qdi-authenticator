package sample.custom.authenticator.qdi;

import java.io.Serializable;

public interface QDIClient extends Serializable {

    boolean qdiAuthenticator(String username, String password);
}
