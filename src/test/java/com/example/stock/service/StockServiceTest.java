package com.example.stock.service;

import com.example.stock.entity.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    public void 재고_감소() {
        stockService.decrease(1L, 1L);

        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(99, stock.getQuantity());
    }

    @Test
    public void 동시에_100개_재고_감소_요청() throws InterruptedException {

        int threadCount = 100; // 생성할 스레드 개수 설정

        // 사용할 스레드 관리
        // 고정 스레드풀 생성(동시에 최대 32개의 스레드만 실행되도록 제한)
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // 다른 스레드에서 특정 작업이 완료될 때까지 대기 -> 100개 스레드가 모두 작업을 완료할때까지 대기
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    // 현재 스레드의 작업 끝
                    latch.countDown();
                }
            });
        }

        latch.await(); // 스레드 대기
        Stock stock = stockRepository.findById(1L).orElseThrow();
        // 레이스 컨디션 발생 -> 둘 이상의 Thread가 공유 데이터에 엑세스할 수 있고 동시에 변경을 하려고 할때 발생하는 문제
        assertEquals(0, stock.getQuantity());
        // 해결 방법 : 하나의 스레드가 작업이 완료된 이후에 다른 스레드가 데이터에 접근하도록 변경
    }
}