package com.marcosDev.fake_api_us.infrastructure.message.Consumer;

import com.marcosDev.fake_api_us.apiv1.dto.ProductsDTO;
import com.marcosDev.fake_api_us.business.service.ProdutoService;
import com.marcosDev.fake_api_us.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class FakeApiConsumer {
    private final ProdutoService produtoService;

    @KafkaListener(topics = "${topic.fake-api.consumer.nome}", groupId = "${topic.fake-api.consumer.group-id}")
    public void consumerProducerProdutos(ProductsDTO productsDTO) {
        try{
            produtoService.salvaProdutoConsumer(productsDTO);
        }catch (Exception exception) {
            throw new BusinessException("Erro ao consumir mensagem do kafka" + exception);
        }
    }
}
