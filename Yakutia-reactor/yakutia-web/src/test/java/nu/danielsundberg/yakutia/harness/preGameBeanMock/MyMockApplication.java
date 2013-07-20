package nu.danielsundberg.yakutia.harness.preGameBeanMock;

import nu.danielsundberg.yakutia.WicketApplication;
import org.apache.wicket.application.IComponentInstantiationListener;

/**
 * User: Fredde
 * Date: 5/7/13 8:16 PM
 */
public class MyMockApplication extends WicketApplication {

    private Object[] beans;

    public MyMockApplication(Object[] beans) {
        this.beans = beans;
    }

    @Override
    protected IComponentInstantiationListener getComponentInstantiationListener() {
        return new MockInjector(this, beans);
    }
}
