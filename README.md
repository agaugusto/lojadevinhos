Problema #1: Venda de Vinhos
Velasquez possui uma loja de vinhos e, ao longo dos anos, guardou dados de seus
clientes e um histórico de compras. Velasquez quer personalizar o atendimento e
contratou você para desenvolver um software.

Para criar esse software o neto do Velasquez (o Velasquinho) disponibilizou uma
API com cadastro de clientes
(http://www.mocky.io/v2/598b16291100004705515ec5) e histórico de compras
(http://www.mocky.io/v2/598b16861100004905515ec7).

# Segue os detalhes para cada solicitação.
 
# 1 - Liste os clientes ordenados pelo maior valor total em compras.
* Retorna Todas as vendas ordenadas pelo valor total
- http://localhost:8080/vendas/maiorvalortotal 

* Rotorna o número de vendas conforme o limite ordenadas pelo valor total
- http://localhost:8080/vendas/maiorvalortotal/{limite} 

# 2 - Mostre o cliente com maior compra única no último ano (2016).
* Retorna maior compra do ano de 2016
- http://localhost:8080/vendas/maiorcompra

* Retorna maior compra do ano informado
- http://localhost:8080/vendas/maiorcompra/{ano}

# 3 - Liste os clientes mais fiéis.
* Retorna a lista com os clientes ordenado pela quantidade de compras
- http://localhost:8080/vendas/porclientes

# 4 - Recomende um vinho para um determinado cliente a partir do histórico de compras.
* Retorna o vinho com maior número de vendas
- http://localhost:8080/vendas/vinhomaisvendido