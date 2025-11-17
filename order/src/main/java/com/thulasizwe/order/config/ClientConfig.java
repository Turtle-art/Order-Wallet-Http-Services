package com.thulasizwe.order.config;

import com.thulasizwe.order.service.WalletClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {
    @Bean
    WalletClient walletClient(){
        RestClient walletRestClient = RestClient.builder()
                .baseUrl("http://localhost:8081/v1/api/wallets")
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(walletRestClient))
                .build();

        return factory.createClient(WalletClient.class);
    }
}
