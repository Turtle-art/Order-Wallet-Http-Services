package com.thulasizwe.order.config;

import com.thulasizwe.order.service.WalletClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {
    @Bean
    public WalletClient walletClient(){
        RestClient walletRestClient = RestClient.builder()
                .baseUrl("http://localhost:8081/v1/api/wallets")
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(walletRestClient);

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(restClientAdapter)
                .build();

        return factory.createClient(WalletClient.class);
    }
}
