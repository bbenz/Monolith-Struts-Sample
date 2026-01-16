<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>配送方法管理</h2>
<p><html:link page="/admin/shipping/edit.do">配送方法を追加</html:link></p>
<logic:empty name="shippingMethods">
  <p>配送方法がありません。</p>
</logic:empty>
<logic:notEmpty name="shippingMethods">
  <table border="1">
    <tr>
      <th>コード</th>
      <th>名称</th>
      <th>送料</th>
      <th>有効</th>
      <th>並び順</th>
      <th>編集</th>
    </tr>
    <logic:iterate id="method" name="shippingMethods">
      <tr>
        <td><bean:write name="method" property="code" filter="true"/></td>
        <td><bean:write name="method" property="name" filter="true"/></td>
        <td><bean:write name="method" property="fee" filter="true"/></td>
        <td><bean:write name="method" property="active" filter="true"/></td>
        <td><bean:write name="method" property="sortOrder" filter="true"/></td>
        <td>
          <html:link page="/admin/shipping/edit.do" paramId="code" paramName="method" paramProperty="code">編集</html:link>
        </td>
      </tr>
    </logic:iterate>
  </table>
</logic:notEmpty>
