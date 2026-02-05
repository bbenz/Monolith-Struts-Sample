<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title><tiles:getAsString name="title"/></title>
  <link rel="stylesheet" href="<html:rewrite page='/assets/css/app.css'/>" />
</head>
<body>
  <div class="app-header">
    <tiles:insert attribute="header"/>
  </div>
  <div class="site-container">
    <tiles:insert attribute="messages"/>
    <tiles:insert attribute="body"/>
  </div>
  <tiles:insert attribute="footer"/>
</body>
</html>
