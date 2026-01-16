package com.skishop.web.action;

public class LogoutActionTest extends StrutsActionTestBase {
  public void testLogoutClearsSession() throws Exception {
    setLoginUser("u-1", "USER");
    setRequestPathInfo("/logout");
    setGetRequest();
    actionPerform();
    verifyForward("success");
    assertNull(getRequest().getSession(false));
  }
}
