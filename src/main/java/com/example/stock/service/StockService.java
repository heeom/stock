package com.example.stock.service;

public interface StockService {

    /**
     * 재고조회 -> 재고 수량 감소 -> 갱신된 값 저장
     * @param id 재고 id
     * @param quantity 수량
     */
    void decrease(Long id, Long quantity);
}
