package com.skishop.web.action.admin;

import com.skishop.dao.shipping.ShippingMethodDao;
import com.skishop.dao.shipping.ShippingMethodDaoImpl;
import com.skishop.domain.shipping.ShippingMethod;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AdminShippingMethodListAction extends Action {
  private final ShippingMethodDao shippingMethodDao = new ShippingMethodDaoImpl();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    List<ShippingMethod> methods = shippingMethodDao.listAll();
    request.setAttribute("shippingMethods", methods);
    return mapping.getInputForward();
  }
}
