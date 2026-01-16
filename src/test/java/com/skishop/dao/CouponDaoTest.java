package com.skishop.dao;

import com.skishop.dao.coupon.CouponDao;
import com.skishop.dao.coupon.CouponDaoImpl;
import com.skishop.domain.coupon.Coupon;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Assert;

public class CouponDaoTest extends DaoTestBase {
  private CouponDao couponDao;

  protected void setUp() throws Exception {
    super.setUp();
    resetDatabase();
    couponDao = new CouponDaoImpl();
  }

  public void testFindByCodeAndUpdateUsage() {
    Coupon coupon = couponDao.findByCode("SAVE10");
    Assert.assertNotNull(coupon);
    Assert.assertEquals("coupon-1", coupon.getId());

    couponDao.incrementUsedCount("coupon-1");
    Coupon updated = couponDao.findByCode("SAVE10");
    Assert.assertEquals(1, updated.getUsedCount());
  }

  public void testSaveOrUpdateAndListAll() {
    Coupon coupon = new Coupon();
    coupon.setId("coupon-2");
    coupon.setCampaignId("camp-1");
    coupon.setCode("SAVE20");
    coupon.setCouponType("PERCENT");
    coupon.setDiscountValue(new BigDecimal("20"));
    coupon.setDiscountType("ORDER");
    coupon.setMinimumAmount(new BigDecimal("1000"));
    coupon.setMaximumDiscount(new BigDecimal("8000"));
    coupon.setUsageLimit(50);
    coupon.setUsedCount(0);
    coupon.setActive(true);
    coupon.setExpiresAt(null);
    couponDao.saveOrUpdate(coupon);

    Coupon loaded = couponDao.findByCode("SAVE20");
    Assert.assertNotNull(loaded);
    Assert.assertEquals(50, loaded.getUsageLimit());

    List<Coupon> coupons = couponDao.listAll();
    Assert.assertTrue(coupons.size() >= 2);
  }
}
