<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<div class="inner">
  <div class="logo"><html:link page="/home.do">Ski Resort Shop</html:link></div>
  <ul class="app-nav">
    <li><html:link page="/home.do">Home</html:link></li>
    <li><html:link page="/products.do">Products</html:link></li>
    <li><html:link page="/coupons/available.do">Coupons</html:link></li>
    <logic:present name="loginUser" scope="session">
      <li><html:link page="/orders.do">Order History</html:link></li>
      <li><html:link page="/points.do">Points</html:link></li>
      <li><html:link page="/addresses.do">Address Book</html:link></li>
      <logic:equal name="loginUser" property="role" value="ADMIN">
        <li><html:link page="/admin/products.do">Admin: Products</html:link></li>
        <li><html:link page="/admin/orders.do">Admin: Orders</html:link></li>
        <li><html:link page="/admin/coupons.do">Admin: Coupons</html:link></li>
        <li><html:link page="/admin/shipping.do">Admin: Shipping</html:link></li>
      </logic:equal>
    </logic:present>
    <logic:notPresent name="loginUser" scope="session">
      <li><html:link page="/login.do">Login</html:link></li>
      <li><html:link page="/register.do">Register</html:link></li>
    </logic:notPresent>
  </ul>
  <div class="actions">
    <html:form action="/products.do" method="get" styleClass="header-search">
      <input type="text" name="keyword" value="" placeholder="Search by product name or brand" />
      <button type="submit">🔍</button>
    </html:form>
    <html:link page="/cart.do" styleClass="btn">🛒 Cart</html:link>
    <logic:present name="loginUser" scope="session">
      <span class="user-name">Hello, <bean:write name="loginUser" property="username"/></span>
      <html:link page="/logout.do">Logout</html:link>
    </logic:present>
    <logic:notPresent name="loginUser" scope="session">
      <html:link page="/login.do">Login</html:link>
      <html:link page="/register.do" styleClass="btn">Register</html:link>
    </logic:notPresent>
  </div>
</div>
