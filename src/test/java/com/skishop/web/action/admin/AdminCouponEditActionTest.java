package com.skishop.web.action.admin;

import com.skishop.dao.coupon.CouponDao;
import com.skishop.dao.coupon.CouponDaoImpl;
import com.skishop.domain.coupon.Coupon;
import com.skishop.web.action.StrutsActionTestBase;

public class AdminCouponEditActionTest extends StrutsActionTestBase {
  public void testCreateCoupon() throws Exception {
    setLoginUser("admin-1", "ADMIN");
    setRequestPathInfo("/admin/coupon/edit");
    setPostRequest();
    addRequestParameter("id", "coupon-new");
    addRequestParameter("campaignId", "camp-1");
    addRequestParameter("code", "SAVE30");
    addRequestParameter("couponType", "PERCENT");
    addRequestParameter("discountType", "ORDER");
    addRequestParameter("discountValue", "30");
    addRequestParameter("minimumAmount", "1000");
    addRequestParameter("maximumDiscount", "9000");
    addRequestParameter("usageLimit", "20");
    addRequestParameter("active", "on");
    addRequestParameter("expiresAt", "2099-12-31");
    actionPerform();
    verifyForward("success");

    CouponDao couponDao = new CouponDaoImpl();
    Coupon coupon = couponDao.findByCode("SAVE30");
    assertNotNull(coupon);
    assertEquals(20, coupon.getUsageLimit());
  }
}
