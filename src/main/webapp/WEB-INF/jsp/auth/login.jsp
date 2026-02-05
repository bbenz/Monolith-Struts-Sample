<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>Login</h2>
<html:form action="/login.do" method="post">
  <table>
    <tr>
      <th><bean:message key="label.email"/></th>
      <td><html:text property="email" size="30"/></td>
    </tr>
    <tr>
      <th><bean:message key="label.password"/></th>
      <td><html:password property="password" size="30"/></td>
    </tr>
  </table>
  <html:token/>
  <html:submit value="Login"/>
</html:form>
<p>
  <html:link page="/register.do">Register</html:link>
  |
  <html:link page="/password/forgot.do">Forgot password?</html:link>
</p>
