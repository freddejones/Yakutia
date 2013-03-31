package nu.danielsundberg.yakutia;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.settings.BootstrapSettings;
import de.agilecoders.wicket.settings.BootswatchThemeProvider;
import de.agilecoders.wicket.settings.ThemeProvider;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.IRequestLogger;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 */
public class WicketApplication extends AuthenticatedWebApplication
{

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return WelcomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();
        BootstrapSettings settings = new BootstrapSettings();
//        settings.minify(false);

        ThemeProvider themeProvider = new BootswatchThemeProvider() {{
            defaultTheme("united");
        }};

        settings.setThemeProvider(themeProvider);
        //settings.useResponsiveCss(true);
        getMarkupSettings().setStripWicketTags(true);
        Bootstrap.install(this, settings);
	}

    @Override
    public Session newSession(Request request, Response response)
    {
        return new MySession(request);
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return MySession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return SignIn.class;
    }

}
