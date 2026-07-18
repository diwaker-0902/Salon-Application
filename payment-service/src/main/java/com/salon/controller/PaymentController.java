package com.salon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;
import com.salon.modal.PaymentOrder;
import com.salon.payload.dto.BookingDTO;
import com.salon.payload.dto.UserDTO;
import com.salon.payload.response.PaymentLinkResponse;
import com.salon.service.PaymentService;
import com.stripe.exception.StripeException;
import com.salon.domain.PaymentMethod;

// import com.stripe.model.PaymentMethod;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
        @RequestBody BookingDTO booking,
        @RequestParam PaymentMethod paymentMethod
        ) throws RazorpayException, StripeException {
        UserDTO user =  new UserDTO();
        user.setFullName("RAM");
        user.setEmail("thewalker@gmail.com");
        user.setId(1L);

        PaymentLinkResponse res = paymentService.createOrder(user, booking, paymentMethod);
        return ResponseEntity.ok(res);
    }



    @PostMapping("/{paymentOrderId}")
    public ResponseEntity<PaymentOrder> getPaymentOrderById(
        @PathVariable Long paymentOrderId
        ) throws Exception{

        PaymentOrder res = paymentService.getPaymentOrderById(paymentOrderId);
        return ResponseEntity.ok(res);
    }



    @PatchMapping("/proceed")
    public ResponseEntity<Boolean> proceedPayment (
        @RequestParam String paymentId,
        @RequestParam String paymentLinkId
        ) throws Exception{

        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

        Boolean res = paymentService.proceedPayment(paymentOrder, paymentId, paymentLinkId);
        return ResponseEntity.ok(res);
    }

}
