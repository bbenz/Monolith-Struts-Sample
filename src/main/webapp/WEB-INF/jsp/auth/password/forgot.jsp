<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>パスワード再発行</h2>
<html:form action="/password/forgot.do" method="post">
  <table>
    <tr>
      <th><bean:message key="label.email"/></th>
      <td><html:text property="email" size="40"/></td>
    </tr>
  </table>
  <html:token/>
  <html:submit value="送信"/>
</html:form>
<p><html:link page="/login.do">ログインへ戻る</html:link></p>
