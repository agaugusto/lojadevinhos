package br.com.adriano.lojadevinhos.service;

import br.com.adriano.lojadevinhos.Exception.CadastroClienteNotFoundException;
import br.com.adriano.lojadevinhos.Exception.HistoricoNotFoundException;
import br.com.adriano.lojadevinhos.consumer.VelasquezClient;
import br.com.adriano.lojadevinhos.consumerdto.ClienteDTO;
import br.com.adriano.lojadevinhos.consumerdto.ItemDTO;
import br.com.adriano.lojadevinhos.consumerdto.VendaDTO;
import br.com.adriano.lojadevinhos.requestdto.QuantidadeTotalPorClienteDTO;
import br.com.adriano.lojadevinhos.requestdto.QuantidadeVinhoDTO;
import br.com.adriano.lojadevinhos.requestdto.ValorTotalClienteDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LojaDeVinhosServiceTest {

    LojaDeVinhosService service;
    VelasquezClient velasquezClient;

    @Before
    public void setUp() {
        velasquezClient = mock(VelasquezClient.class);
        service = new LojaDeVinhosService(velasquezClient);
    }

    @Test
    public void deveBuscarCompraDeMaiorValorComSucesso() {
        ValorTotalClienteDTO expectativa = ValorTotalClienteDTO.builder()
                .nome("Marcos")
                .clienteId(2)
                .valorTotal(BigDecimal.valueOf(319))
                .build();
        when(velasquezClient.buscarHistoricoVendas()).thenReturn(vendaDTOListBuild());
        when(velasquezClient.buscarClientes()).thenReturn(clienteDTOListBuild());

        try {
            ValorTotalClienteDTO atual = service.buscarCompraDeMaiorValor(2016);
            Assert.assertEquals(expectativa, atual);
        } catch (Exception e) {
            fail("Deve finalizar com sucesso!");
        }
    }

    @Test
    public void deveGerarExceptionBuscarCompraDeMaiorValorNaoEncontrarCodigoCliente() {
        List<ClienteDTO> clienteDTOList = new ArrayList<>();
        clienteDTOList.add(ClienteDTO.builder()
                .id(5)
                .cpf("000.000.000-01")
                .nome("Vinicius")
                .build());
        when(velasquezClient.buscarHistoricoVendas()).thenReturn(vendaDTOListBuild());
        when(velasquezClient.buscarClientes()).thenReturn(clienteDTOList);

        try {
            ValorTotalClienteDTO atual = service.buscarCompraDeMaiorValor(2016);
            fail("Deve gerar HistoricoNotFoundException!");
        } catch (HistoricoNotFoundException e) {
            assertEquals("Erro ao buscar histórico de valores!", e.getMessage());
        } catch (Exception e) {
            fail("Deve gerar HistoricoNotFoundException!");
        }
    }

    @Test
    public void deveGerarExceptionQuandoBuscarCompraDeMaiorValorENaoEncontrarHistorico() {
        when(velasquezClient.buscarClientes()).thenReturn(clienteDTOListBuild());

        try {
            service.buscarCompraDeMaiorValor(2016);
            fail("Deve gerar HistoricoNotFoundException!");
        } catch (HistoricoNotFoundException e) {
            assertEquals("Histório de vendas não encontrado!", e.getMessage());
        } catch (Exception e) {
            fail("Deve gerar HistoricoNotFoundException!");
        }
    }

    @Test
    public void deveGerarExceptionQuandoBuscarCompraDeMaiorValorENaoEncontrarCadastroCliente() {
        when(velasquezClient.buscarHistoricoVendas()).thenReturn(vendaDTOListBuild());

        try {
            service.buscarCompraDeMaiorValor(2016);
            fail("Deve gerar HistoricoNotFoundException!");
        } catch (HistoricoNotFoundException e) {
            assertEquals("Erro ao buscar histórico de valores!", e.getMessage());
        } catch (Exception e) {
            fail("Deve gerar HistoricoNotFoundException!");
        }
    }

    @Test
    public void deveBuscarQuantidadeDeVendasPorClienteComSucesso() {
        List<QuantidadeTotalPorClienteDTO> expectativa = new ArrayList<>();
        expectativa.add(QuantidadeTotalPorClienteDTO.builder()
                .nome("Marcos")
                .id(2)
                .quantidadeDeCompras(2)
                .build());
        expectativa.add(QuantidadeTotalPorClienteDTO.builder()
                .nome("Vinicius")
                .id(1)
                .quantidadeDeCompras(1)
                .build());

        when(velasquezClient.buscarHistoricoVendas()).thenReturn(vendaDTOListBuild());
        when(velasquezClient.buscarClientes()).thenReturn(clienteDTOListBuild());
        try {
            List<QuantidadeTotalPorClienteDTO> atual = service.buscarQuantidadeDeVendasPorCliente();
            assertEquals(expectativa, atual);
        } catch (Exception e) {
            fail("Deve finalizar com sucesso!");
        }
    }

    @Test
    public void deveGerarExceptionQuandoBuscarQuantidadeDeVendasPorClienteENaoEncontrarHistorico() {
        when(velasquezClient.buscarClientes()).thenReturn(clienteDTOListBuild());

        try {
            service.buscarQuantidadeDeVendasPorCliente();
            fail("Deve gerar HistoricoNotFoundException!");
        } catch (HistoricoNotFoundException e) {
            assertEquals("Histório de vendas não encontrado!", e.getMessage());
        } catch (Exception e) {
            fail("Deve gerar HistoricoNotFoundException!");
        }
    }

    @Test
    public void deveGerarExceptionQuandoBuscarQuantidadeDeVendasPorClienteENaoEncontrarCadastroCliente() {
        when(velasquezClient.buscarHistoricoVendas()).thenReturn(vendaDTOListBuild());

        try {
            service.buscarQuantidadeDeVendasPorCliente();
            fail("Deve gerar CadastroClienteNotFoundException!");
        } catch (CadastroClienteNotFoundException e) {
            assertEquals("Erro ao buscar cliente!", e.getMessage());
        } catch (Exception e) {
            fail("Deve gerar CadastroClienteNotFoundException!");
        }
    }

    @Test
    public void deveBuscarVendasPorValorDecrescenteComSucesso() {
        List<ValorTotalClienteDTO> expectativa = new ArrayList<>();
        expectativa.add(ValorTotalClienteDTO.builder()
                .clienteId(2)
                .nome("Marcos")
                .valorTotal(BigDecimal.valueOf(319))
                .build());
        expectativa.add(ValorTotalClienteDTO.builder()
                .clienteId(1)
                .nome("Vinicius")
                .valorTotal(BigDecimal.valueOf(199))
                .build());
        expectativa.add(ValorTotalClienteDTO.builder()
                .clienteId(2)
                .nome("Marcos")
                .valorTotal(BigDecimal.valueOf(199))
                .build());

        when(velasquezClient.buscarHistoricoVendas()).thenReturn(vendaDTOListBuild());
        when(velasquezClient.buscarClientes()).thenReturn(clienteDTOListBuild());
        try {
            List<ValorTotalClienteDTO> atual = service.buscarVendasPorValorDecrescente(10);
            assertEquals(expectativa, atual);
        } catch (Exception e) {
            fail("Deve finalizar com sucesso!");
        }
    }

    @Test
    public void deveGerarExceptionQuandoBuscarVendasPorValorDecrescenteENaoEncontrarHistorico() {
        when(velasquezClient.buscarClientes()).thenReturn(clienteDTOListBuild());

        try {
            service.buscarVendasPorValorDecrescente(10);
            fail("Deve gerar HistoricoNotFoundException!");
        } catch (HistoricoNotFoundException e) {
            assertEquals("Histório de vendas não encontrado!", e.getMessage());
        } catch (Exception e) {
            fail("Deve gerar HistoricoNotFoundException!");
        }
    }

    @Test
    public void deveGerarExceptionQuandoBuscarVendasPorValorDecrescenteENaoEncontrarCadastroCliente() {
        when(velasquezClient.buscarHistoricoVendas()).thenReturn(vendaDTOListBuild());

        try {
            service.buscarVendasPorValorDecrescente(10);
            fail("Deve gerar CadastroClienteNotFoundException!");
        } catch (HistoricoNotFoundException e) {
            assertEquals("Erro ao buscar histórico de vendas!", e.getMessage());
        } catch (Exception e) {
            fail("Deve gerar CadastroClienteNotFoundException!");
        }
    }

    @Test
    public void deveBuscarVinhoMaisVendidoComSucesso() {
        QuantidadeVinhoDTO expectativa = QuantidadeVinhoDTO.builder()
                .nome("Casa Silva Gran Reserva")
                .quantidadeVendida(4)
                .build();
        when(velasquezClient.buscarHistoricoVendas()).thenReturn(vendaDTOListBuild());
        when(velasquezClient.buscarClientes()).thenReturn(clienteDTOListBuild());

        try {
            QuantidadeVinhoDTO atual = service.buscarVinhoMaisVendido();
            assertEquals(expectativa, atual);
        } catch (Exception e) {
            fail("Deve finalizar com sucesso!");
        }
    }

    @Test
    public void deveGerarExceptionQuandoBuscarVinhoMaisVendidoENaoEncontrarHistorico() {
        when(velasquezClient.buscarClientes()).thenReturn(clienteDTOListBuild());

        try {
            service.buscarVinhoMaisVendido();
            fail("Deve gerar HistoricoNotFoundException!");
        } catch (HistoricoNotFoundException e) {
            assertEquals("Histório de vendas não encontrado!", e.getMessage());
        } catch (Exception e) {
            fail("Deve gerar HistoricoNotFoundException!");
        }
    }

    private List<VendaDTO> vendaDTOListBuild() {
        List<VendaDTO> lista = new ArrayList<>();
        lista.add(VendaDTO.builder()
                .cliente("000.000.000.01")
                .data("19-02-2016")
                .valorTotal(BigDecimal.valueOf(199))
                .itens((itemDTOListBuild()))
                .build());

        List<ItemDTO> itemDTOS = itemDTOListBuild();
        itemDTOS.add(itemDTOListBuild().get(1));
        lista.add(VendaDTO.builder()
                .cliente("000.000.000.02")
                .data("10-02-2016")
                .valorTotal(BigDecimal.valueOf(319))
                .itens(itemDTOS)
                .build());

        lista.add(VendaDTO.builder()
                .cliente("000.000.000.02")
                .data("20-02-2016")
                .valorTotal(BigDecimal.valueOf(199))
                .itens(itemDTOListBuild())
                .build());
        return lista;
    }

    private List<ItemDTO> itemDTOListBuild() {
        List<ItemDTO> lista = new ArrayList<>();
        lista.add(ItemDTO.builder()
                .produto("Casa Silva Reserva")
                .preco(BigDecimal.valueOf(79))
                .build());
        lista.add(ItemDTO.builder()
                .produto("Casa Silva Gran Reserva")
                .preco(BigDecimal.valueOf(120))
                .build());

        return lista;
    }

    private List<ClienteDTO> clienteDTOListBuild() {
        List<ClienteDTO> clienteDTOList = new ArrayList<>();
        clienteDTOList.add(ClienteDTO.builder()
                .id(1)
                .cpf("000.000.000-01")
                .nome("Vinicius")
                .build());
        clienteDTOList.add(ClienteDTO.builder()
                .id(2)
                .cpf("000.000.000-02")
                .nome("Marcos")
                .build());
        return clienteDTOList;
    }

}