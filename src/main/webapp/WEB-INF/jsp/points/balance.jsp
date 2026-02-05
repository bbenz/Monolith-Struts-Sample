<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>Points Balance</h2>
<logic:present name="pointBalance">
  <p>Balance: <bean:write name="pointBalance" property="balance" filter="true"/></p>
  <p>Lifetime Earned: <bean:write name="pointBalance" property="lifetimeEarned" filter="true"/></p>
  <p>Lifetime Redeemed: <bean:write name="pointBalance" property="lifetimeRedeemed" filter="true"/></p>
</logic:present>
<logic:notPresent name="pointBalance">
  <p>Points information not available.</p>
</logic:notPresent>
