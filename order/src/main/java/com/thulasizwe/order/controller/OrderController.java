package com.thulasizwe.order.controller;

import com.thulasizwe.order.model.OrderRequest;
import com.thulasizwe.order.model.Wallet;
import com.thulasizwe.order.service.WalletClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final WalletClient walletClient;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        var optionalWallet = walletClient.findById(request.walletId());
        if (optionalWallet.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Wallet not found");
        }

        Wallet wallet = optionalWallet.get();

        if (wallet.balance() < request.amount()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient balance");
        }

        double newBalance = wallet.balance() - request.amount();
        Wallet updated = new Wallet(wallet.id(), wallet.username(), newBalance);

        walletClient.update(updated, wallet.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Order processed successfully. New balance: " + newBalance);
    }
}
