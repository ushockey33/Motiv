package com.SD.motiv.login;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.SD.motiv.channel.data.model.Channels;
import com.SD.motiv.user.data.model.User;
import com.google.android.gms.common.SignInButton;
import com.SD.motiv.R;
import com.SD.motiv.TestDependencies;
import com.SD.motiv.channel.service.ChannelService;
import com.SD.motiv.login.data.model.Authentication;
import com.SD.motiv.login.service.LoginService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import io.reactivex.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    private static final User SUCCESSFUL_AUTH_USER = new User("id", "name", "http://photo.url");

    @Rule
    public ActivityTestRule<LoginActivity> activity = new ActivityTestRule<>(LoginActivity.class, false, false);
    private Authentication authentication;

    @Before
    public void setUp() throws Exception {
        LoginService loginService = Mockito.mock(LoginService.class);
        ChannelService channelService = Mockito.mock(ChannelService.class);
        authentication = Mockito.mock(Authentication.class);

        when(loginService.getAuthentication()).thenReturn(Observable.just(authentication));
        when(channelService.getChannelsFor(SUCCESSFUL_AUTH_USER)).thenReturn(Observable.<Channels>empty());

        TestDependencies.init()
                .withLoginService(loginService)
                .withChannelService(channelService);
    }

    @Test
    public void signInButtonIsVisibleWhenAuthenticationFails() {
        givenAuthenticationFails();

        activity.launchActivity(new Intent());

        assertThatGoogleSignInButtonIsShown();
    }

    private void givenAuthenticationFails() {
        when(authentication.isSuccess()).thenReturn(false);
        when(authentication.getFailure()).thenReturn(new Throwable("Message"));
    }

    private void assertThatGoogleSignInButtonIsShown() {
        onView(withClassName(endsWith(SignInButton.class.getSimpleName()))).check(matches(isDisplayed()));
    }

    @Test
    public void whenAuthenticationSucceedsThenListOfChannelsIsShownImmediately() throws Exception {
        givenAuthenticationIsSuccessful();

        activity.launchActivity(new Intent());

        assertThatScreenWithChannelsIsShown();
    }

    private void givenAuthenticationIsSuccessful() {
        when(authentication.isSuccess()).thenReturn(true);
        when(authentication.getUser()).thenReturn(SUCCESSFUL_AUTH_USER);
    }

    private ViewInteraction assertThatScreenWithChannelsIsShown() {
        return onView(withId(R.id.channels)).check(matches(isDisplayed()));
    }

}
