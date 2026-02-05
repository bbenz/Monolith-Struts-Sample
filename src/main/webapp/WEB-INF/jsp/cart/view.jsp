<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2 class="page-title">Cart</h2>
<logic:empty name="cartItems">
  <div class="card">Your cart is empty.</div>
</logic:empty>
<logic:notEmpty name="cartItems">
  <div class="card table-responsive">
  <table>
    <tr>
      <th>Product Name</th>
      <th>Quantity</th>
      <th>Unit Price</th>
    </tr>
    <logic:iterate id="item" name="cartItems">
      <tr>
        <td><bean:write name="item" property="productName" filter="true"/></td>
        <td><bean:write name="item" property="quantity" filter="true"/></td>
        <td><bean:write name="item" property="unitPrice" filter="true"/></td>
      </tr>
    </logic:iterate>
  </table>
  </div>
  <div class="card cart-summary">
    <p>Subtotal: <strong><bean:write name="cartSubtotal" filter="true"/></strong></p>
  <logic:present name="coupon">
    <p>Coupon: <bean:write name="coupon" property="code" filter="true"/></p>
    <p>Discount: <bean:write name="discountAmount" filter="true"/></p>
  </logic:present>
  </div>
</logic:notEmpty>

<h3>Apply Coupon</h3>
<html:form action="/coupon/apply.do" method="post">
  <html:text property="code" size="20"/>
  <html:token/>
  <html:submit value="Apply"/>
</html:form>

<p><html:link page="/checkout.do" styleClass="btn">Proceed to Checkout</html:link></p>
