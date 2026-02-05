<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>Password Reset Request Complete</h2>
<p>Reset information has been sent to your email.</p>
<logic:present name="resetToken">
  <p>Development Token: <bean:write name="resetToken" filter="true"/></p>
</logic:present>
<p><html:link page="/password/reset.do">Go to Password Reset</html:link></p>
