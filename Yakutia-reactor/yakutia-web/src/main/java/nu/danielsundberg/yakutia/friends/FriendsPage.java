package nu.danielsundberg.yakutia.friends;



import nu.danielsundberg.yakutia.NavbarPage;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.games.ActiveGamePage;
import nu.danielsundberg.yakutia.games.CreateGamePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.ejb.EJB;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;

@AuthorizeInstantiation("USER")
public class FriendsPage extends NavbarPage {

    @EJB(name = "pregamebean")
    private PreGameInterface preGameInterface;

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


        List<String> test = new ArrayList<String>();
        test.add("lol");
        ListView<String> lv = new ListView<String>("rows", test)
        {
            public void populateItem(final ListItem<String> item)
            {
                add(new Label("playerid","fal"));
                Form f = new Form("form");
                f.add(new Button("invite-button"));
                add(f);
            }
        };

        add(lv);
        lv.setVisible(false);


        Form f2 = new Form("list-all-players");
        Button b = new Button("list-all-button"){

            @Override
            public void onSubmit() {

            }
        };
        f2.add(b);
        add(f2);
//        Form form = new Form("form");
//        Button searchButton = new Button("searchButton") {
//
//            @Override
//            public void onSubmit() {
//            }
//        };
//
//        form.add(searchButton);
//        add(form);

    }
}
