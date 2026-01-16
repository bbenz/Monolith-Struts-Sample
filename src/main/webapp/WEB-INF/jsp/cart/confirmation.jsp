<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>注文確認</h2>
<logic:present name="order">
  <p>注文番号: <bean:write name="order" property="orderNumber" filter="true"/></p>
  <p>ステータス: <bean:write name="order" property="status" filter="true"/></p>
  <p>合計金額: <bean:write name="order" property="totalAmount" filter="true"/></p>
</logic:present>
<logic:notPresent name="order">
  <p>注文情報がありません。</p>
</logic:notPresent>
<p><html:link page="/orders.do">注文履歴へ</html:link></p>
