package com.mandacarubroker.controller;

import com.mandacarubroker.domain.stock.RequestStockDTO;
import com.mandacarubroker.domain.stock.Stock;
import com.mandacarubroker.domain.stock.StockRepository;
import com.mandacarubroker.service.StockService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class StockControllerTest {

    @Autowired
    StockRepository stockRepository;

    StockService stockService;

    @Autowired
    EntityManager entityManager;

    private Stock createStock(RequestStockDTO stockDTO) {
        Stock newStock = new Stock(stockDTO);
        this.entityManager.persist(newStock);
        return newStock;
    }

    private void createService() {
        this.stockService = new StockService(this.stockRepository);
    }

    @Test
    @DisplayName("Should get stock successfully if ID exists")
    void getStockById() {
        createService();
        RequestStockDTO request = new RequestStockDTO("AB5", "Company", 12.4);
        Stock stock = this.createStock(request);

        Optional<Stock> foundStock = this.stockService.getStockById(stock.getId());

        Assertions.assertTrue(foundStock.isPresent());
    }

    @Test
    @DisplayName("Shouldn't get stock if ID don't exists")
    void dontGetStockById() {
        createService();
        String id = "not Id";

        Optional<Stock> foundStock = this.stockService.getStockById(id);

        Assertions.assertFalse(foundStock.isPresent());
    }

    @Test
    @DisplayName("Should get all stocks successfully")
    void getAllStocks() {
        createService();
        RequestStockDTO request1 = new RequestStockDTO("AB5", "Company 1", 12.4);
        RequestStockDTO request2 = new RequestStockDTO("ca2", "Company 2", 32.4);
        RequestStockDTO request3 = new RequestStockDTO("CY9", "Company 3", 0.4);
        List<Stock> stockList = new ArrayList<>();
        stockList.add(createStock(request1));
        stockList.add(createStock(request2));
        stockList.add(createStock(request3));

        List<Stock> foundStocks = this.stockService.getAllStocks();

        Assertions.assertArrayEquals(stockList.toArray(), foundStocks.toArray());
    }



}
