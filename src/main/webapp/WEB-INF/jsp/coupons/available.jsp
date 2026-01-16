<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>利用可能なクーポン</h2>
<logic:empty name="coupons">
  <p>利用可能なクーポンはありません。</p>
</logic:empty>
<logic:notEmpty name="coupons">
  <table border="1">
    <tr>
      <th>コード</th>
      <th>タイプ</th>
      <th>割引値</th>
      <th>最低金額</th>
      <th>最大割引</th>
      <th>期限</th>
    </tr>
    <logic:iterate id="coupon" name="coupons">
      <tr>
        <td><bean:write name="coupon" property="code" filter="true"/></td>
        <td><bean:write name="coupon" property="couponType" filter="true"/></td>
        <td><bean:write name="coupon" property="discountValue" filter="true"/></td>
        <td><bean:write name="coupon" property="minimumAmount" filter="true"/></td>
        <td><bean:write name="coupon" property="maximumDiscount" filter="true"/></td>
        <td><bean:write name="coupon" property="expiresAt" filter="true"/></td>
      </tr>
    </logic:iterate>
  </table>
</logic:notEmpty>
