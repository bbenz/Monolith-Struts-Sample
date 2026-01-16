<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<div class="site-header">
  <h1><html:link page="/home.do">SkiShop Monolith</html:link></h1>
  <ul>
    <li><html:link page="/products.do">商品一覧</html:link></li>
    <li><html:link page="/cart.do">カート</html:link></li>
    <logic:present name="loginUser" scope="session">
      <li>こんにちは、<bean:write name="loginUser" property="username" filter="true"/></li>
      <li><html:link page="/orders.do">注文履歴</html:link></li>
      <li><html:link page="/points.do">ポイント</html:link></li>
      <li><html:link page="/addresses.do">住所帳</html:link></li>
      <li><html:link page="/logout.do">ログアウト</html:link></li>
      <logic:equal name="loginUser" property="role" value="ADMIN">
        <li><html:link page="/admin/products.do">管理:商品</html:link></li>
        <li><html:link page="/admin/orders.do">管理:注文</html:link></li>
        <li><html:link page="/admin/coupons.do">管理:クーポン</html:link></li>
        <li><html:link page="/admin/shipping.do">管理:配送方法</html:link></li>
      </logic:equal>
    </logic:present>
    <logic:notPresent name="loginUser" scope="session">
      <li><html:link page="/login.do">ログイン</html:link></li>
      <li><html:link page="/register.do">会員登録</html:link></li>
    </logic:notPresent>
  </ul>
</div>
