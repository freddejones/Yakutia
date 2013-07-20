package nu.danielsundberg.yakutia.bleh;

import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.FriendManagerInterface;
import nu.danielsundberg.yakutia.base.NavbarPage;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.entity.Player;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;

@AuthorizeInstantiation("ADMIN")
public class ApiPage extends NavbarPage {

    @EJB(name = "pregamebean")
    private PreGameInterface preGameInterface;
    @EJB(name = "friendManagerBean")
    private FriendManagerInterface friendManager;

    public ApiPage(PageParameters parameters) throws NamingException {
        super(parameters);

        Form testdataForm = new Form("testdata");
        Button testdataButton = new Button("testbutton") {

            @Override
            public void onSubmit() {

                try {
                    Long playerOne = preGameInterface.createNewPlayer("apan1","email1@email.com");
                    Long playerTwo = preGameInterface.createNewPlayer("apan2","email2@email.com");
                    preGameInterface.createNewPlayer("apan3","email3@email.com");
                    Long myId = preGameInterface.createNewPlayer("fidde","freddejones@gmail.com");

                    Player myPlayer = preGameInterface.getPlayerById(myId);

                    // Create one invite
                    Player pToInvite = preGameInterface.getPlayerById(playerOne);
                    friendManager.sendInvite(pToInvite,myPlayer);

                    // Create a friend
                    Player pToFriend = preGameInterface.getPlayerById(playerTwo);
                    friendManager.sendInvite(myPlayer, pToFriend);
                    friendManager.acceptInvite(pToFriend, myPlayer);

                } catch (PlayerAlreadyExists pae) {

                } catch (NoPlayerFoundException e) {

                }

            }
        };

        testdataForm.add(testdataButton);
        add(testdataForm);


        List<Player> players = preGameInterface.getPlayers();
        add(new ListView<Player>("rows", players)
        {
            public void populateItem(final ListItem<Player> item)
            {
                final Player player = item.getModelObject();
                item.add(new Label("name", player.getName()));
                item.add(new Label("id", player.getPlayerId()));
            }
        });
    }

}
