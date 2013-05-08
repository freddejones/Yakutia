package nu.danielsundberg.yakutia.friends;



import nu.danielsundberg.yakutia.NavbarPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.naming.NamingException;

@AuthorizeInstantiation("USER")
public class FriendsPage extends NavbarPage {

    Label alarmLabel;

    public FriendsPage(PageParameters parameters) throws NamingException {
        super(parameters);


        alarmLabel = new Label("status2", "oh well");
        alarmLabel.add(new AttributeAppender("class", new Model("badge badge-warning")));
        alarmLabel.setOutputMarkupId(true);


        Form form1 = new Form("ajaxform");

        form1.add(new AjaxFallbackButton("button1", form1) {
            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form) {
//                alarmLabel = new Label("status2", "oh well");
//                alarmLabel.replaceWith(new Label("status2","fack?"));

//                alarmLabel.add(new AttributeAppender("class", new Model("badge badge-success")));
//                alarmLabel.setOutputMarkupId(true);
//                target.add(alarmLabel);
                if (target != null ) {
                    form.remove(alarmLabel);
                    form.add(alarmLabel);
                    alarmLabel
                            .replaceWith(new Label("status2","fakc?"))
                            .add(new AttributeAppender("class", new Model("badge badge-success")))
                            .setOutputMarkupId(true);

                    target.add(form);
                }


            }
        });
        form1.add(alarmLabel);
        add(form1);

        Form form = new Form("form");
        Button searchButton = new Button("searchButton") {

            @Override
            public void onSubmit() {
            }
        };

        form.add(searchButton);
        add(form);

    }
}
