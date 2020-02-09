package sample.custom.authenticator.qdi;

public class QDIServerClient implements QDIClient {

    @Override
    public boolean qdiAuthenticator(String username, String password) {

        return false;
    }
}
