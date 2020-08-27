package br.com.adriano.lojadevinhos.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class QuantidadeVinhoDTO {
    private String nome;
    private Integer quantidadeVendida;
}
