<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>クーポン管理</h2>
<p><html:link page="/admin/coupon/edit.do">クーポンを追加</html:link></p>
<logic:empty name="coupons">
  <p>クーポンがありません。</p>
</logic:empty>
<logic:notEmpty name="coupons">
  <table border="1">
    <tr>
      <th>コード</th>
      <th>種別</th>
      <th>割引</th>
      <th>利用上限</th>
      <th>有効</th>
      <th>編集</th>
    </tr>
    <logic:iterate id="coupon" name="coupons">
      <tr>
        <td><bean:write name="coupon" property="code" filter="true"/></td>
        <td><bean:write name="coupon" property="couponType" filter="true"/></td>
        <td><bean:write name="coupon" property="discountValue" filter="true"/></td>
        <td><bean:write name="coupon" property="usageLimit" filter="true"/></td>
        <td><bean:write name="coupon" property="active" filter="true"/></td>
        <td>
          <html:link page="/admin/coupon/edit.do" paramId="code" paramName="coupon" paramProperty="code">編集</html:link>
        </td>
      </tr>
    </logic:iterate>
  </table>
</logic:notEmpty>
