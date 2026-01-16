<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>注文詳細</h2>
<logic:present name="order">
  <p>注文番号: <bean:write name="order" property="orderNumber" filter="true"/></p>
  <p>状態: <bean:write name="order" property="status" filter="true"/></p>
  <p>支払状態: <bean:write name="order" property="paymentStatus" filter="true"/></p>
  <p>合計金額: <bean:write name="order" property="totalAmount" filter="true"/></p>
  <logic:equal name="order" property="status" value="DELIVERED">
    <html:form action="/admin/order/refund.do" method="post">
      <html:hidden name="order" property="id"/>
      <html:token/>
      <html:submit value="返金処理"/>
    </html:form>
  </logic:equal>
</logic:present>
<logic:notPresent name="order">
  <p>注文情報がありません。</p>
</logic:notPresent>
<logic:present name="orderItems">
  <h3>商品明細</h3>
  <table border="1">
    <tr>
      <th>商品名</th>
      <th>数量</th>
      <th>小計</th>
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
