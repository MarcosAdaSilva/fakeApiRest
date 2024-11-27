package com.marcosDev.fake_api_us.business.service;

import com.marcosDev.fake_api_us.apiv1.dto.ProductsDTO;
import com.marcosDev.fake_api_us.business.service.converter.ProdutoConverter;
import com.marcosDev.fake_api_us.infrastructure.client.FakeApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FakeApiService {

    private final FakeApiClient cliente;
    private final ProdutoConverter converter;
    private final ProdutoService produtoService;

    public List<ProductsDTO> buscaProdutos() {
        try {

            List<ProductsDTO> dto = cliente.buscarListaProdutos();
            dto.forEach(produto -> {
                Boolean retorno = produtoService.existsPorNome(produto.getNome());
                if (retorno.equals(false)) {
                    produtoService.salvaProdutos(converter.toEntity(produto));
                }


               }
            );
             return produtoService.buscaTodosProdutos();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todos os produtos no banco de dados");
        }
    }
}
