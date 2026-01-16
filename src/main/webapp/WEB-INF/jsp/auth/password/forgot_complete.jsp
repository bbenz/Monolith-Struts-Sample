<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>パスワード再発行完了</h2>
<p>メールにリセット情報を送信しました。</p>
<logic:present name="resetToken">
  <p>開発用トークン: <bean:write name="resetToken" filter="true"/></p>
</logic:present>
<p><html:link page="/password/reset.do">パスワードリセットへ</html:link></p>
