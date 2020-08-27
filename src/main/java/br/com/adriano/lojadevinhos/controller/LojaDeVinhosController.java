package br.com.adriano.lojadevinhos.controller;

import br.com.adriano.lojadevinhos.Exception.CadastroClienteNotFoundException;
import br.com.adriano.lojadevinhos.Exception.HistoricoNotFoundException;
import br.com.adriano.lojadevinhos.service.LojaDeVinhosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendas/")
public class LojaDeVinhosController {

    private final LojaDeVinhosService lojaDeVinhosService;

    public LojaDeVinhosController(LojaDeVinhosService lojaDeVinhosService) {
        this.lojaDeVinhosService = lojaDeVinhosService;
    }

    @GetMapping("maiorcompra")
    public ResponseEntity<?> buscarCompraDeMaiorValor() throws Exception {
        return new ResponseEntity<>(lojaDeVinhosService.buscarCompraDeMaiorValor(2016), HttpStatus.OK);
    }

    @GetMapping("maiorcompra/{ano}")
    public ResponseEntity<?> buscarCompraDeMaiorValor(@PathVariable("ano") Integer ano) throws Exception {
        return new ResponseEntity<>(lojaDeVinhosService.buscarCompraDeMaiorValor(ano), HttpStatus.OK);
    }

    @GetMapping("porclientes")
    public ResponseEntity<?> buscarQuantidadePorCompradores() throws HistoricoNotFoundException, CadastroClienteNotFoundException {
        return new ResponseEntity<>(lojaDeVinhosService.buscarQuantidadeDeVendasPorCliente(), HttpStatus.OK);
    }

    @GetMapping("maiorvalortotal")
    public ResponseEntity<?> buscarVendasPorValor() throws HistoricoNotFoundException {
        return new ResponseEntity<>(lojaDeVinhosService.buscarVendasPorValorDecrescente(0), HttpStatus.OK);
    }

    @GetMapping("maiorvalortotal/{limite}")
    public ResponseEntity<?> buscarVendasPorValor(@PathVariable("limite") Integer limite) throws HistoricoNotFoundException {
        return new ResponseEntity<>(lojaDeVinhosService.buscarVendasPorValorDecrescente(limite), HttpStatus.OK);
    }

    @GetMapping("vinhomaisvendido")
    public ResponseEntity<?> buscarVinhoMaisVendidos() throws HistoricoNotFoundException {
        return new ResponseEntity<>(lojaDeVinhosService.buscarVinhoMaisVendido(), HttpStatus.OK);
    }

}
