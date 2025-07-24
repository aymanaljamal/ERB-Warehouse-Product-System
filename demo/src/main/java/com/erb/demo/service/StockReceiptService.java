package com.erb.demo.service;

import com.erb.demo.dto.StockReceiptDto;
import com.erb.demo.model.StockReceipt;

import java.util.List;

public interface StockReceiptService {
    List<StockReceipt> getAll();
    StockReceipt getById(Long id);
    StockReceipt save(StockReceipt receipt);
    void delete(Long id);

    StockReceiptDto mapToDto(StockReceipt receipt);

    List<StockReceiptDto> mapToDtoList(List<StockReceipt> receipts);
}
