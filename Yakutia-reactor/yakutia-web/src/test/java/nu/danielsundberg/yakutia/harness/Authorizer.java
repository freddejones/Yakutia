package nu.danielsundberg.yakutia.harness;

import org.apache.wicket.authroles.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;

import java.io.Serializable;

/**
 * User: Fredde
 * Date: 5/6/13 11:57 PM
 */
public class Authorizer implements IRoleCheckingStrategy, Serializable
{
    private static final long serialVersionUID = 1L;

    private final Roles roles;

    public Authorizer(String roles)
    {
        this.roles = new Roles(roles);
    }

    public boolean hasAnyRole(Roles roles)
    {
        return this.roles.hasAnyRole(roles);
    }
}