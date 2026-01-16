<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>住所帳</h2>
<p><html:link page="/addresses/save.do">新しい住所を追加</html:link></p>
<logic:empty name="addresses">
  <p>登録済み住所がありません。</p>
</logic:empty>
<logic:notEmpty name="addresses">
  <table border="1">
    <tr>
      <th>ラベル</th>
      <th>宛名</th>
      <th>住所</th>
      <th>電話</th>
      <th>既定</th>
    </tr>
    <logic:iterate id="address" name="addresses">
      <tr>
        <td><bean:write name="address" property="label" filter="true"/></td>
        <td><bean:write name="address" property="recipientName" filter="true"/></td>
        <td>
          <bean:write name="address" property="postalCode" filter="true"/>
          <bean:write name="address" property="prefecture" filter="true"/>
          <bean:write name="address" property="address1" filter="true"/>
          <bean:write name="address" property="address2" filter="true"/>
        </td>
        <td><bean:write name="address" property="phone" filter="true"/></td>
        <td>
          <logic:equal name="address" property="default" value="true"><bean:message key="label.yes"/></logic:equal>
          <logic:notEqual name="address" property="default" value="true">-</logic:notEqual>
        </td>
      </tr>
    </logic:iterate>
  </table>
</logic:notEmpty>
