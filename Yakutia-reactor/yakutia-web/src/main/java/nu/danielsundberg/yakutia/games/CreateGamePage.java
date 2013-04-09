package nu.danielsundberg.yakutia.games;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.agilecoders.wicket.markup.html.bootstrap.button.BootstrapAjaxFallbackButton;
import nu.danielsundberg.yakutia.NavbarPage;
import nu.danielsundberg.yakutia.PlayerApi;
import nu.danielsundberg.yakutia.auth.RestParameters;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@AuthorizeInstantiation("USER")
public class CreateGamePage extends NavbarPage {

    public String message = "Nada";

    Label alarmLabel;

    public CreateGamePage(PageParameters parameters) throws NamingException {
        super(parameters);

        List<PlayerApi> list = new ArrayList<PlayerApi>();

        HttpGet request = new HttpGet(RestParameters.GETALLPLAYERS_URL);
        request.setHeader("Accept","application/json");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("Single: " + sb.toString());

            JSONObject obj = null;
            JSONString jstring = null;
            try {
                obj = new JSONObject(sb.toString());

                //JSONObject jsonObject = obj.getJSONObject("playerApi");
                //PlayerApi value = GSON.fromJSON(rd.toString(), PlayerApi.class);


                JSONArray jsonMainArr = obj.getJSONArray("playerApi");
                System.out.println("SIZE=" + jsonMainArr.length());
                for (int i = 0; i < jsonMainArr.length(); i++) {  // **line 2**
                    JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
                    String name = childJSONObject.getString("playerName");
                    String id     = childJSONObject.getString("playerId");
                    PlayerApi p1 = new PlayerApi();
                    p1.setPlayerName(name);
                    p1.setPlayerId(Long.parseLong(id));
                    list.add(p1 );
                }

//                Gson gson = new Gson();
//                List<PlayerApi> list = new ArrayList<PlayerApi>();
                  for (PlayerApi p : list) {
                      System.out.println("Well: " + p.getPlayerName());
                  }
//                GsonBuilder gsonBuilder = new GsonBuilder();
//                Gson gson = gsonBuilder.create();
////                Gson gson = new Gson();
//                PlayerApi p1 = gson.fromJson(sb.toString(), PlayerApi.class);
//                System.out.println("p1 stuff: " + p1.getPlayerName());

            } catch (JSONException e) {
                // TODO handle some exceptions here
                e.printStackTrace();
            }
        } catch (IOException e) {

        }

        List<PlayerApi> players = list;
        add(new ListView<PlayerApi>("players", players)
        {
            public void populateItem(final ListItem<PlayerApi> item)
            {
                final PlayerApi player = item.getModelObject();
                item.add(new Label("playername", player.getPlayerName()));
                Label status = new Label("status", "Invited!");
                item.add(status);
            }
        });


//        Label playername = new Label("playername", "playername!");
//        add(playername);



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
                setMessage("Changed message dude");
            }
        };

        form.add(searchButton);
        add(form);
        add(new Label("msg", message));

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
