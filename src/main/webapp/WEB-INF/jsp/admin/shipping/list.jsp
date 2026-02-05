<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>Shipping Method Management</h2>
<p><html:link page="/admin/shipping/edit.do">Add Shipping Method</html:link></p>
<logic:empty name="shippingMethods">
  <p>No shipping methods available.</p>
</logic:empty>
<logic:notEmpty name="shippingMethods">
  <table border="1">
    <tr>
      <th>Code</th>
      <th>Name</th>
      <th>Shipping Fee</th>
      <th>Active</th>
      <th>Sort Order</th>
      <th>Edit</th>
    </tr>
    <logic:iterate id="method" name="shippingMethods">
      <tr>
        <td><bean:write name="method" property="code" filter="true"/></td>
        <td><bean:write name="method" property="name" filter="true"/></td>
        <td><bean:write name="method" property="fee" filter="true"/></td>
        <td><bean:write name="method" property="active" filter="true"/></td>
        <td><bean:write name="method" property="sortOrder" filter="true"/></td>
        <td>
          <html:link page="/admin/shipping/edit.do" paramId="code" paramName="method" paramProperty="code">Edit</html:link>
        </td>
      </tr>
    </logic:iterate>
  </table>
</logic:notEmpty>
