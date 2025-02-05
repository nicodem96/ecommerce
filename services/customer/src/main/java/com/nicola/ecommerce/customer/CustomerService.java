package com.nicola.ecommerce.customer;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicola.ecommerce.exception.CustomerNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public String createCustomer(CustomerRequest request) {
        Customer customer = repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request) {
        Customer customer = repository.findById(request.id())
            .orElseThrow(() -> new CustomerNotFoundException(
                String.format("Non è stato possibile aggiornare i dati dell'utente:: Utente non trovato con ID:: %s",
                request.id())));
        mergeCustomer(customer, request);
        repository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstName())) {
            customer.setFirstName(request.firstName());
        }
        if(StringUtils.isNotBlank(request.lastName())) {
            customer.setLastName(request.lastName());
        }
        if(StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if(request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> findAllCustomers() {
        return repository.findAll().stream()
            .map(mapper::fromCustomer)
            .toList();
    }

    public Boolean existsById(String customerId) {
        return repository.findById(customerId)
            .isPresent();
    }

    public CustomerResponse findById(String customerId) {
        return repository.findById(customerId)
            .map(mapper::fromCustomer)
            .orElseThrow(() -> new CustomerNotFoundException(
                String.format("Utente non trovato con ID:: %s", customerId)));
    }

    public void deleteCustomer(String customerId) {
        repository.deleteById(customerId);
    }

}
