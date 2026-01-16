package com.skishop.web.action;

import javax.servlet.http.HttpServletResponse;
import servletunit.HttpServletRequestSimulator;
import servletunit.HttpServletResponseSimulator;

public class AuthRequestProcessorTest extends StrutsActionTestBase {
  public void testCsrfFailureReturnsForbidden() throws Exception {
    setLoginUser("u-1", "USER");
    setRequestPathInfo("/checkout");
    HttpServletRequestSimulator request = (HttpServletRequestSimulator) getRequest();
    request.setMethod(HttpServletRequestSimulator.POST);
    addRequestParameter("cartId", "cart-1");
    addRequestParameter("paymentMethod", "CARD");
    addRequestParameter("cardNumber", "4111111111111111");
    addRequestParameter("cardExpMonth", "12");
    addRequestParameter("cardExpYear", "2099");
    addRequestParameter("cardCvv", "123");
    addRequestParameter("billingZip", "12345");
    try {
      actionPerform();
    } catch (junit.framework.AssertionFailedError e) {
      // Some Struts test flows throw on sendError; status validation still applies.
    }
    HttpServletResponseSimulator response = (HttpServletResponseSimulator) getResponse();
    assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatusCode());
  }

  public void testRedirectsToLoginWhenUnauthenticated() throws Exception {
    setRequestPathInfo("/orders");
    setGetRequest();
    actionPerform();
    HttpServletResponseSimulator response = (HttpServletResponseSimulator) getResponse();
    assertEquals(HttpServletResponse.SC_FOUND, response.getStatusCode());
    assertEquals("/login.do", response.getHeader("Location"));
  }

  public void testUnauthorizedRoleReturnsForbidden() throws Exception {
    setLoginUser("u-1", "USER");
    setRequestPathInfo("/admin/products");
    setGetRequest();
    try {
      actionPerform();
    } catch (junit.framework.AssertionFailedError e) {
      // Some Struts test flows throw on sendError; status validation still applies.
    }
    HttpServletResponseSimulator response = (HttpServletResponseSimulator) getResponse();
    assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatusCode());
  }

  public void testAdminRoleAllowsAccess() throws Exception {
    setLoginUser("admin-1", "ADMIN");
    setRequestPathInfo("/admin/products");
    setGetRequest();
    actionPerform();
    verifyForward("success");
  }
}
