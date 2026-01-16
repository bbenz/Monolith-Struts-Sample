package com.skishop.web.action.admin;

import com.skishop.dao.shipping.ShippingMethodDao;
import com.skishop.dao.shipping.ShippingMethodDaoImpl;
import com.skishop.domain.shipping.ShippingMethod;
import com.skishop.web.form.admin.AdminShippingMethodForm;
import java.math.BigDecimal;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class AdminShippingMethodEditAction extends Action {
  private final ShippingMethodDao shippingMethodDao = new ShippingMethodDaoImpl();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    AdminShippingMethodForm shippingForm = (AdminShippingMethodForm) form;
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      String code = request.getParameter("code");
      if (code != null && code.length() > 0) {
        ShippingMethod method = shippingMethodDao.findByCode(code);
        if (method != null) {
          shippingForm.setId(method.getId());
          shippingForm.setCode(method.getCode());
          shippingForm.setName(method.getName());
          shippingForm.setFee(method.getFee() != null ? method.getFee().toString() : null);
          shippingForm.setActive(method.isActive());
          shippingForm.setSortOrder(method.getSortOrder());
        }
      } else {
        shippingForm.setId(generateId());
        shippingForm.setActive(true);
      }
      return mapping.getInputForward();
    }

    if (shippingForm.getCode() == null || shippingForm.getCode().length() == 0) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.shipping.code"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }

    BigDecimal fee;
    try {
      fee = new BigDecimal(shippingForm.getFee());
    } catch (NumberFormatException e) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.shipping.fee"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }

    ShippingMethod existing = shippingMethodDao.findByCode(shippingForm.getCode());
    ShippingMethod method = new ShippingMethod();
    String methodId = existing != null ? existing.getId() : shippingForm.getId();
    if (methodId == null || methodId.length() == 0) {
      methodId = generateId();
    }
    method.setId(methodId);
    method.setCode(shippingForm.getCode());
    method.setName(shippingForm.getName());
    method.setFee(fee);
    method.setActive(shippingForm.isActive());
    method.setSortOrder(shippingForm.getSortOrder());

    if (existing == null) {
      shippingMethodDao.insert(method);
    } else {
      shippingMethodDao.update(method);
    }

    return mapping.findForward("success");
  }

  private String generateId() {
    return UUID.randomUUID().toString();
  }
}
