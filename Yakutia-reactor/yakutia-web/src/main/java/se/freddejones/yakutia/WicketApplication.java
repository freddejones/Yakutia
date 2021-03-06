package se.freddejones.yakutia;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.ThemeProvider;
import de.agilecoders.wicket.themes.settings.BootswatchThemeProvider;
import se.freddejones.yakutia.auth.SignIn;
import se.freddejones.yakutia.base.WelcomePage;
import se.freddejones.yakutia.session.MySession;
import org.apache.wicket.RuntimeConfigurationType;
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

        ThemeProvider themeProvider = new BootswatchThemeProvider() {{
            defaultTheme("united");
        }};

        settings.setThemeProvider(themeProvider);
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

    @Override
    public RuntimeConfigurationType getConfigurationType() {

//        return RuntimeConfigurationType.DEPLOYMENT;
        return RuntimeConfigurationType.DEVELOPMENT;
    }

}
