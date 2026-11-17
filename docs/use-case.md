# Especificação de Caso de Uso — MVP Pizzaria do Seu Antonio

## 1. Contexto do Problema
A pizzaria tradicional do Seu Antonio cresceu via telefone e WhatsApp, mas enfrenta três gargalos operacionais críticos:
1. **Erros manuais** na anotação de pedidos (especialmente pizzas meio a meio e cálculo de valores).
2. **Sobrecarga de atendimento** com clientes ligando para saber o status da entrega (*"Onde está minha pizza?"*).
3. **Desorganização na cozinha** durante os horários de pico nos finais de semana.

---

## 2. Visão Geral dos Casos de Uso (Escopo MVP)

| ID | Caso de Uso | Ator Principal | Dor Resolvida |
| :--- | :--- | :--- | :--- |
| **UC01** | **Realizar Pedido de Pizza** | Atendente / Cliente | Elimina erros de anotação, preço meio a meio e cálculo de troco. |
| **UC02** | Consultar Status do Pedido | Atendente / Cliente | Reduz ligações de acompanhamento via consulta pelo número da comanda. |
| **UC03** | Atualizar Fila de Produção | Cozinha | Organiza a ordem de preparo (*Recebido → Em Preparo → Saiu para Entrega*). |
| **UC04** | Consultar Cardápio | Atendente / Cliente | Padroniza visualização de sabores, tamanhos e preços vigentes. |

---

## 3. Especificação Detalhada: UC01 — Realizar Pedido de Pizza

### 3.1. Identificação e Condições de Estado

| Campo | Descrição |
| :--- | :--- |
| **Identificador** | UC01 |
| **Nome** | Realizar Pedido de Pizza |
| **Ator Principal** | Atendente / Cliente (operador do terminal no MVP) |
| **Atores Secundários** | Módulo de Cozinha e Entregador |
| **Objetivo** | Registrar um pedido com uma ou mais pizzas (inteiras ou meio a meio), calcular o valor total, processar o pagamento com cálculo de troco (quando em dinheiro) e gerar uma comanda padronizada. |
| **Pré-condições** | O sistema deve estar inicializado com o cardápio (sabores, tamanhos e preços) carregado em memória. |
| **Pós-condições** | • Um objeto `Pedido` é armazenado na lista de pedidos ativos com ID único e status `RECEBIDO`.<br>• A comanda registra os sabores, observações, forma de pagamento, valor pago e o **valor exato do troco** para o entregador. |

---

### 3.2. Regras de Negócio Associadas
* **RN01 (Cálculo Pizza Meio a Meio):** Quando uma pizza for composta por dois sabores distintos, o sistema deve cobrar integralmente o valor do sabor de **maior preço** entre as duas metades.
* **RN02 (Cálculo de Troco):** Para pagamentos na modalidade `DINHEIRO`, o valor informado pelo cliente deve ser maior ou igual ao total do pedido, sendo o troco calculado por: `troco = valorPago - valorTotal`. Para `PIX` ou `CARTAO`, o troco é sempre `R$ 0,00`.
* **RN03 (Estado Inicial da Comanda):** Todo pedido confirmado entra na fila da cozinha obrigatoriamente com o status `RECEBIDO`.

---

### 3.3. Fluxo Principal (Caminho Feliz — Pizza Meio a Meio e Pagamento em Dinheiro)
1. O ator seleciona a opção **"1 - Novo Pedido"** no menu principal do terminal.
2. O sistema solicita o nome do cliente e o endereço (ou telefone) de contato.
3. O ator informa os dados de identificação do cliente.
4. O sistema exibe o cardápio numerado com códigos, sabores, tamanhos e valores.
5. O ator escolhe o tamanho da pizza (ex.: Grande) e seleciona a modalidade **Meio a Meio**.
6. O sistema solicita o código do **primeiro sabor** e o código do **segundo sabor**.
7. O ator informa os dois códigos válidos.
8. O sistema aplica a **RN01** (valor do sabor mais caro) e adiciona a pizza ao pedido.
9. O sistema pergunta se o ator deseja adicionar outra pizza ao mesmo pedido.
10. O ator informa que deseja encerrar a inclusão de itens.
11. O sistema exibe o valor total do pedido e solicita a forma de pagamento (`1 - Dinheiro`, `2 - PIX`, `3 - Cartão`).
12. O ator seleciona a opção **`1 - Dinheiro`**.
13. O sistema solicita o valor em dinheiro que o cliente utilizará para pagar (*"Troco para quanto?"*).
14. O ator informa o valor recebido (ex.: `R$ 100,00` para um pedido de `R$ 68,00`).
15. O sistema valida o valor e calcula o troco conforme a **RN02**.
16. O sistema exibe o resumo completo da comanda (dados do cliente, pizzas, valor total, forma de pagamento, valor pago e **troco a levar**) e solicita confirmação.
17. O ator confirma o pedido.
18. O sistema gera o número da comanda, define o status como `RECEBIDO` (**RN03**) e exibe a mensagem de sucesso.

---

### 3.4. Fluxos Alternativos (Variações Válidas)

* **FA01 — Pedido de Pizza Inteira (Passo 5)**
  1. No passo 5, o ator seleciona a modalidade **Inteira**.
  2. O sistema solicita apenas um código de sabor.
  3. O ator informa o código desejado, o sistema atribui o valor integral do sabor e retoma o Fluxo Principal no **passo 8**.

* **FA02 — Inclusão de Múltiplos Itens no Pedido (Passo 9)**
  1. No passo 9, o ator opta por adicionar mais uma pizza.
  2. O sistema mantém os dados do cliente e os itens já adicionados em memória, retornando ao **passo 4**.

* **FA03 — Inserção de Observação de Preparo (Passo 8)**
  1. Antes de concluir a adição da pizza ao pedido, o ator informa uma observação curta (ex.: *"sem cebola"*, *"bem assada"*).
  2. O sistema vincula a observação especificamente àquela pizza na comanda da cozinha.

* **FA04 — Pagamento via PIX ou Cartão (Passo 12)**
  1. No passo 12, o ator seleciona `2 - PIX` ou `3 - Cartão`.
  2. O sistema define o troco como `R$ 0,00` (**RN02**).
  3. O fluxo segue diretamente para o **passo 16** (resumo e confirmação do pedido).

---

### 3.5. Fluxos de Exceção (Tratamento de Erros)

* **FE01 — Código de Sabor Inexistente (Passos 6 e 7)**
  1. O ator digita um código numérico inexistente no cardápio.
  2. O sistema exibe o alerta: *"Código de sabor inválido. Tente novamente."*
  3. O sistema retorna ao **passo 6** sem perder os dados já preenchidos.

* **FE02 — Cancelamento antes da Confirmação Final (Passo 17)**
  1. Ao visualizar o resumo da comanda, o ator opta por cancelar.
  2. O sistema descarta o pedido em construção, não gera comanda para a cozinha e retorna ao menu inicial.

* **FE03 — Entrada de Tipo Inválido no Terminal (Qualquer passo numérico)**
  1. O ator digita letras ou caracteres inválidos onde o sistema espera um número (opção, código ou valor monetário).
  2. O sistema captura a exceção de leitura, exibe mensagem orientativa e repete a pergunta atual.

* **FE04 — Valor em Dinheiro Insuficiente (Passos 14 e 15)**
  1. O ator informa um valor em dinheiro menor que o valor total do pedido (ex.: total de `R$ 68,00` e pagamento de `R$ 50,00`).
  2. O sistema exibe o alerta: *"Valor insuficiente. Informe uma quantia igual ou superior ao total de R$ [valorTotal]."*
  3. O sistema retorna ao **passo 13** para nova digitação do valor pago.