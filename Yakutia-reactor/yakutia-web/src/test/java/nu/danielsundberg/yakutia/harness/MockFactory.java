package nu.danielsundberg.yakutia.harness;

import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import org.apache.wicket.injection.IFieldValueFactory;

import javax.ejb.EJB;
import java.lang.reflect.Field;

/**
 * User: Fredde
 * Date: 5/7/13 8:18 PM
 */
public class MockFactory implements IFieldValueFactory {

    private PreGameInterface preGameBean = new PreGameBeanMock();

    @Override
    public boolean supportsField(Field field) {
        return field.isAnnotationPresent(EJB.class);
    }

    @Override
    public Object getFieldValue(Field field, Object fieldOwner) {

        if (field.getType().isAssignableFrom(PreGameInterface.class)) {
            return preGameBean;
        } else {
            return null;
        }
    }
}
