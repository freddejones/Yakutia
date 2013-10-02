package se.freddejones.yakutia.harness.beanMocker;

import se.freddejones.yakutia.WicketApplication;
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
