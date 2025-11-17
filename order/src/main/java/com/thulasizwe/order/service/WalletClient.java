package com.thulasizwe.order.service;

import com.thulasizwe.order.model.Wallet;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.Optional;

@HttpExchange
public interface WalletClient {
    @GetExchange
    public List<Wallet> findAll();

    @GetExchange("/{id}")
    public Optional<Wallet> findById(@PathVariable Integer id);

    @PostExchange
    public void create(@RequestBody Wallet wallet);

    @PostExchange("/{id}")
    public void update(@RequestBody Wallet wallet, @PathVariable Integer id);

    @DeleteExchange("/{id}")
    public void delete(@PathVariable Integer id);
}
