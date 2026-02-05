<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>Available Coupons</h2>
<logic:empty name="coupons">
  <p>No available coupons.</p>
</logic:empty>
<logic:notEmpty name="coupons">
  <table border="1">
    <tr>
      <th>Code</th>
      <th>Type</th>
      <th>Discount Value</th>
      <th>Minimum Amount</th>
      <th>Maximum Discount</th>
      <th>Expiration</th>
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
