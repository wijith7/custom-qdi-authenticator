package sample.custom.authenticator.qdi;

import net.minidev.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class QDIMockClient implements QDIClient {

    @Override
    public boolean qdiAuthenticator(String username, String password) {

     return callWebService(username);

    }

    private boolean callWebService(String soapAction) {

       // String existingSpi = System.getProperty("javax.xml.ws.spi.Provider");
      //  System.setProperty("javax.xml.ws.spi.Provider","com.sun.xml.internal.ws.spi.ProviderImpl");

//  —JAX-WS code calls Webservices —

       // System.setProperty("javax.xml.ws.spi.Provider”, existingSpi");

        HelloWorldImplService helloService = new HelloWorldImplService();
        HelloWorld hello = helloService.getHelloWorldImplPort();

        return "1".equals(hello.getHelloWorldAsString(soapAction));

    }
}