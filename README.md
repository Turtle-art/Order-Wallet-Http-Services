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

---

# WalletClient HTTP Integration – Comparison

### Usage (Same for All Approaches)

```java
@Service
public class WalletService {

    private final WalletClient walletClient;

    public WalletService(WalletClient walletClient) {
        this.walletClient = walletClient;
    }

    public List<Wallet> getAllWallets() {
        return walletClient.findAll();
    }

    public Optional<Wallet> getWalletById(Integer id) {
        return walletClient.findById(id);
    }
}
```

---

## 1. Using RestTemplate (Classic)

```java
@Configuration
public class ClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WalletClient walletClient(RestTemplate restTemplate) {
        return new WalletClient(restTemplate, "http://localhost:8081/v1/api/wallets");
    }
}
```

```java
public class WalletClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public WalletClient(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<Wallet> findAll() {
        return List.of(restTemplate.getForObject(baseUrl, Wallet[].class));
    }

    public Optional<Wallet> findById(Integer id) {
        return Optional.ofNullable(restTemplate.getForObject(baseUrl + "/" + id, Wallet.class));
    }
}
```

---

## 2. Pre-Spring Boot 4 (RestClient + HttpServiceProxyFactory)

```java
@Configuration
public class ClientConfig {
    @Bean
    public WalletClient walletClient() {
        RestClient walletRestClient = RestClient.builder()
                .baseUrl("http://localhost:8081/v1/api/wallets")
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(walletRestClient);

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        return factory.createClient(WalletClient.class);
    }
}

@HttpExchange
public interface WalletClient {
    @GetExchange
    List<Wallet> findAll();

    @GetExchange("/{id}")
    Optional<Wallet> findById(@PathVariable Integer id);
}
```

---

## 3. Spring Boot 4 Declarative (`@ImportHttpServices`)

```java
@Configuration(proxyBeanMethods = false)
@ImportHttpServices(WalletClient.class)
public class ClientConfig {
}

@HttpExchange(url = "http://localhost:8081/v1/api/wallets", accept = "application/json")
public interface WalletClient {
    @GetExchange
    List<Wallet> findAll();

    @GetExchange("/{id}")
    Optional<Wallet> findById(@PathVariable Integer id);
}
```

---

### Comparison Table

| Feature                       | RestTemplate                               | Pre-Spring Boot 4                                          | Spring Boot 4                                  |
| ----------------------------- | ------------------------------------------ | ---------------------------------------------------------- | ---------------------------------------------- |
| **Configuration**             | `@Bean RestTemplate` + custom WalletClient | `RestClient + RestClientAdapter + HttpServiceProxyFactory` | `@ImportHttpServices` on the interface class   |
| **Bean Creation**             | Manual WalletClient with RestTemplate      | Manual creation via factory                                | Automatic, declarative bean creation by Spring |
| **Base URL**                  | Set in WalletClient constructor            | Set in RestClient.builder().baseUrl(...)                   | Set directly on `@HttpExchange(url = "...")`   |
| **Usage**                     | Same as above                              | Same as above                                              | Same as above                                  |
| **Boilerplate**               | Moderate                                   | Higher (adapter + factory setup)                           | Lower (just annotate interface and import)     |
| **Type-Safety / Declarative** | Manual, less declarative                   | Type-safe via interface + factory                          | Fully type-safe and declarative                |

---

**Key Takeaways**

* **RestTemplate**: classic, manual, synchronous, moderate boilerplate.
* **Pre-Spring Boot 4**: interface-based, type-safe, but needs adapter + factory.
* **Spring Boot 4**: fully declarative, type-safe, minimal boilerplate.


