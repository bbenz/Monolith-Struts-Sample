package com.skishop.web.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class TokenTag extends TagSupport {
    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return SKIP_BODY;
        }
        String token = (String) session.getAttribute(org.apache.struts.Globals.TRANSACTION_TOKEN_KEY);
        if (token == null) {
            return SKIP_BODY;
        }
        String name = org.apache.struts.taglib.html.Constants.TOKEN_KEY;
        try {
            JspWriter out = pageContext.getOut();
            out.write("<input type=\"hidden\" name=\"");
            out.write(name);
            out.write("\" value=\"");
            out.write(token);
            out.write("\" />");
        } catch (Exception e) {
            throw new JspException("Error writing token hidden field", e);
        }
        return SKIP_BODY;
    }
}
