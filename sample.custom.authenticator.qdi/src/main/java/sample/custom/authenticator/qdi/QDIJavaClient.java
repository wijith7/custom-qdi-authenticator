package sample.custom.authenticator.qdi;

public class QDIJavaClient implements QDIClient {

    @Override
    public boolean qdiAuthenticator(String username, String password) {

        if ("foo".equals(username) && "pass".equals(password)) {
            return true;
        }
        return false;
    }
}
