<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>Product Management</h2>
<p><html:link page="/admin/product/edit.do">Add Product</html:link></p>
<logic:empty name="products">
  <p>No products available.</p>
</logic:empty>
<logic:notEmpty name="products">
  <table border="1">
    <tr>
      <th>ID</th>
      <th>Product Name</th>
      <th>Brand</th>
      <th>Status</th>
      <th>Edit</th>
      <th>Delete</th>
    </tr>
    <logic:iterate id="product" name="products">
      <tr>
        <td><bean:write name="product" property="id" filter="true"/></td>
        <td><bean:write name="product" property="name" filter="true"/></td>
        <td><bean:write name="product" property="brand" filter="true"/></td>
        <td><bean:write name="product" property="status" filter="true"/></td>
        <td>
          <html:link page="/admin/product/edit.do" paramId="id" paramName="product" paramProperty="id">Edit</html:link>
        </td>
        <td>
          <html:form action="/admin/product/delete.do" method="post">
            <html:hidden name="product" property="id"/>
            <html:token/>
            <html:submit value="Delete"/>
          </html:form>
        </td>
      </tr>
    </logic:iterate>
  </table>
</logic:notEmpty>
