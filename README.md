# **Order-Wallet-Http-Services**

A lightweight demo showing how two Spring Boot microservices communicate with each other using simple synchronous **RESTful HTTP calls**.

This project includes:

### **1. WalletService**

* Provides basic wallet data
* Supports retrieving a wallet and updating its balance

### **2. OrderService**

* Receives an order request
* Calls the WalletService using a Spring HTTP client
* Checks the customer’s wallet balance before processing the order
* Updates the wallet balance after a successful order

---

## **Purpose**

This project is a **simple learning demo** created to test and understand:

* How **HTTP clients** work in Spring Boot
* How one microservice calls another using synchronous REST communication
* Basic request/response flow between services

Nothing here is production-ready — it's only meant to help understand **service-to-service communication**.

---

## **How It Works**

1. OrderService receives `POST /orders`
2. OrderService calls WalletService:

   * GET `/wallets/{id}` → fetch wallet
   * POST `/wallets/{id}` → update wallet
3. OrderService validates balance
4. Order is “processed” (simulated)

---

## **Testing**

You can test using **Postman**, **cURL**, or mock tests in each service.

Example cURL:

```bash
curl -X POST http://localhost:8080/orders \
     -H "Content-Type: application/json" \
     -d '{"walletId": 1, "amount": 50}'
```

---

## **Goal**

To provide a clean, minimal example of:

* HTTP-based communication between microservices
* Using Spring HTTP Interface clients
* Understanding synchronous inter-service calls
