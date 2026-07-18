package com.salon.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.salon.domain.PaymentMethod;
import com.salon.domain.PaymentOrderStatus;
import com.salon.modal.PaymentOrder;
import com.salon.payload.dto.BookingDTO;
import com.salon.payload.dto.UserDTO;
import com.salon.payload.response.PaymentLinkResponse;
import com.salon.repository.PaymentOrderRepository;
import com.salon.service.PaymentService;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
// import com.stripe.model.billingportal.Session;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String razorpaySecretKey;

    // @Value("${stripe.api.secret}")
    // private String stripeApiSecret;

    @Value("${razorpay.api.secret}")
    private String razorpayApiSecret;

    @Override
    public PaymentLinkResponse createOrder(UserDTO user, BookingDTO booking, PaymentMethod paymentMethod) throws RazorpayException, StripeException {
        
        Long amount = (long) booking.getTotalPrice();

        // creation of payment order
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setUserId(user.getId());
        paymentOrder.setBookingId(booking.getId());
        paymentOrder.setSalonId(booking.getSalonId());

        paymentOrder.setPaymentLinkId("TEMP");
        
        PaymentOrder savedOrder = paymentOrderRepository.save(paymentOrder);

        // Now we will create the Payment link based on razorpay and stripe

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            
            PaymentLink payment = createRazorpayPaymentLink(user,
                                                savedOrder.getAmount(),
                                                savedOrder.getId());

            String paymentUrl = payment.get("short_url");
            String paymentUrlId = payment.get("id");
            
            paymentLinkResponse.setPayment_link_url(paymentUrl);
            paymentLinkResponse.setGetPayment_link_id(paymentUrl);

            savedOrder.setPaymentLinkId(paymentUrlId);

            paymentOrderRepository.save(savedOrder);
        } else {
            String paymentUrl = createStripePaymentLink(user,
                                                    savedOrder.getAmount(),
                                                    savedOrder.getId());
                                paymentLinkResponse.setPayment_link_url(paymentUrl);
        }

        return paymentLinkResponse;

    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        PaymentOrder paymentOrder = paymentOrderRepository.findById(id).orElse(null);
            if (paymentOrder == null) {
                throw new Exception("Payment Order Not Found");
            }
            return paymentOrder;
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {
        return paymentOrderRepository.findByPaymentLinkId(paymentId);
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(UserDTO user, Long amount, Long orderId)
        throws RazorpayException {
        
        Long finalAmount = amount * 100;

            RazorpayClient razorpay = new RazorpayClient(razorpaySecretKey, razorpayApiSecret);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", finalAmount);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());

            customer.put("email", user.getEmail());

            JSONObject notify = new JSONObject();
            notify.put("email", true);

            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("remainder_enable", true);

            paymentLinkRequest.put("callback_url", "http://localhost:3000/payment-success/" + orderId);

            paymentLinkRequest.put("callback_method", "get");

            // paymentLinkRequest.put("reminder_enable", true);

            return razorpay.paymentLink.create(paymentLinkRequest);
    }

    @Override
    public String createStripePaymentLink(UserDTO user, Long amount, Long orderId) throws StripeException{
        
        // first set api key in the Stripe
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                                            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                                            .setMode(SessionCreateParams.Mode.PAYMENT)
                                            .setSuccessUrl("http://localhost:3000/payment-success" + orderId)
                                            .setCancelUrl("http://localhost:3000/payment/cancel")
                                            .addLineItem(SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("usd")
                                            .setUnitAmount(amount*100)
                                            .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Salon Appointment Booking").build()
                                            ).build()
                                        ). build()
                                    ).build();

        Session session = Session.create(params);

        return session.getUrl();
    }

    @Override
    public Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {
        
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {
                RazorpayClient razorpay = new RazorpayClient(razorpaySecretKey, razorpayApiSecret);

                Payment payment = razorpay.payments.fetch(paymentId);
                // Integer amount = payment.get("amount");
                String status = payment.get("status");

                if (status.equals("captured")) {
                     paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                     paymentOrderRepository.save(paymentOrder);
                     return true;
                }
                return true;
            }
            else {
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;

            }
        }
        return false;
    }

}
