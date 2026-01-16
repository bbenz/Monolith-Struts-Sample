<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>商品一覧</h2>
<html:form action="/products.do" method="get">
  <table>
    <tr>
      <th>キーワード</th>
      <td><html:text property="keyword" size="20"/></td>
      <th>カテゴリ</th>
      <td><html:text property="categoryId" size="10"/></td>
      <th>並び替え</th>
      <td>
        <html:select property="sort">
          <html:option value="">指定なし</html:option>
          <html:option value="priceAsc">価格(昇順)</html:option>
          <html:option value="priceDesc">価格(降順)</html:option>
          <html:option value="newest">新着</html:option>
        </html:select>
      </td>
    </tr>
  </table>
  <html:token/>
  <html:submit value="検索"/>
</html:form>

<logic:empty name="productList">
  <p>商品が見つかりませんでした。</p>
</logic:empty>
<logic:notEmpty name="productList">
  <table border="1">
    <tr>
      <th>商品名</th>
      <th>ブランド</th>
      <th>SKU</th>
      <th>カート</th>
    </tr>
    <logic:iterate id="product" name="productList">
      <bean:define id="productId" name="product" property="id" type="java.lang.String"/>
      <tr>
        <td>
          <html:link page="/product.do" paramId="id" paramName="product" paramProperty="id">
            <bean:write name="product" property="name" filter="true"/>
          </html:link>
        </td>
        <td><bean:write name="product" property="brand" filter="true"/></td>
        <td><bean:write name="product" property="sku" filter="true"/></td>
        <td>
          <html:form action="/cart.do" method="post">
            <html:hidden property="productId" value="<%= org.apache.struts.util.ResponseUtils.filter(productId) %>"/>
            <html:text property="quantity" value="1" size="3"/>
            <html:token/>
            <html:submit value="追加"/>
          </html:form>
        </td>
      </tr>
    </logic:iterate>
  </table>
</logic:notEmpty>
