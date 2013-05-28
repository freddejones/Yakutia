package nu.danielsundberg.yakutia;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.settings.BootstrapSettings;
import de.agilecoders.wicket.settings.BootswatchThemeProvider;
import de.agilecoders.wicket.settings.ThemeProvider;
import nu.danielsundberg.yakutia.auth.SignIn;
import nu.danielsundberg.yakutia.base.WelcomePage;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.Session;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.wicketstuff.javaee.injection.JavaEEComponentInjector;

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

        /* Enable wicket to handle injects */
        getComponentInstantiationListeners().add(getComponentInstantiationListener());

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

    protected IComponentInstantiationListener getComponentInstantiationListener() {
        return new JavaEEComponentInjector(this);
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
