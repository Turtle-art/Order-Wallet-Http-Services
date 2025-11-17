package com.thulasizwe.wallet.controller;

import com.thulasizwe.wallet.model.Wallet;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/api/wallets")
public class WalletController {
    private final List<Wallet> wallets = new ArrayList<>();

    @PostConstruct
    private void init(){
        Wallet wallet1 = new Wallet(1, "User1", 100.00);
        Wallet wallet2 = new Wallet(2, "User2", 200.00);
        Wallet wallet3 = new Wallet(3, "User3", 300.00);
        wallets.add(wallet1);
        wallets.add(wallet2);
        wallets.add(wallet3);
    }

    @GetMapping
    public List<Wallet> findAll(){
        return wallets;
    }

    @GetMapping("/{id}")
    public Optional<Wallet> findById(@PathVariable Integer id){
        return wallets.stream().filter(wallet -> wallet.id().equals(id)).findFirst();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Wallet wallet){
        wallets.add(wallet);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Wallet wallet, @PathVariable Integer id){
        Optional<Wallet> currentWallet = wallets.stream().filter(w -> w.id().equals(id)).findFirst();
        currentWallet.ifPresent(value -> wallets.set(wallets.indexOf(value), wallet));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        wallets.removeIf(w -> w.id().equals(id));
    }
}
