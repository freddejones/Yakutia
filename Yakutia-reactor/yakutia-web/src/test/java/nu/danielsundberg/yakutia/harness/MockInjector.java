package nu.danielsundberg.yakutia.harness;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * User: Fredde
 * Date: 5/7/13 8:17 PM
 */
public class MockInjector extends Injector implements IComponentInstantiationListener {

    private MockFactory factory;

    public MockInjector(WebApplication webApplication) {
        bind(webApplication);
        factory = new MockFactory();
    }

    @Override
    public void inject(Object object) {
        inject(object, factory);
    }

    @Override
    public void onInstantiation(Component component) {
        inject(component);
    }
}
