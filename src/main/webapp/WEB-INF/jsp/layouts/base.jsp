<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title><tiles:getAsString name="title"/></title>
</head>
<body>
  <div id="header">
    <tiles:insert attribute="header"/>
  </div>
  <div id="messages">
    <tiles:insert attribute="messages"/>
  </div>
  <div id="content">
    <tiles:insert attribute="body"/>
  </div>
  <div id="footer">
    <tiles:insert attribute="footer"/>
  </div>
</body>
</html>
