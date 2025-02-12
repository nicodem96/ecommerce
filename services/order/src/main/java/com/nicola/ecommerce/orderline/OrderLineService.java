package com.nicola.ecommerce.orderline;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderLineService {

    private final OrderLineRepository repository;
    private final OrderLineMapper mapper;

    public void saveOrderLine(OrderLineRequest request) {
        OrderLine orderLine = mapper.toOrderLine(request);
        repository.save(orderLine);
    }

    public List<OrderLineResponse> findAllByOrderId(Integer orderId) {
        return repository.findAllByOrderId(orderId)
            .stream().map(mapper::toOrderLineResponse).toList();
    }
}
