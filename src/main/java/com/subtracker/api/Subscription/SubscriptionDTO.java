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
    private BillingUnit billingUnit;
    private boolean automaticallyRenews;
    private LocalDate startDate;
    private LocalDate nextPaymentDate;
    private PaymentMethod paymentMethod;
    private String paidBy;
    private String category;
    private String url;
    private String notes;
    private SubscriptionStatus status;
    private int ownerId;



    public static SubscriptionDTO fromEntity(Subscription subscription) {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(subscription.getId());
        dto.setName(subscription.getName());
        dto.setPrice(subscription.getPrice());
        dto.setCurrency(subscription.getCurrency());
        dto.setBillingInterval(subscription.getBillingInterval());
        dto.setBillingUnit(subscription.getBillingUnit());
        dto.setAutomaticallyRenews(subscription.isAutomaticallyRenews());
        dto.setStartDate(subscription.getStartDate());
        dto.setNextPaymentDate(subscription.getNextPaymentDate());
        dto.setPaymentMethod(subscription.getPaymentMethod());
        dto.setPaidBy(subscription.getPaidBy());
        dto.setCategory(subscription.getCategory());
        dto.setUrl(subscription.getUrl());
        dto.setNotes(subscription.getNotes());
        dto.setStatus(subscription.getStatus());
        dto.setOwnerId(subscription.getUsers().getUserId());
        return dto;
    }
}
