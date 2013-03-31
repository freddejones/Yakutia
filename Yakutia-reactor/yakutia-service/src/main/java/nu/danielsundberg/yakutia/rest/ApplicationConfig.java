package nu.danielsundberg.yakutia.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("/rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return getRestResourceClasses();
    }

    private Set<Class<?>> getRestResourceClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        resources.add(nu.danielsundberg.yakutia.rest.RestTest.class);
        resources.add(nu.danielsundberg.yakutia.application.service.impl.PreGameBean.class);
        return resources;
    }


}
