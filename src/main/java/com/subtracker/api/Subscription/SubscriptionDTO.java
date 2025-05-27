package com.subtracker.api.Subscription;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Data
public class SubscriptionDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private Currency currency;
    private int billingInterval;
    private TimeUnit billingUnit;
    private boolean automaticallyRenews;
    private LocalDate startDate;
    private LocalDate nextPaymentDate;
    private PaymentMethod paymentMethod;
    private String paidBy;
    private String category;
    private String url;
    private String notes;
    private SubscriptionStatus status;
}
