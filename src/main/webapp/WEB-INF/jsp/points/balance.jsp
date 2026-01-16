<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>ポイント残高</h2>
<logic:present name="pointBalance">
  <p>残高: <bean:write name="pointBalance" property="balance" filter="true"/></p>
  <p>累計獲得: <bean:write name="pointBalance" property="lifetimeEarned" filter="true"/></p>
  <p>累計利用: <bean:write name="pointBalance" property="lifetimeRedeemed" filter="true"/></p>
</logic:present>
<logic:notPresent name="pointBalance">
  <p>ポイント情報がありません。</p>
</logic:notPresent>
