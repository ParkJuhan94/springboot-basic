package org.prgrms.kdt.voucher.repository;

import java.util.Optional;
import java.util.UUID;
import org.prgrms.kdt.order.Voucher;

public interface VoucherRepository {
  Optional<Voucher> findById(UUID voucherId);
  Voucher insert(Voucher voucher);
}