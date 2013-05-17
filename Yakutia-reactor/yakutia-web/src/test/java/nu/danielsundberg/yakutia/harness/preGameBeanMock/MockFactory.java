package nu.danielsundberg.yakutia.harness.preGameBeanMock;

import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import org.apache.wicket.injection.IFieldValueFactory;

import javax.ejb.EJB;
import java.lang.reflect.Field;

/**
 * User: Fredde
 * Date: 5/7/13 8:18 PM
 */
public class MockFactory implements IFieldValueFactory {

//    protected PreGameInterface preGameBean = new PreGameBeanMock();
    protected Object bean;

    public MockFactory(Object obj) {
        bean = obj;
    }

    @Override
    public boolean supportsField(Field field) {
        return field.isAnnotationPresent(EJB.class);
    }

    @Override
    public Object getFieldValue(Field field, Object fieldOwner) {

        if (field.getType().isAssignableFrom(PreGameInterface.class)) {
//            return preGameBean;
            return bean;
        } else {
            return null;
        }
    }
}