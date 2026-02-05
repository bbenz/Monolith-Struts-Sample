<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>Order History</h2>
<logic:empty name="orders">
  <p>No order history available.</p>
</logic:empty>
<logic:notEmpty name="orders">
  <table border="1">
    <tr>
      <th>Order Number</th>
      <th>Status</th>
      <th>Total</th>
      <th>Actions</th>
    </tr>
    <logic:iterate id="order" name="orders">
      <bean:define id="orderId" name="order" property="id" type="java.lang.String"/>
      <tr>
        <td><bean:write name="order" property="orderNumber" filter="true"/></td>
        <td><bean:write name="order" property="status" filter="true"/></td>
        <td><bean:write name="order" property="totalAmount" filter="true"/></td>
        <td>
          <html:link page="/orders/detail.do" paramId="orderId" paramName="order" paramProperty="id">Details</html:link>
          <br/>
          <form action="/orders/cancel.do" method="post">
            <input type="hidden" name="orderId" value="<bean:write name='order' property='id' filter='true'/>"/>
            <html:token/>
            <button type="submit">Cancel</button>
          </form>
          <form action="/orders/return.do" method="post">
            <input type="hidden" name="orderId" value="<bean:write name='order' property='id' filter='true'/>"/>
            <html:token/>
            <button type="submit">Return</button>
          </form>
        </td>
      </tr>
    </logic:iterate>
  </table>
</logic:notEmpty>
