<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>Order Confirmation</h2>
<logic:present name="order">
  <p>Order Number: <bean:write name="order" property="orderNumber" filter="true"/></p>
  <p>Status: <bean:write name="order" property="status" filter="true"/></p>
  <p>Total Amount: <bean:write name="order" property="totalAmount" filter="true"/></p>
</logic:present>
<logic:notPresent name="order">
  <p>Order information not available.</p>
</logic:notPresent>
<p><html:link page="/orders.do">Go to Order History</html:link></p>
