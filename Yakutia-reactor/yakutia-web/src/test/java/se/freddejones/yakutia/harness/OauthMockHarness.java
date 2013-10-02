package se.freddejones.yakutia.harness;

import se.freddejones.yakutia.auth.OauthParameters;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * User: Fredde
 * Date: 6/12/13 11:54 PM
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest( { OauthParameters.class })
public class OauthMockHarness {

    private Token tokenMock;
    private OAuthService service;

    @Before
    public void setupBeforeClass() {
        service = mock(OAuthService.class);
        tokenMock = mock(Token.class);

        mockStatic(OauthParameters.class);
        when(OauthParameters.getOAuthService()).thenReturn(service);
        when(service.getRequestToken()).thenReturn(tokenMock);
        when(service.getAuthorizationUrl((Token)any())).thenReturn("localhost");
    }


}
