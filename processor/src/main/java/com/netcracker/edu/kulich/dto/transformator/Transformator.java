package com.netcracker.edu.kulich.dto.transformator;

import com.netcracker.edu.kulich.dto.OfferDTO;
import com.netcracker.edu.kulich.dto.OrderItemDTO;
import org.springframework.stereotype.Component;

@Component
public class Transformator {
    public OrderItemDTO convertOfferDtoToOrderItemDto(OfferDTO offer) {
        OrderItemDTO orderItem = new OrderItemDTO();
        orderItem.setPrice(offer.getPrice());
        orderItem.setName(offer.getName());
        orderItem.setCategory(offer.getCategory().getName());
        orderItem.setTags(offer.getTags());
        return orderItem;
    }
}
