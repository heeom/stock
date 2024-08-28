package com.example.stock.service;

import com.example.stock.entity.Stock;
import com.example.stock.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
//@Transactional
public class PlainStockService implements StockService {

    private static final Logger log = LoggerFactory.getLogger(PlainStockService.class);

    private final StockRepository stockRepository;

    public PlainStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     * 재고조회 -> 재고 수량 감소 -> 갱신된 값 저장
     * @param id 재고 id
     * @param quantity 수량
     */
    @Override
    public synchronized void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }
}
