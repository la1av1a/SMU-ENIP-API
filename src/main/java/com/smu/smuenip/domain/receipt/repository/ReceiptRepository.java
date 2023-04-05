package com.smu.smuenip.domain.receipt.repository;

import com.smu.smuenip.domain.receipt.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

}
