package br.com.adriano.lojadevinhos.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class QuantidadeTotalPorClienteDTO {

    private String nome;
    private Integer id;
    private Integer quantidadeDeCompras;
}
