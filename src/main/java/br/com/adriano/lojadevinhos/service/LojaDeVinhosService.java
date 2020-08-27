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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LojaDeVinhosService {

    private final VelasquezClient velasquezClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(LojaDeVinhosService.class);

    public LojaDeVinhosService(VelasquezClient velasquezClient) {
        this.velasquezClient = velasquezClient;
    }

    public ValorTotalClienteDTO buscarCompraDeMaiorValor(Integer ano) throws HistoricoNotFoundException {
        List<VendaDTO> vendaDTOS = BuscarHistoricoDeCompras();

        Optional<ValorTotalClienteDTO> valorTotalClienteDTO = vendaDTOS
                .stream()
                .filter(vendaDTO -> convertStringLocalDate(vendaDTO.getData()).getYear() == ano)
                .max(Comparator.comparing(vendaDTO -> vendaDTO.getValorTotal()))
                .map(vendaDTO -> {
                    try {
                        return valorTotalClienteDTOBuild(convertClienteId(vendaDTO.getCliente()), vendaDTO.getValorTotal());
                    } catch (CadastroClienteNotFoundException e) {
                        LOGGER.error("Erro ao buscar cadastro cliente ID: ".concat(String.valueOf(convertClienteId(vendaDTO.getCliente()))));
                    }
                    return null;
                });
        if (!valorTotalClienteDTO.isPresent()) {
            throw new HistoricoNotFoundException("Erro ao buscar histórico de valores!");
        }
        return valorTotalClienteDTO.get();
    }

    private List<VendaDTO> BuscarHistoricoDeCompras() throws HistoricoNotFoundException {
        List<VendaDTO> vendaDTOS = velasquezClient.buscarHistoricoVendas();
        if (vendaDTOS.isEmpty()) {
            throw new HistoricoNotFoundException("Histório de vendas não encontrado!");
        }
        return vendaDTOS;
    }


    public List<QuantidadeTotalPorClienteDTO> buscarQuantidadeDeVendasPorCliente() throws HistoricoNotFoundException, CadastroClienteNotFoundException {
        List<VendaDTO> vendaDTOS = BuscarHistoricoDeCompras();
        Map<Integer, Integer> mapVendas = agruparVendas(vendaDTOS);
        return quantidadeTotalPorClienteDTOSListBuild(ordenarMapIntegerIntegerDecrescente(mapVendas));
    }

    private Map<Integer, Integer> agruparVendas(List<VendaDTO> vendaDTOS) {
        Map<Integer, Integer> clientesQuantidadeCompras = new HashMap<>();
        for (VendaDTO vendaDTO : vendaDTOS) {
            var clienteId = convertClienteId(vendaDTO.getCliente());
            if (clientesQuantidadeCompras.containsKey(clienteId)) {
                clientesQuantidadeCompras.put(clienteId, clientesQuantidadeCompras.get(clienteId) + 1);
            } else {
                clientesQuantidadeCompras.put(clienteId, 1);
            }
        }
        return clientesQuantidadeCompras;
    }

    public List<ValorTotalClienteDTO> buscarVendasPorValorDecrescente(Integer limite) throws HistoricoNotFoundException {
        List<VendaDTO> vendaDTOS = BuscarHistoricoDeCompras();
        List<ValorTotalClienteDTO> lista = vendaDTOS
                .stream()
                .sorted((c1, c2) -> c2.getValorTotal().compareTo(c1.getValorTotal()))
                .map(vendaDTO -> {
                    try {
                        return valorTotalClienteDTOBuild(convertClienteId(vendaDTO.getCliente()), vendaDTO.getValorTotal());
                    } catch (CadastroClienteNotFoundException e) {
                        LOGGER.error("Erro ao buscar cadastro cliente ID: ".concat(String.valueOf(convertClienteId(vendaDTO.getCliente()))));
                    }
                    return null;
                })
                .limit(limite > 0 ? limite : vendaDTOS.size())
                .collect(Collectors.toList());
        if (lista.contains(null)) {
            throw new HistoricoNotFoundException("Erro ao buscar histórico de vendas!");
        }
        return lista;
    }

    public QuantidadeVinhoDTO buscarVinhoMaisVendido() throws HistoricoNotFoundException {
        List<VendaDTO> vendaDTOS = BuscarHistoricoDeCompras();
        Map<String, Integer> clientesQuantidadeCompras = new HashMap<>();
        for (VendaDTO vendaDTO : vendaDTOS) {
            for (ItemDTO itemDTO : vendaDTO.getItens()) {
                if (clientesQuantidadeCompras.containsKey(itemDTO.getProduto())) {
                    clientesQuantidadeCompras.put(itemDTO.getProduto(), clientesQuantidadeCompras.get(itemDTO.getProduto()) + 1);
                } else {
                    clientesQuantidadeCompras.put(itemDTO.getProduto(), 1);
                }
            }
        }
        return quantidadeVinhoDTOListBuild(ordenarMapStringIntegerDecrescente(clientesQuantidadeCompras)).get(0);
    }

    private List<QuantidadeVinhoDTO> quantidadeVinhoDTOListBuild(Map<String, Integer> map) {
        return map.entrySet().stream()
                .map(key -> quantidadeVinhoDTOBuild(key.getKey(), key.getValue()))
                .collect(Collectors.toList());
    }


    private QuantidadeVinhoDTO quantidadeVinhoDTOBuild(String nome, Integer quantidade) {
        return QuantidadeVinhoDTO.builder()
                .nome(nome)
                .quantidadeVendida(quantidade)
                .build();
    }

    private Map<Integer, Integer> ordenarMapIntegerIntegerDecrescente(Map<Integer, Integer> map) {
        LinkedHashMap<Integer, Integer> collect;
        collect = map.entrySet()
                .stream()
                .sorted((objeto1, objeto2) -> Integer.compare(objeto2.getValue(), objeto1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1, LinkedHashMap::new));
        return collect;
    }

    private Map<String, Integer> ordenarMapStringIntegerDecrescente(Map<String, Integer> map) {
        LinkedHashMap<String, Integer> collect;
        collect = map.entrySet()
                .stream()
                .sorted((objeto1, objeto2) -> Integer.compare(objeto2.getValue(), objeto1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1, LinkedHashMap::new));
        return collect;
    }

    private LocalDate convertStringLocalDate(String data) {
        return LocalDate.parse(data, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    private ClienteDTO buscarClienteId(Integer clienteId) throws CadastroClienteNotFoundException {
        List<ClienteDTO> clienteDTOS = velasquezClient.buscarClientes();
        if (clienteDTOS.isEmpty()) {
            throw new CadastroClienteNotFoundException("Cadastro de clientes não encontrado!");
        }
        Optional<ClienteDTO> clienteDTO = clienteDTOS.stream()
                .filter(c -> clienteId.equals(c.getId()))
                .findFirst();
        if (!clienteDTO.isPresent()) {
            throw new CadastroClienteNotFoundException("Cliente não encontrado");
        }
        return clienteDTO.get();

    }

    private Integer convertClienteId(String clienteId) {
        return Integer.parseInt(clienteId.replaceAll("[.]+", ""));
    }

    private ValorTotalClienteDTO valorTotalClienteDTOBuild(Integer clienteId, BigDecimal valorTotal) throws CadastroClienteNotFoundException {
        return new ValorTotalClienteDTO(buscarClienteId(clienteId).getNome(), clienteId, valorTotal);
    }

    private List<QuantidadeTotalPorClienteDTO> quantidadeTotalPorClienteDTOSListBuild(Map<Integer, Integer> map) throws CadastroClienteNotFoundException {
        List<QuantidadeTotalPorClienteDTO> lista;
        lista = map.entrySet().stream()
                .map(entry -> {
                    try {
                        return QuantidadeTotalPorClienteDTO.builder().nome(buscarClienteId(entry.getKey()).getNome())
                                .id(entry.getKey())
                                .quantidadeDeCompras(entry.getValue())
                                .build();
                    } catch (CadastroClienteNotFoundException e) {
                        LOGGER.error("Erro ao buscar cliente!");
                    }
                    return null;
                })
                .collect(Collectors.toList());
        if (lista.contains(null)) {
            throw new CadastroClienteNotFoundException("Erro ao buscar cliente!");
        }
        return lista;
    }
}
