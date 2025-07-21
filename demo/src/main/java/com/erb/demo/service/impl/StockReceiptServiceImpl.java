package com.erb.demo.service.impl;

import com.erb.demo.model.StockReceipt;
import com.erb.demo.repository.StockReceiptRepository;
import com.erb.demo.service.StockReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockReceiptServiceImpl implements StockReceiptService {

    @Autowired
    private StockReceiptRepository repository;

    @Override
    public List<StockReceipt> getAll() {
        return repository.findAll();
    }

    @Override
    public StockReceipt getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public StockReceipt save(StockReceipt receipt) {
        return repository.save(receipt);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
