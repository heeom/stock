package com.example.stock.facade;

import com.example.stock.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonLockStockFacade {

    private static final Logger log = LoggerFactory.getLogger(RedissonLockStockFacade.class);

    private RedissonClient redissonClient;

    private StockService stockService;

    public RedissonLockStockFacade(RedissonClient redissonClient, StockService plainStockService) {
        this.redissonClient = redissonClient;
        this.stockService = plainStockService;
    }

    public void decrease(Long id, Long quantity) {

        RLock lock = redissonClient.getLock(id.toString());

        try {
            boolean triedLock = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!triedLock) {
                log.info("tried lock failed ");
                return;
            }
            stockService.decrease(id, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
