<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>Coupon Management</h2>
<p><html:link page="/admin/coupon/edit.do">Add Coupon</html:link></p>
<logic:empty name="coupons">
  <p>No coupons available.</p>
</logic:empty>
<logic:notEmpty name="coupons">
  <table border="1">
    <tr>
      <th>Code</th>
      <th>Type</th>
      <th>Discount</th>
      <th>Usage Limit</th>
      <th>Active</th>
      <th>Edit</th>
    </tr>
    <logic:iterate id="coupon" name="coupons">
      <tr>
        <td><bean:write name="coupon" property="code" filter="true"/></td>
        <td><bean:write name="coupon" property="couponType" filter="true"/></td>
        <td><bean:write name="coupon" property="discountValue" filter="true"/></td>
        <td><bean:write name="coupon" property="usageLimit" filter="true"/></td>
        <td><bean:write name="coupon" property="active" filter="true"/></td>
        <td>
          <html:link page="/admin/coupon/edit.do" paramId="code" paramName="coupon" paramProperty="code">Edit</html:link>
        </td>
      </tr>
    </logic:iterate>
  </table>
</logic:notEmpty>
