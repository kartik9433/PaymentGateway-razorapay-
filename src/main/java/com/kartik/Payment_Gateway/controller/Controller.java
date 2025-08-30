package com.kartik.Payment_Gateway.controller;

import com.kartik.Payment_Gateway.entity.PaymentOrder;
import com.kartik.Payment_Gateway.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class Controller {

    @Autowired
    private PaymentService paymentService;

    @PostMapping ("/create-order")
     public ResponseEntity<String> createOrder(@RequestBody PaymentOrder order){
        try {
          String serviceOrder =   paymentService.createOrder(order);
          return   ResponseEntity.ok(serviceOrder);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating order" );
        }
    }
    @PostMapping("update-order")
    public ResponseEntity<String>updateOder(@RequestParam String paymentId,
                                            @RequestParam String orderId,
                                            @RequestParam String status){
            paymentService.updateOrder(paymentId,orderId,status);
            return  ResponseEntity.ok("Oder is updated successfully and email sent");
    }
}
