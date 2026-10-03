# Spec Delta

## Purpose

Fornece operações de transação de crédito e débito para carteiras de usuários comuns, permitindo depósitos e saques com regras adequadas de validação e autorização.

## ADDED Requirements

### Requirement: System accepts credit transactions via REST API
O sistema SHALL fornecer um endpoint REST para creditar (depositar) fundos na carteira de um usuário comum.

#### Scenario: Successful credit transaction for common user
- **WHEN** uma requisição POST é feita para `/transactions/credit` com dados válidos `{"userId": 1, "value": 100.00}` para um usuário comum (tipo COMUM)
- **THEN** o sistema responde com HTTP 200, aumenta o saldo da carteira do usuário em 100.00, e cria um registro de transação de crédito

#### Scenario: Credit rejected for merchant user
- **WHEN** uma requisição POST é feita para `/transactions/credit` com `{"userId": 2, "value": 50.00}` para um usuário lojista (tipo LOJISTA)
- **THEN** o sistema responde com HTTP 403 Forbidden e mensagem de erro "Credit transactions are not allowed for merchant accounts"

#### Scenario: Credit rejected for non-existent user
- **WHEN** uma requisição POST é feita para `/transactions/credit` com um userId que não existe
- **THEN** o sistema responde com HTTP 404 Not Found e mensagem de erro indicando que o usuário não foi encontrado

#### Scenario: Credit rejected for invalid amount
- **WHEN** uma requisição POST é feita para `/transactions/credit` com valor zero ou negativo
- **THEN** o sistema responde com HTTP 400 Bad Request e mensagem de erro "Transaction value must be positive"

#### Scenario: Credit rejected for missing fields
- **WHEN** uma requisição POST é feita para `/transactions/credit` sem campos obrigatórios (userId ou value)
- **THEN** o sistema responde com HTTP 400 Bad Request e mensagem de erro indicando qual campo está faltando

### Requirement: System accepts debit transactions via REST API
O sistema SHALL fornecer um endpoint REST para debitar (sacar) fundos da carteira de um usuário comum.

#### Scenario: Successful debit transaction for common user
- **WHEN** uma requisição POST é feita para `/transactions/debit` com dados válidos `{"userId": 1, "value": 50.00}` para um usuário comum com saldo suficiente
- **THEN** o sistema responde com HTTP 200, diminui o saldo da carteira do usuário em 50.00, e cria um registro de transação de débito

#### Scenario: Debit rejected for merchant user
- **WHEN** uma requisição POST é feita para `/transactions/debit` com `{"userId": 2, "value": 30.00}` para um usuário lojista (tipo LOJISTA)
- **THEN** o sistema responde com HTTP 403 Forbidden e mensagem de erro "Debit transactions are not allowed for merchant accounts"

#### Scenario: Debit rejected for insufficient balance
- **WHEN** uma requisição POST é feita para `/transactions/debit` com valor maior que o saldo atual do usuário
- **THEN** o sistema responde com HTTP 400 Bad Request e mensagem de erro "Insufficient balance for debit transaction"

#### Scenario: Debit rejected for non-existent user
- **WHEN** uma requisição POST é feita para `/transactions/debit` com um userId que não existe
- **THEN** o sistema responde com HTTP 404 Not Found e mensagem de erro indicando que o usuário não foi encontrado

#### Scenario: Debit rejected for invalid amount
- **WHEN** uma requisição POST é feita para `/transactions/debit` com valor zero ou negativo
- **THEN** o sistema responde com HTTP 400 Bad Request e mensagem de erro "Transaction value must be positive"

#### Scenario: Debit rejected for missing fields
- **WHEN** uma requisição POST é feita para `/transactions/debit` sem campos obrigatórios (userId ou value)
- **THEN** o sistema responde com HTTP 400 Bad Request e mensagem de erro indicando qual campo está faltando

### Requirement: System restricts credit and debit to common users only
O sistema SHALL validar que apenas carteiras com tipo COMUM podem realizar transações de crédito e débito.

#### Scenario: Common user type validation passes
- **WHEN** uma requisição de crédito ou débito é feita para uma carteira com tipo COMUM
- **THEN** o sistema prossegue com a validação e processamento da transação

#### Scenario: Merchant user type validation fails
- **WHEN** uma requisição de crédito ou débito é feita para uma carteira com tipo LOJISTA
- **THEN** o sistema rejeita a transação com HTTP 403 e não modifica o saldo da carteira

### Requirement: System records all credit and debit transactions
O sistema SHALL criar um registro de transação para cada operação de crédito e débito bem-sucedida com timestamp.

#### Scenario: Credit transaction is recorded
- **WHEN** uma transação de crédito é concluída com sucesso
- **THEN** um registro de transação é criado com tipo de transação, ID do usuário, valor, e timestamp de criação

#### Scenario: Debit transaction is recorded
- **WHEN** uma transação de débito é concluída com sucesso
- **THEN** um registro de transação é criado com tipo de transação, ID do usuário, valor, e timestamp de criação

#### Scenario: Failed transactions are not recorded
- **WHEN** uma transação de crédito ou débito falha na validação
- **THEN** nenhum registro de transação é criado no banco de dados

### Requirement: System validates transaction amounts
O sistema SHALL garantir que os valores de transação de crédito e débito sejam números positivos.

#### Scenario: Positive amount validation passes
- **WHEN** uma requisição de transação inclui um valor decimal positivo maior que zero
- **THEN** a validação de valor passa e o processamento continua

#### Scenario: Zero amount validation fails
- **WHEN** uma requisição de transação inclui um valor de exatamente 0.00
- **THEN** o sistema rejeita com HTTP 400 e mensagem de erro "Transaction value must be positive"

#### Scenario: Negative amount validation fails
- **WHEN** uma requisição de transação inclui um valor negativo
- **THEN** o sistema rejeita com HTTP 400 e mensagem de erro "Transaction value must be positive"

### Requirement: System ensures atomic balance updates
O sistema SHALL garantir que as modificações de saldo de carteira para transações de crédito e débito sejam atômicas e consistentes.

#### Scenario: Credit transaction updates balance atomically
- **WHEN** uma transação de crédito é processada
- **THEN** o aumento do saldo da carteira e a criação do registro de transação ocorrem como uma única operação atômica

#### Scenario: Debit transaction updates balance atomically
- **WHEN** uma transação de débito é processada
- **THEN** a diminuição do saldo da carteira e a criação do registro de transação ocorrem como uma única operação atômica

#### Scenario: Failed transaction does not modify balance
- **WHEN** uma validação de transação falha ou ocorre um erro durante o processamento
- **THEN** o saldo da carteira permanece inalterado e nenhuma atualização parcial é persistida
