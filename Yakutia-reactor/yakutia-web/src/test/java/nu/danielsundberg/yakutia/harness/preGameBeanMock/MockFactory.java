package nu.danielsundberg.yakutia.harness.preGameBeanMock;

import nu.danielsundberg.yakutia.application.service.iface.FriendManagerInterface;
import nu.danielsundberg.yakutia.application.service.iface.PlayerActionsInterface;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import org.apache.wicket.injection.IFieldValueFactory;

import javax.ejb.EJB;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Fredde
 * Date: 5/7/13 8:18 PM
 */
public class MockFactory implements IFieldValueFactory {

    protected List<Object> beans;

    public MockFactory(Object[] beanArray) {
        beans = new ArrayList<Object>();
        for (Object obj : beanArray) {
            beans.add(obj);
        }
    }

    @Override
    public boolean supportsField(Field field) {
        return field.isAnnotationPresent(EJB.class);
    }

    @Override
    public Object getFieldValue(Field field, Object fieldOwner) {

        if (field.getType().isAssignableFrom(PreGameInterface.class)) {
            return findBean("PreGameInterface");
        } else if (field.getType().isAssignableFrom(FriendManagerInterface.class)) {
            return findBean("FriendManagerInterface");
        } else if (field.getType().isAssignableFrom(PlayerActionsInterface.class)) {
            return findBean("PlayerActionsInterface");
        }
        else {
            return null;
        }
    }

    private Object findBean(String beanName) {
        for (Object bean : beans) {
            if (bean.getClass().toString().contains(beanName)) {
                return bean;
            }
        }
        return null;
    }
}
