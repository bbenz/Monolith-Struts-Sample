<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>カート</h2>
<logic:empty name="cartItems">
  <p>カートは空です。</p>
</logic:empty>
<logic:notEmpty name="cartItems">
  <table border="1">
    <tr>
      <th>商品ID</th>
      <th>数量</th>
      <th>単価</th>
    </tr>
    <logic:iterate id="item" name="cartItems">
      <tr>
        <td><bean:write name="item" property="productId" filter="true"/></td>
        <td><bean:write name="item" property="quantity" filter="true"/></td>
        <td><bean:write name="item" property="unitPrice" filter="true"/></td>
      </tr>
    </logic:iterate>
  </table>
  <p>小計: <bean:write name="cartSubtotal" filter="true"/></p>
  <logic:present name="coupon">
    <p>クーポン: <bean:write name="coupon" property="code" filter="true"/></p>
    <p>割引額: <bean:write name="discountAmount" filter="true"/></p>
  </logic:present>
</logic:notEmpty>

<h3>クーポン適用</h3>
<html:form action="/coupon/apply.do" method="post">
  <html:text property="code" size="20"/>
  <html:token/>
  <html:submit value="適用"/>
</html:form>

<p><html:link page="/checkout.do">チェックアウトへ</html:link></p>
