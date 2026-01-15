package com.skishop.web.action;

import com.skishop.dao.address.UserAddressDao;
import com.skishop.dao.address.UserAddressDaoImpl;
import com.skishop.domain.address.Address;
import com.skishop.domain.user.User;
import com.skishop.web.form.AddressForm;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class AddressSaveAction extends Action {
  private static final int MAX_ADDRESSES_PER_USER = 10;
  private final UserAddressDao addressDao = new UserAddressDaoImpl();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      return mapping.getInputForward();
    }
    HttpSession session = request.getSession(false);
    User user = session != null ? (User) session.getAttribute("loginUser") : null;
    if (user == null) {
      return mapping.findForward("login");
    }
    List<Address> existing = addressDao.listByUserId(user.getId());
    if (existing != null && existing.size() >= MAX_ADDRESSES_PER_USER) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.address.limit"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
    AddressForm addressForm = (AddressForm) form;
    Address address = new Address();
    address.setId(resolveAddressId(addressForm));
    address.setUserId(user.getId());
    address.setLabel(addressForm.getLabel());
    address.setRecipientName(addressForm.getRecipientName());
    address.setPostalCode(addressForm.getPostalCode());
    address.setPrefecture(addressForm.getPrefecture());
    address.setAddress1(addressForm.getAddress1());
    address.setAddress2(addressForm.getAddress2());
    address.setPhone(addressForm.getPhone());
    address.setDefault(addressForm.getIsDefault());
    Date now = new Date();
    address.setCreatedAt(now);
    address.setUpdatedAt(now);
    addressDao.save(address);
    return mapping.findForward("success");
  }

  private String resolveAddressId(AddressForm form) {
    if (form.getId() != null && form.getId().length() > 0) {
      return form.getId();
    }
    return UUID.randomUUID().toString();
  }
}
