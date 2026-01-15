package com.skishop.web.action;

import com.skishop.common.util.PasswordHasher;
import com.skishop.dao.user.UserDao;
import com.skishop.dao.user.UserDaoImpl;

public class LoginActionTest extends StrutsActionTestBase {
  public void testLoginValidation() throws Exception {
    setRequestPathInfo("/login");
    setPostRequest();
    addRequestParameter("email", "");
    addRequestParameter("password", "");
    actionPerform();
    verifyInputForward();
    verifyActionErrors(new String[] {"errors.required", "errors.required"});
  }

  public void testLoginSuccess() throws Exception {
    UserDao userDao = new UserDaoImpl();
    String salt = PasswordHasher.generateSalt();
    String hash = PasswordHasher.hash("password123", salt);
    userDao.updatePassword("u-1", hash, salt);

    setRequestPathInfo("/login");
    setPostRequest();
    addRequestParameter("email", "user@example.com");
    addRequestParameter("password", "password123");
    actionPerform();
    verifyForward("success");
    assertNotNull(getRequest().getSession().getAttribute("loginUser"));
  }
}
