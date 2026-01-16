package com.skishop.dao.coupon;

import com.skishop.domain.coupon.Coupon;
import java.util.List;

public interface CouponDao {
  Coupon findByCode(String code);

  List<Coupon> listActive();

  void incrementUsedCount(String couponId);

  void decrementUsedCount(String couponId);
}
