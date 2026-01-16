package com.skishop.web.action.admin;

import com.skishop.dao.coupon.CouponDao;
import com.skishop.dao.coupon.CouponDaoImpl;
import com.skishop.domain.coupon.Coupon;
import com.skishop.web.form.admin.AdminCouponForm;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class AdminCouponEditAction extends Action {
  private static final String DATE_FORMAT = "yyyy-MM-dd";
  private final CouponDao couponDao = new CouponDaoImpl();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    AdminCouponForm couponForm = (AdminCouponForm) form;
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      String code = request.getParameter("code");
      if (code != null && code.length() > 0) {
        Coupon coupon = couponDao.findByCode(code);
        if (coupon != null) {
          couponForm.setId(coupon.getId());
          couponForm.setCampaignId(coupon.getCampaignId());
          couponForm.setCode(coupon.getCode());
          couponForm.setCouponType(coupon.getCouponType());
          couponForm.setDiscountValue(toStringValue(coupon.getDiscountValue()));
          couponForm.setDiscountType(coupon.getDiscountType());
          couponForm.setMinimumAmount(toStringValue(coupon.getMinimumAmount()));
          couponForm.setMaximumDiscount(toStringValue(coupon.getMaximumDiscount()));
          couponForm.setUsageLimit(coupon.getUsageLimit());
          couponForm.setActive(coupon.isActive());
          couponForm.setExpiresAt(formatDate(coupon.getExpiresAt()));
        }
      } else {
        couponForm.setId(UUID.randomUUID().toString());
        couponForm.setActive(true);
      }
      return mapping.getInputForward();
    }

    if (couponForm.getCode() == null || couponForm.getCode().length() == 0) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.coupon.code"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }

    BigDecimal discountValue;
    try {
      discountValue = new BigDecimal(couponForm.getDiscountValue());
    } catch (NumberFormatException e) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.coupon.discount"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }

    BigDecimal minimumAmount;
    try {
      minimumAmount = parseOptionalDecimal(couponForm.getMinimumAmount());
    } catch (NumberFormatException e) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.coupon.minimum"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
    BigDecimal maximumDiscount;
    try {
      maximumDiscount = parseOptionalDecimal(couponForm.getMaximumDiscount());
    } catch (NumberFormatException e) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.coupon.maximum"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }

    Date expiresAt;
    try {
      expiresAt = parseOptionalDate(couponForm.getExpiresAt());
    } catch (ParseException e) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.coupon.date"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }

    Coupon coupon = new Coupon();
    coupon.setId(couponForm.getId());
    if (coupon.getId() == null || coupon.getId().length() == 0) {
      coupon.setId(UUID.randomUUID().toString());
    }
    coupon.setCampaignId(couponForm.getCampaignId());
    coupon.setCode(couponForm.getCode());
    coupon.setCouponType(couponForm.getCouponType());
    coupon.setDiscountValue(discountValue);
    coupon.setDiscountType(couponForm.getDiscountType());
    coupon.setMinimumAmount(minimumAmount);
    coupon.setMaximumDiscount(maximumDiscount);
    coupon.setUsageLimit(couponForm.getUsageLimit());
    coupon.setActive(couponForm.isActive());
    coupon.setExpiresAt(expiresAt);
    couponDao.saveOrUpdate(coupon);

    return mapping.findForward("success");
  }

  private BigDecimal parseOptionalDecimal(String value) {
    if (value == null || value.trim().length() == 0) {
      return null;
    }
    return new BigDecimal(value.trim());
  }

  private Date parseOptionalDate(String value) throws ParseException {
    if (value == null || value.trim().length() == 0) {
      return null;
    }
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    formatter.setLenient(false);
    return formatter.parse(value.trim());
  }

  private String formatDate(Date date) {
    if (date == null) {
      return null;
    }
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    return formatter.format(date);
  }

  private String toStringValue(BigDecimal value) {
    return value != null ? value.toString() : null;
  }

}
