package br.com.adriano.lojadevinhos.controller;

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

    @GetMapping("maiorvalor/{ano}")
    public ResponseEntity<?> buscarCompraDeMaiorValor(@PathVariable("ano") Integer ano) throws Exception {
        return new ResponseEntity<>(lojaDeVinhosService.buscarCompraDeMaiorValor(ano), HttpStatus.OK);
    }

    @GetMapping("maiorvalor")
    public ResponseEntity<?> buscarCompraDeMaiorValor() throws Exception {
        return new ResponseEntity<>(lojaDeVinhosService.buscarCompraDeMaiorValor(2016), HttpStatus.OK);
    }

    @GetMapping("porclientes")
    public ResponseEntity<?> buscarQuantidadePorCompradores() {
        return new ResponseEntity<>(lojaDeVinhosService.buscarQuantidadeDeVendasPorCliente(), HttpStatus.OK);
    }

    @GetMapping("maiorvalores")
    public ResponseEntity<?> buscarVendasPorValor() {
        return new ResponseEntity<>(lojaDeVinhosService.buscarVendasPorValorDecrescente(), HttpStatus.OK);
    }

    @GetMapping("vinhomaisvendido")
    public ResponseEntity<?> buscarVinhoMaisVendidos() {
        return new ResponseEntity<>(lojaDeVinhosService.buscarVinhoMaisVendido(), HttpStatus.OK);
    }

}
