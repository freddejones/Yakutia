package nu.danielsundberg.yakutia;

import nu.danielsundberg.yakutia.auth.CreateAccountPage;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * User: Fredde
 * Date: 3/29/13 12:19 AM
 */
public class CallbackStuffPage extends BasePage {


    public CallbackStuffPage(PageParameters parameters) {
        super(parameters);
        getSession().bind();

        String what = "authrized?";
        String oauthVerifier = null;

        Iterator<String> iter = parameters.getNamedKeys().iterator();
        while (iter.hasNext()) {

            String name = iter.next();
            what += ", " + name + "=" + parameters.get(name).toString();

            if (name.equals("oauth_verifier")) {
                oauthVerifier = parameters.get(name).toString();
                if (oauthVerifier.equals("undefined")) {
                    oauthVerifier = null;
                }
            }
        }

        Token requestToken = null;
        if (getSession().getAttribute("requestToken") != null) {
            what += " -> request toke stuff";
            requestToken = (Token) getSession().getAttribute("requestToken");
        }
        final String SCOPE = "https://www.googleapis.com/auth/userinfo.email";
        OAuthService service = new ServiceBuilder()
                .provider(GoogleApi.class)
                .apiKey("199525905624.apps.googleusercontent.com")
                .apiSecret("dFojU6QE5tdbi7BQ_j9wd-bo")
                .scope(SCOPE)
                .build();

        String emailExtracted = "";
        if (oauthVerifier != null) {
            try {
                Verifier verifier = new Verifier(oauthVerifier);
                Token accessToken = service.getAccessToken(requestToken, verifier);
                // TODO Store this token?

                String link = "https://www.googleapis.com/oauth2/v1/userinfo";
                OAuthRequest request = new OAuthRequest(Verb.GET, link);
                service.signRequest(accessToken, request);
                request.addHeader("GData-Version", "3.0");
                Response response = request.send();
                System.out.println("Got it! Lets see what we found...");
                System.out.println();
                System.out.println(response.getCode());
                System.out.println(response.getBody());

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response.getBody().toString());
                    emailExtracted = obj.getString("email");
                    what += "EMAIL: " + emailExtracted + ".... " ;
                } catch (JSONException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


                what += response.getBody();
            } catch (RuntimeException re) {
                what += "Hmm somethings fishy here!";
            }
        }

        String restUrl = "http://localhost:8080/yakutia-services/rest/player/playerExist";
//        restUrl += "?email=fuckfacejones.com";
        restUrl += "?email="+emailExtracted;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(restUrl);

        HttpResponse response = null;
        try {
            response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {

                if (line.contains("true")) {
                    MySession session = (MySession)getSession();

                    if (session.signIn("admin",""))
                    {
                        PageParameters params = new PageParameters();
                        params.set("playername","TODO FIX ME");
                        setResponsePage(WelcomePage.class, params);
                    }
                } else {
                    what += "Well just show how to create account";
                    PageParameters params = new PageParameters();
                    params.set("email",emailExtracted);
                    setResponsePage(CreateAccountPage.class, params);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            what += "IOException";
        }

        add(new Label("msg", what));
    }


}
