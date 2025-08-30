package com.kartik.Payment_Gateway.service;
import com.kartik.Payment_Gateway.entity.PaymentOrder;
import com.kartik.Payment_Gateway.repo.PaymentRepo;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private EmailService emailService;

    public String createOrder(PaymentOrder orderDetails) throws RazorpayException {

        RazorpayClient razorpayClient = new RazorpayClient(keyId,keySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",(int)(orderDetails.getAmount()*100));  // Amount is in currency subunits.
        orderRequest.put("currency","INR");
        orderRequest.put("receipt", "txn"+ UUID.randomUUID());

        Order razorpayOrder = razorpayClient.orders.create(orderRequest);
        orderDetails.setOrderId(razorpayOrder.get("id"));
        orderDetails.setStatus("CREATED");
        orderDetails.setLocalDateTime(LocalDateTime.now());

        paymentRepo.save(orderDetails);

        return razorpayOrder.toString();
    }

    public void updateOrder(String paymentId, String orderId, String status) {
        PaymentOrder order = paymentRepo.findByOrderId(orderId);
        order.setPaymentId(paymentId);
        order.setStatus(status);
        paymentRepo.save(order);
        if ("SUCCESS".equalsIgnoreCase(status)){
            emailService.sendMail(order.getEmail(),order.getName(),
                    order.getCourseName(),order.getAmount());
        }
    }
}
