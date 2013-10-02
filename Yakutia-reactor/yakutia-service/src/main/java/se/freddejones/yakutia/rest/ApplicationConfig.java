package se.freddejones.yakutia.rest;

import se.freddejones.yakutia.application.service.impl.PreGameBean;

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
        resources.add(RestTest.class);
        resources.add(PreGameBean.class);
        return resources;
    }


}
