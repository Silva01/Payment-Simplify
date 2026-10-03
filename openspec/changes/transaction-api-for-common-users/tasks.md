# Tasks

## 1. Mudanças no Schema do Banco de Dados

- [ ] 1.1 Criar TransactionTypeEnum com valores TRANSFER, CREDIT, DEBIT e verificar que o enum compila
- [ ] 1.2 Adicionar campo type à entidade Transaction com anotação @Enumerated e verificar que a entidade compila
- [ ] 1.3 Criar script de migração de banco de dados para adicionar coluna type com padrão TRANSFER para registros existentes e verificar que a migração executa com sucesso via Flyway/Liquibase
- [ ] 1.4 Adicionar constraint CHECK do banco de dados no balance de Wallet (balance >= 0) e verificar que a constraint está criada no banco de dados

## 2. Modelos de Requisição

- [ ] 2.1 Criar classe CreditRequest com campos userId e value e verificar que a classe compila
- [ ] 2.2 Adicionar anotações de validação ao CreditRequest (@NotNull, @Positive) e verificar que as anotações estão presentes
- [ ] 2.3 Criar classe DebitRequest com campos userId e value e verificar que a classe compila
- [ ] 2.4 Adicionar anotações de validação ao DebitRequest (@NotNull, @Positive) e verificar que as anotações estão presentes

## 3. Componentes de Validação

- [ ] 3.1 Criar validador ValidatePositiveAmount implementando interface TransactionValidate e verificar que rejeita valores zero e negativos em testes unitários
- [ ] 3.2 Criar validador ValidateCreditDebitUserType que verifica se o tipo da carteira é COMUM e verificar que rejeita carteiras LOJISTA em testes unitários
- [ ] 3.3 Adaptar ValidateIfHasBalance para trabalhar com contexto de débito (userId único ao invés de payer/payee) e verificar que débito com saldo insuficiente falha em testes unitários

## 4. Implementação da Camada de Serviço

- [ ] 4.1 Adicionar método createCreditTransaction ao TransactionService com anotação @Transactional e verificar que a assinatura do método compila
- [ ] 4.2 Implementar lógica de transação de crédito: validar tipo de usuário, validar valor positivo, buscar carteira, creditar saldo, salvar registro de transação, e verificar que teste unitário com dependências mockadas passa
- [ ] 4.3 Adicionar método createDebitTransaction ao TransactionService com anotação @Transactional e verificar que a assinatura do método compila
- [ ] 4.4 Implementar lógica de transação de débito: validar tipo de usuário, validar valor positivo, validar saldo, buscar carteira, debitar saldo, salvar registro de transação, e verificar que teste unitário com dependências mockadas passa
- [ ] 4.5 Atualizar método factory Transaction.of() ou criar novos métodos factory para lidar com CreditRequest e DebitRequest com campo type apropriado e verificar que registros de transação têm o tipo correto

## 5. Camada de Controller

- [ ] 5.1 Adicionar endpoint POST /transactions/credit ao TransactionController com @PostMapping e @ResponseStatus(HttpStatus.OK) e verificar que o endpoint está registrado executando a aplicação e verificando os mapeamentos do actuator
- [ ] 5.2 Conectar chamada de serviço createCreditTransaction no endpoint de crédito com @RequestBody CreditRequest e verificar que o endpoint compila
- [ ] 5.3 Adicionar anotações Swagger/OpenAPI ao endpoint de crédito (@Operation, @ApiResponse para 200, 400, 403, 404) e verificar que a UI do Swagger mostra a documentação correta
- [ ] 5.4 Adicionar endpoint POST /transactions/debit ao TransactionController com @PostMapping e @ResponseStatus(HttpStatus.OK) e verificar que o endpoint está registrado executando a aplicação e verificando os mapeamentos do actuator
- [ ] 5.5 Conectar chamada de serviço createDebitTransaction no endpoint de débito com @RequestBody DebitRequest e verificar que o endpoint compila
- [ ] 5.6 Adicionar anotações Swagger/OpenAPI ao endpoint de débito (@Operation, @ApiResponse para 200, 400, 403, 404) e verificar que a UI do Swagger mostra a documentação correta

## 6. Tratamento de Exceções

- [ ] 6.1 Adicionar manipulador de exceção ao GlobalHandler para usuário não encontrado (404) e verificar que a exceção retorna o código de status HTTP correto em testes
- [ ] 6.2 Adicionar manipulador de exceção ao GlobalHandler para falhas de validação (400) incluindo campos faltando, valores inválidos, saldo insuficiente e verificar que as mensagens de exceção correspondem aos requisitos da spec em testes
- [ ] 6.3 Adicionar manipulador de exceção ao GlobalHandler para tipo de usuário proibido (403) para usuários LOJISTA e verificar que a exceção retorna status HTTP e mensagem corretos em testes

## 7. Testes de Integração

- [ ] 7.1 Criar teste de integração para transação de crédito bem-sucedida com usuário COMUM e verificar que o saldo aumenta corretamente e o registro de transação existe
- [ ] 7.2 Criar teste de integração para crédito rejeitado com usuário LOJISTA e verificar resposta HTTP 403 e saldo inalterado
- [ ] 7.3 Criar teste de integração para crédito com valor inválido (zero/negativo) e verificar resposta HTTP 400
- [ ] 7.4 Criar teste de integração para crédito com usuário não existente e verificar resposta HTTP 404
- [ ] 7.5 Criar teste de integração para transação de débito bem-sucedida com usuário COMUM e saldo suficiente e verificar que o saldo diminui corretamente e o registro de transação existe
- [ ] 7.6 Criar teste de integração para débito rejeitado com usuário LOJISTA e verificar resposta HTTP 403 e saldo inalterado
- [ ] 7.7 Criar teste de integração para débito com saldo insuficiente e verificar resposta HTTP 400 com mensagem de erro correta
- [ ] 7.8 Criar teste de integração para débito com valor inválido (zero/negativo) e verificar resposta HTTP 400
- [ ] 7.9 Criar teste de integração para débito com usuário não existente e verificar resposta HTTP 404
- [ ] 7.10 Criar teste de integração para rollback atômico quando transação falha após atualização de saldo e verificar que o saldo da carteira permanece inalterado

## 8. Documentação

- [ ] 8.1 Atualizar documentação da API para incluir endpoints de crédito e débito com exemplos de requisição/resposta e verificar que a documentação está precisa e completa
- [ ] 8.2 Adicionar documentação de código (Javadoc) aos novos métodos de serviço, validadores e DTOs e verificar a cobertura da documentação
