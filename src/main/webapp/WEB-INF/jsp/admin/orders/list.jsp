<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>Order Management</h2>
<logic:empty name="orders">
  <p>No order data available.</p>
</logic:empty>
<logic:notEmpty name="orders">
  <table border="1">
    <tr>
      <th>Order Number</th>
      <th>Status</th>
      <th>Payment Status</th>
      <th>Total</th>
      <th>Details</th>
      <th>Update</th>
    </tr>
    <logic:iterate id="order" name="orders">
      <bean:define id="orderId" name="order" property="id" type="java.lang.String"/>
      <bean:define id="orderStatus" name="order" property="status" type="java.lang.String"/>
      <bean:define id="paymentStatus" name="order" property="paymentStatus" type="java.lang.String"/>
      <tr>
        <td><bean:write name="order" property="orderNumber" filter="true"/></td>
        <td><bean:write name="order" property="status" filter="true"/></td>
        <td><bean:write name="order" property="paymentStatus" filter="true"/></td>
        <td><bean:write name="order" property="totalAmount" filter="true"/></td>
        <td>
          <html:link page="/admin/orders/detail.do" paramId="orderId" paramName="order" paramProperty="id">Details</html:link>
        </td>
        <td>
          <html:form action="/admin/order/update.do" method="post">
            <html:hidden property="orderId" value="<%= org.apache.struts.util.ResponseUtils.filter(orderId) %>"/>
            <html:text property="status" value="<%= org.apache.struts.util.ResponseUtils.filter(orderStatus) %>" size="10"/>
            <html:text property="paymentStatus" value="<%= org.apache.struts.util.ResponseUtils.filter(paymentStatus) %>" size="10"/>
            <html:token/>
            <html:submit value="Update"/>
          </html:form>
        </td>
      </tr>
    </logic:iterate>
  </table>
</logic:notEmpty>
