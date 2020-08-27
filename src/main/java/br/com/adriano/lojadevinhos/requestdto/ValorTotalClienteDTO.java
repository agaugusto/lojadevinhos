package br.com.adriano.lojadevinhos.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@Builder
public class ValorTotalClienteDTO {
    private String nome;
    private Integer clienteId;
    private BigDecimal valorTotal;
}
