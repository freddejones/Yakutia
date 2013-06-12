package nu.danielsundberg.yakutia.auth;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.WicketApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { OauthParameters.class })
public class SignInTest {

    private WicketTester tester;
    OAuthService oAuthService;
    Token tokenMock;

    @Before
    public void setup() {
        tester = new WicketTester(new WicketApplication());
        oAuthService = mock(OAuthService.class);
        tokenMock = mock(Token.class);
    }

    @Test
    public void signInWithOAuth() {
        // Given: a user

        OAuthService service = mock(OAuthService.class);
        Token tokenMock = mock(Token.class);
        mockStatic(OauthParameters.class);
        when(OauthParameters.getOAuthService()).thenReturn(service);
        when(service.getRequestToken()).thenReturn(tokenMock);
        when(service.getAuthorizationUrl((Token)any())).thenReturn("localhost");

        // When: a user hits sign in and clicks sign in with google
        tester.startPage(SignIn.class);
        FormTester formTester = tester.newFormTester("google-form");
        tester.setFollowRedirects(false);
        formTester.submit("google");

        // Then: page gets redirected
        Assert.assertTrue(tester.getLastResponse().isRedirect());
    }

}
