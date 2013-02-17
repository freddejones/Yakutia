package nu.danielsundberg.yakutia;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.settings.BootstrapSettings;
import de.agilecoders.wicket.settings.BootswatchThemeProvider;
import de.agilecoders.wicket.settings.ThemeProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 */
public class WicketApplication extends WebApplication
{    	
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();
        BootstrapSettings settings = new BootstrapSettings();
        settings.minify(false);

        ThemeProvider themeProvider = new BootswatchThemeProvider() {{
            defaultTheme("cyborg");
        }};

        settings.setThemeProvider(themeProvider);
        //settings.useResponsiveCss(true);
        getMarkupSettings().setStripWicketTags(true);
        Bootstrap.install(this, settings);
	}
}
