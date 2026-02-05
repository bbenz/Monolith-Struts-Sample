<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>User Registration</h2>
<html:form action="/register.do" method="post">
  <table>
    <tr>
      <th><bean:message key="label.email"/></th>
      <td><html:text property="email" size="30"/></td>
    </tr>
    <tr>
      <th><bean:message key="label.username"/></th>
      <td><html:text property="username" size="30"/></td>
    </tr>
    <tr>
      <th><bean:message key="label.password"/></th>
      <td><html:password property="password" size="30"/></td>
    </tr>
    <tr>
      <th><bean:message key="label.passwordConfirm"/></th>
      <td><html:password property="passwordConfirm" size="30"/></td>
    </tr>
  </table>
  <html:token/>
  <html:submit value="Register"/>
</html:form>
<p><html:link page="/login.do">Back to Login</html:link></p>
