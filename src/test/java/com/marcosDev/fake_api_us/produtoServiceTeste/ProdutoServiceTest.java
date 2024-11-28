package com.marcosDev.fake_api_us.produtoServiceTeste;

import com.marcosDev.fake_api_us.apiv1.dto.ProductsDTO;
import com.marcosDev.fake_api_us.business.service.ProdutoService;
import com.marcosDev.fake_api_us.business.service.converter.ProdutoConverter;
import com.marcosDev.fake_api_us.exceptions.BusinessException;
import com.marcosDev.fake_api_us.exceptions.ConflictException;
import com.marcosDev.fake_api_us.exceptions.UnprocessableEntityException;
import com.marcosDev.fake_api_us.infrastructure.entities.ProdutoEntity;
import com.marcosDev.fake_api_us.infrastructure.message.producer.FakeApiProducer;
import com.marcosDev.fake_api_us.infrastructure.repositories.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ProdutoConverter converter;

    @Mock
    private FakeApiProducer producer;

    private ProdutoEntity produtoEntity;
    private ProductsDTO productsDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produtoEntity = new ProdutoEntity();
        produtoEntity.setNome("Produto Teste");

        productsDTO = new ProductsDTO();
        productsDTO.setNome("Produto Teste");
    }

    @Test
    void salvaProdutos_deveSalvarComSucesso() {
        when(repository.save(any(ProdutoEntity.class))).thenReturn(produtoEntity);

        ProdutoEntity result = produtoService.salvaProdutos(produtoEntity);

        assertNotNull(result);
        assertEquals("Produto Teste", result.getNome());
        verify(repository, times(1)).save(produtoEntity);
    }

    @Test
    void salvaProdutos_deveLancarBusinessException() {
        when(repository.save(any(ProdutoEntity.class))).thenThrow(new RuntimeException("Erro ao salvar"));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> produtoService.salvaProdutos(produtoEntity));

        assertEquals("Erro ao salvar Produtos ", exception.getMessage());
        verify(repository, times(1)).save(produtoEntity);
    }

    @Test
    void salvaProdutoDTO_deveSalvarComSucesso() {
        when(repository.existsByNome("Produto Teste")).thenReturn(false);
        when(converter.toEntity(any(ProductsDTO.class))).thenReturn(produtoEntity);
        when(repository.save(any(ProdutoEntity.class))).thenReturn(produtoEntity);
        when(converter.toDTO(any(ProdutoEntity.class))).thenReturn(productsDTO);

        ProductsDTO result = produtoService.salvaProdutoDTO(productsDTO);

        assertNotNull(result);
        assertEquals("Produto Teste", result.getNome());
        verify(repository, times(1)).existsByNome("Produto Teste");
        verify(repository, times(1)).save(produtoEntity);
    }

    @Test
    void salvaProdutoDTO_deveLancarConflictException() {
        when(repository.existsByNome("Produto Teste")).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class,
                () -> produtoService.salvaProdutoDTO(productsDTO));

        assertEquals("Produto já existente no banco de dados Produto Teste", exception.getMessage());
        verify(repository, times(1)).existsByNome("Produto Teste");
        verify(repository, never()).save(any(ProdutoEntity.class));
    }

    @Test
    void buscaProdutoPorNome_deveRetornarProduto() {
        when(repository.findByNome("Produto Teste")).thenReturn(produtoEntity);
        when(converter.toDTO(any(ProdutoEntity.class))).thenReturn(productsDTO);

        ProductsDTO result = produtoService.buscaProdutoPorNome("Produto Teste");

        assertNotNull(result);
        assertEquals("Produto Teste", result.getNome());
        verify(repository, times(1)).findByNome("Produto Teste");
    }

    @Test
    void buscaProdutoPorNome_deveLancarUnprocessableEntityException() {
        when(repository.findByNome("Produto Inexistente")).thenReturn(null);

        Exception exception = assertThrows(UnprocessableEntityException.class,
                () -> produtoService.buscaProdutoPorNome("Produto Inexistente"));

        assertEquals("Não foram encontrados produtos com o nome Produto Inexistente", exception.getMessage());
        verify(repository, times(1)).findByNome("Produto Inexistente");
    }

    @Test
    void deletaProduto_deveDeletarComSucesso() {
        when(repository.existsByNome("Produto Teste")).thenReturn(true);

        produtoService.deletaProduto("Produto Teste");

        verify(repository, times(1)).deleteByNome("Produto Teste");
    }

    @Test
    void deletaProduto_deveLancarUnprocessableEntityException() {
        when(repository.existsByNome("Produto Inexistente")).thenReturn(false);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> produtoService.deletaProduto("Produto Inexistente"));

        assertEquals("Não foi possível deletar o produto, pois não existe produto com o nome Produto Inexistente", exception.getMessage());
        verify(repository, never()).deleteByNome(anyString());
    }
}
