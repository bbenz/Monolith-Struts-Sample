<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<div class="hero">
	<h1>Welcome to Azure SkiShop</h1>
	<p>Start your winter adventure with the highest quality ski and snowboard equipment.</p>
	<div class="hero-actions">
		<html:link page="/products.do" styleClass="btn">Browse Products</html:link>
	</div>
</div>

<h2 class="page-title">Featured Ski Equipment</h2>
<logic:notEmpty name="featuredProducts">
	<div class="products-grid">
		<logic:iterate id="product" name="featuredProducts">
			<bean:define id="pid" name="product" property="id" type="java.lang.String"/>
			<div class="product-card">
				<div class="name">
					<html:link page="/product.do" paramId="id" paramName="product" paramProperty="id">
						<bean:write name="product" property="name"/>
					</html:link>
				</div>
				<div class="price">¥<bean:write name="product" property="price"/></div>
				<div class="tags">
					<logic:notEmpty name="product" property="brand"><span class="tag"><bean:write name="product" property="brand"/></span></logic:notEmpty>
				</div>
				<div>
					<html:link page="/product.do" paramId="id" paramName="product" paramProperty="id" styleClass="btn">View Details</html:link>
				</div>
			</div>
		</logic:iterate>
	</div>
</logic:notEmpty>
<logic:empty name="featuredProducts">
	<p>Featured products coming soon.</p>
</logic:empty>
