package com.kartik.Payment_Gateway.repo;

import com.kartik.Payment_Gateway.entity.PaymentOrder;
import com.kartik.Payment_Gateway.service.PaymentService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<PaymentOrder,Long> {

    PaymentOrder findByOrderId(String orderId);
}
