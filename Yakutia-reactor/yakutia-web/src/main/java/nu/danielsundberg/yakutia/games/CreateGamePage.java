package nu.danielsundberg.yakutia.games;



import nu.danielsundberg.yakutia.application.service.iface.GameEngineInterface;
import nu.danielsundberg.yakutia.NavbarPage;
import nu.danielsundberg.yakutia.application.service.PlayerApi;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.application.service.PlayerApi;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.application.service.iface.GameEngineInterface;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;

@AuthorizeInstantiation("USER")
public class CreateGamePage extends NavbarPage {

    public String message = "Nada";



    public CreateGamePage(PageParameters parameters) throws NamingException {
        super(parameters);

        List<Player> list = new ArrayList<Player>();

        InitialContext ctx = null;
        PreGameInterface preGameBean = null;

        try {
            ctx = new InitialContext();
            preGameBean = (PreGameInterface) ctx.lookup("preGameBean");
            List<Player> listTmp = preGameBean.getPlayers();
            for (Player p : listTmp) {
                MySession session = (MySession) getSession();
                if (!p.getName().equals(session.getPlayerName())) {
                    list.add(p);
                }
            }

        } catch (NamingException e) {
            e.printStackTrace();
        }

//        HttpGet request = new HttpGet(RestParameters.GETALLPLAYERS_URL);
//        request.setHeader("Accept","application/json");
//        HttpClient client = new DefaultHttpClient();
//        HttpResponse response = null;
//        try {
//            response = client.execute(request);
//            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//            StringBuilder sb = new StringBuilder();
//            String line = "";
//            while ((line = rd.readLine()) != null) {
//                sb.append(line);
//            }
//            System.out.println("Single: " + sb.toString());
//
//            JSONObject obj = null;
//            JSONString jstring = null;
//            try {
//                obj = new JSONObject(sb.toString());
//
//                //JSONObject jsonObject = obj.getJSONObject("playerApi");
//                //PlayerApi value = GSON.fromJSON(rd.toString(), PlayerApi.class);
//
//
//                JSONArray jsonMainArr = obj.getJSONArray("playerApi");
//                System.out.println("SIZE=" + jsonMainArr.length());
//                for (int i = 0; i < jsonMainArr.length(); i++) {  // **line 2**
//                    JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
//                    String name = childJSONObject.getString("playerName");
//                    String id     = childJSONObject.getString("playerId");
//                    PlayerApi p1 = new PlayerApi();
//                    p1.setPlayerName(name);
//                    p1.setPlayerId(Long.parseLong(id));
//                    list.add(p1 );
//                }
//
////                Gson gson = new Gson();
////                List<PlayerApi> list = new ArrayList<PlayerApi>();
//                  for (PlayerApi p : list) {
//                      System.out.println("Well: " + p.getPlayerName());
//                  }
////                GsonBuilder gsonBuilder = new GsonBuilder();
////                Gson gson = gsonBuilder.create();
//////                Gson gson = new Gson();
////                PlayerApi p1 = gson.fromJson(sb.toString(), PlayerApi.class);
////                System.out.println("p1 stuff: " + p1.getPlayerName());
//
//            } catch (JSONException e) {
//                // TODO handle some exceptions here
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//
//        }


        List<Player> players = list;
        final ListView<Player> listview = new ListView<Player>("players", players)
        {
            public void populateItem(final ListItem<Player> item)
            {
                final Player player = item.getModelObject();
                final Label playerName = new Label("playername", player.getName());
                playerName.setOutputMarkupId(true);

                item.add(playerName);
                Label status = new Label("status", "Invited!");
                item.add(new Check<Player>("checkbox", item.getModel()));
                item.add(status);
            }
        };
//        final CheckGroup group=new CheckGroup("group",listview.getModel());
        final CheckGroup group=new CheckGroup("group",listview.getModel());
        listview.setReuseItems(true);
        listview.setOutputMarkupId(true);
        group.add(listview);
        group.setOutputMarkupId(true);

        final Label test = new Label("msg", message);
        test.setOutputMarkupId(true);

        Form form = new Form("form1");
//        group.add(new AjaxFormChoiceComponentUpdatingBehavior()
//        {
//            @Override
//            protected void onUpdate(AjaxRequestTarget target)
//            {
//
//                target.add(new Label("playername","blaa"));
//
////                form.remove(alarmLabel);
////                form.add(alarmLabel);
////                alarmLabel
////                        .replaceWith(new Label("status2","fakc?"))
////                        .add(new AttributeAppender("class", new Model("badge badge-success")))
////                        .setOutputMarkupId(true);
////
////                target.add(form);
//            }
//        });




        Button button = new Button("submit1") {

            @Override
            public void onSubmit() {
                List<PlayerApi> players = (ArrayList<PlayerApi>)group.getDefaultModelObject();
                System.out.println(players.get(0).getPlayerName());

                try {
                    InitialContext ctx = new InitialContext();
                    GameEngineInterface gameBean = (GameEngineInterface) ctx.lookup("kickass");
                    PreGameInterface preGame = (PreGameInterface) ctx.lookup("preGameBean");

                    long gameId = gameBean.createNewGame(3);
                    for (PlayerApi p : players) {
                        preGame.invitePlayerToGame(p.getPlayerId(),gameId);
                    }

                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        };
        form.add(button);
        form.add(group);
        add(form);
        add(test);

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
