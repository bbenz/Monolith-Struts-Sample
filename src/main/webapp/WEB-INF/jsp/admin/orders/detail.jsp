<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>Order Details</h2>
<logic:present name="order">
  <p>Order Number: <bean:write name="order" property="orderNumber" filter="true"/></p>
  <p>Status: <bean:write name="order" property="status" filter="true"/></p>
  <p>Payment Status: <bean:write name="order" property="paymentStatus" filter="true"/></p>
  <p>Total Amount: <bean:write name="order" property="totalAmount" filter="true"/></p>
  <logic:equal name="order" property="status" value="DELIVERED">
    <html:form action="/admin/order/refund.do" method="post">
      <html:hidden name="order" property="id"/>
      <html:token/>
      <html:submit value="Process Refund"/>
    </html:form>
  </logic:equal>
</logic:present>
<logic:notPresent name="order">
  <p>Order information not available.</p>
</logic:notPresent>
<logic:present name="orderItems">
  <h3>Product Details</h3>
  <table border="1">
    <tr>
      <th>Product Name</th>
      <th>Quantity</th>
      <th>Subtotal</th>
    </tr>
    <logic:iterate id="item" name="orderItems">
      <tr>
        <td><bean:write name="item" property="productName" filter="true"/></td>
        <td><bean:write name="item" property="quantity" filter="true"/></td>
        <td><bean:write name="item" property="subtotal" filter="true"/></td>
      </tr>
    </logic:iterate>
  </table>
</logic:present>
