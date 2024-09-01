package com.example.stock.service;

import com.example.stock.entity.Stock;
import com.example.stock.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlainStockService implements StockService {

    private static final Logger log = LoggerFactory.getLogger(PlainStockService.class);

    private final StockRepository stockRepository;

    public PlainStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrease(Long id, Long quantity) {
        log.info("decrease stock id : {}, quantity : {}", id, quantity);
        Stock stock = stockRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }
}
