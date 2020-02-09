package sample.custom.authenticator.qdi.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.identity.application.authentication.framework.ApplicationAuthenticator;
import sample.custom.authenticator.qdi.CustomQDIAuthenticator;

@Component(
        name = "sample.custom.authenticator.qdi.component",
        immediate = true)
public class CustomBasicAuthQDIServiceComponent {

    private static Log log = LogFactory.getLog(CustomBasicAuthQDIServiceComponent.class);

    protected void activate(ComponentContext ctxt) {

        try {
            CustomQDIAuthenticator auth = new CustomQDIAuthenticator();
            ctxt.getBundleContext().registerService(ApplicationAuthenticator.class.getName(), auth, null);
            log.info("CustomQDIAuthenticator bundle is activated");

        } catch (Throwable e) {
            log.error("CustomQDIAuthenticator bundle activation Failed", e);
        }
    }

    protected void deactivate(ComponentContext context) {

        log.info("CustomQDIAuthenticator bundle is deactivated");
    }
}
