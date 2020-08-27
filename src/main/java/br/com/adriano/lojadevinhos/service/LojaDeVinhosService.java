package br.com.adriano.lojadevinhos.service;


import br.com.adriano.lojadevinhos.consumer.VelasquezClient;
import br.com.adriano.lojadevinhos.consumerdto.ClienteDTO;
import br.com.adriano.lojadevinhos.consumerdto.ItemDTO;
import br.com.adriano.lojadevinhos.consumerdto.VendaDTO;
import br.com.adriano.lojadevinhos.requestdto.QuantidadeTotalPorClienteDTO;
import br.com.adriano.lojadevinhos.requestdto.QuantidadeVinhoDTO;
import br.com.adriano.lojadevinhos.requestdto.ValorTotalClienteDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LojaDeVinhosService {

    private final VelasquezClient velasquezClient;

    public LojaDeVinhosService(VelasquezClient velasquezClient) {
        this.velasquezClient = velasquezClient;
    }

    public ValorTotalClienteDTO buscarCompraDeMaiorValor(Integer ano) throws Exception {
        Optional<ValorTotalClienteDTO> valorTotalClienteDTO = velasquezClient.buscarHistoricoVendas()
                .stream()
                .filter(vendaDTO -> convertStringLocalDate(vendaDTO.getData()).getYear() == ano)
                .max(Comparator.comparing(vendaDTO -> vendaDTO.getValorTotal()))
                .map(vendaDTO -> valorTotalClienteDTOBuild(convertClienteId(vendaDTO.getCliente()), vendaDTO.getValorTotal()));
        if (!valorTotalClienteDTO.isPresent()) {
            throw new Exception("Erro ao buscar dados!");
        }
        return valorTotalClienteDTO.get();
    }


    public List<QuantidadeTotalPorClienteDTO> buscarQuantidadeDeVendasPorCliente() {
        Map<String, Integer> clientesQuantidadeCompras = new HashMap<>();
        for (VendaDTO vendaDTO : velasquezClient.buscarHistoricoVendas()) {
            if (clientesQuantidadeCompras.containsKey(vendaDTO.getCliente())) {
                clientesQuantidadeCompras.put(vendaDTO.getCliente(), clientesQuantidadeCompras.get(vendaDTO.getCliente()) + 1);
            } else {
                clientesQuantidadeCompras.put(vendaDTO.getCliente(), 1);
            }
        }
        return quantidadeTotalPorClienteDTOSListBuild(ordenarMapStringIntegerDecrescente(clientesQuantidadeCompras));
    }

    public List<ValorTotalClienteDTO> buscarVendasPorValorDecrescente() {
        List<ValorTotalClienteDTO> collect = velasquezClient.buscarHistoricoVendas()
                .stream()
                .sorted((c1, c2) -> c2.getValorTotal().compareTo(c1.getValorTotal()))
                .map(vendaDTO -> valorTotalClienteDTOBuild(convertClienteId(vendaDTO.getCliente()), vendaDTO.getValorTotal()))
                .collect(Collectors.toList());
        return collect;

    }

    public List<ValorTotalClienteDTO> buscarVendasPorValorDecrescente2() {

        Map<String, BigDecimal> clientesCompras = new HashMap<>();
        for (VendaDTO vendaDTO : velasquezClient.buscarHistoricoVendas()) {
            if (clientesCompras.containsKey(vendaDTO.getCliente())) {
                clientesCompras.put(vendaDTO.getCliente(), clientesCompras.get(vendaDTO.getCliente()).add(vendaDTO.getValorTotal()));
            } else {
                clientesCompras.put(vendaDTO.getCliente(), vendaDTO.getValorTotal());
            }
        }

        List<ValorTotalClienteDTO> collect = clientesCompras.entrySet()
                .stream()
                .sorted((objeto1, objeto2) -> (objeto2.getValue().compareTo(objeto1.getValue())))
                .map(key -> valorTotalClienteDTOBuild(convertClienteId(key.getKey()), key.getValue()))
                .collect(Collectors.toList());

        return collect;

    }

    public QuantidadeVinhoDTO buscarVinhoMaisVendido() {
        Map<String, Integer> clientesQuantidadeCompras = new HashMap<>();
        for (VendaDTO vendaDTO : velasquezClient.buscarHistoricoVendas()) {
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

    private Map<String, Integer> ordenarMapStringIntegerDecrescente(Map<String, Integer> map) {
        LinkedHashMap<String, Integer> collect = map.entrySet()
                .stream()
                .sorted((objeto1, objeto2) -> Integer.compare(objeto2.getValue(), objeto1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1, LinkedHashMap::new));
        return collect;
    }

    private LocalDate convertStringLocalDate(String data) {
        return LocalDate.parse(data, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    private ClienteDTO buscarClienteId(Integer clienteId) {
        List<ClienteDTO> clienteDTOS = velasquezClient.buscarClientes();

        ClienteDTO clienteDTO = clienteDTOS.stream()
                .filter(c -> clienteId == c.getId())
                .findFirst()
                .get();
        return clienteDTO;

    }

    private Integer convertClienteId(String clienteId) {
        return Integer.parseInt(clienteId.replaceAll("[.]+", ""));
    }

    private ValorTotalClienteDTO valorTotalClienteDTOBuild(Integer clienteId, BigDecimal valorTotal) {
        return new ValorTotalClienteDTO(buscarClienteId(clienteId).getNome(), clienteId, valorTotal);
    }

    private List<QuantidadeTotalPorClienteDTO> quantidadeTotalPorClienteDTOSListBuild(Map<String, Integer> map) {
        List<QuantidadeTotalPorClienteDTO> lista = new ArrayList<>();
        for (String key : map.keySet()) {
            QuantidadeTotalPorClienteDTO dto = new QuantidadeTotalPorClienteDTO(buscarClienteId(convertClienteId(key)).getNome(), convertClienteId(key), map.get(key));
            lista.add(dto);
        }
        return lista;
    }
}
