package nu.danielsundberg.yakutia.bleh;

import nu.danielsundberg.yakutia.base.NavbarPage;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.naming.InitialContext;
import javax.naming.NamingException;

@AuthorizeInstantiation("ADMIN")
public class ApiPage extends NavbarPage {


    public ApiPage(PageParameters parameters) throws NamingException {
        super(parameters);

        Form testdataForm = new Form("testdata");
        Button testdataButton = new Button("testbutton") {

            @Override
            public void onSubmit() {
                InitialContext ctx = null;
                PreGameInterface test = null;

                try {
                    ctx = new InitialContext();
                    test = (PreGameInterface) ctx.lookup("preGameBean");
                    test.createNewPlayer("apan1","email@email.com");
                    test.createNewPlayer("apan2","email@email.com");
                    test.createNewPlayer("apan3","email@email.com");
                } catch (NamingException e) {

                } catch (PlayerAlreadyExists pae) {

                }

            }
        };

        testdataForm.add(testdataButton);
        add(testdataForm);
    }

}
