package sample.custom.authenticator.qdi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.AbstractApplicationAuthenticator;
import org.wso2.carbon.identity.application.authentication.framework.LocalApplicationAuthenticator;
import org.wso2.carbon.identity.application.authentication.framework.config.ConfigurationFacade;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.authentication.framework.exception.AuthenticationFailedException;
import org.wso2.carbon.identity.application.authentication.framework.exception.InvalidCredentialsException;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkUtils;
import org.wso2.carbon.identity.application.common.model.User;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomQDIAuthenticator extends AbstractApplicationAuthenticator implements LocalApplicationAuthenticator {

    private static final long serialVersionUID = 3365604122818036200L;
    private static Log log = LogFactory.getLog(CustomQDIAuthenticator.class);
    //private static QDIClient qdiClient  = new QDIJavaClient();
    private static QDIClient qdiClient = new QDIMockClient();

    @Override
    protected void initiateAuthenticationRequest(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 AuthenticationContext context)
            throws AuthenticationFailedException {

        String loginPage = ConfigurationFacade.getInstance().getAuthenticationEndpointURL();
        //This is the default WSO2 IS login page. If you can create your custom login page you can use that instead.

        String queryParams =
                FrameworkUtils.getQueryStringWithFrameworkContextId(context.getQueryParams(),
                        context.getCallerSessionKey(),
                        context.getContextIdentifier());

        try {
            String retryParam = "";

            if (context.isRetrying()) {
                retryParam = "&authFailure=true&authFailureMsg=login.fail.message";
            }
            // TODO: 2/7/20 put debug logs when create url check this when debugin
            response.sendRedirect(response.encodeRedirectURL(loginPage + ("?" + queryParams)) +
                    "&authenticators=" + getName() +":LOCAL" + retryParam);
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.debug("Failed to create redirect URL for authenticator : " + getName());
            }
            throw new AuthenticationFailedException(e.getMessage(), e);
        }
    }

    /**
     * This method is used to process the authentication response.
     */
    @Override
    protected void processAuthenticationResponse(HttpServletRequest request,
                                                 HttpServletResponse response, AuthenticationContext context)
            throws AuthenticationFailedException {

        String username = request.getParameter(CustomQdiAuthenticatorConstants.USER_NAME);
        String password = request.getParameter(CustomQdiAuthenticatorConstants.PASSWORD);
        boolean isAuthenticated;
        isAuthenticated = qdiClient.qdiAuthenticator(username, password);

        if (!isAuthenticated) {
            if (log.isDebugEnabled()) {
                log.debug("User authentication is failed due to invalid credentials for user : " + username);
            }
            throw new InvalidCredentialsException("User authentication failed due to invalid credentials",
                    User.getUserFromUserName(username));
        }
        context.setSubject(AuthenticatedUser.createLocalAuthenticatedUserFromSubjectIdentifier(username));
    }

    @Override
    protected boolean retryAuthenticationEnabled() {

        return true;
    }

    public String getFriendlyName() {

        //Set the name to be displayed in local authenticator drop down lsit
        return CustomQdiAuthenticatorConstants.AUTHENTICATOR_FRIENDLY_NAME;
    }

    @Override
    // TODO: 2/7/20 check the canHandle with latest code
    public boolean canHandle(HttpServletRequest httpServletRequest) {

        String userName = httpServletRequest.getParameter(CustomQdiAuthenticatorConstants.USER_NAME);
        String password = httpServletRequest.getParameter(CustomQdiAuthenticatorConstants.PASSWORD);
        return userName != null && password != null;
    }

    @Override
    public String getContextIdentifier(HttpServletRequest httpServletRequest) {

        return httpServletRequest.getParameter("sessionDataKey");
    }

    @Override
    public String getName() {

        return CustomQdiAuthenticatorConstants.AUTHENTICATOR_NAME;
    }
}
