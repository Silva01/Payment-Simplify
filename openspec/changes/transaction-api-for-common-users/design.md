# Design

## Context

O sistema atualmente possui transações de transferência entre carteiras (pagador → recebedor) com fluxos de validação, autorização e notificação. A arquitetura existente inclui:
- `TransactionService` com anotação `@Transactional` para operações atômicas
- Cadeia de validação via componentes `List<TransactionValidate>` (ex: `ValidateIfCommonUser`, `ValidateIfHasBalance`)
- `WalletService` para operações de carteira (buscar, debitar, creditar)
- Entidade `Transaction` persistida no `TransactionRepository`
- Autorização externa via `Authorizator`
- Notificação assíncrona via `NotificationProducer`

As carteiras possuem um campo `type` (`WalletTypeEnum.COMUM` ou `WalletTypeEnum.LOJISTA`). Transações de transferência já impõem que apenas usuários COMUM podem ser pagadores. Veja proposal.md para a motivação do negócio.

## Goals / Non-Goals

**Goals:**
- Fornecer endpoints simples de crédito/débito para operações de carteira única (depósito/saque)
- Reutilizar padrões de validação existentes e persistência de transações
- Aplicar restrição de apenas COMUM para crédito e débito
- Garantir atualizações atômicas de saldo com registros de transação

**Non-Goals:**
- Chamada ao serviço de autorização externa (crédito/débito são operações internas, não requerem aprovação externa ao contrário de transferências)
- Notificações (crédito/débito não envolvem uma segunda parte para notificar)
- Suporte para carteiras de lojistas realizarem crédito/débito
- Modificação do comportamento de transação de transferência existente

## Decisions

### 1. Novos Endpoints de Controller vs Estender TransactionController Existente

**Decision:** Adicionar novos endpoints `POST /transactions/credit` e `POST /transactions/debit` ao `TransactionController` existente.

**Rationale:**
- Mantém todas as operações de transação em um controller para uma superfície de API coesa
- Segue o padrão de namespace `/transaction` existente
- Mais fácil de documentar e descobrir operações relacionadas

**Alternative Considered:** Criar `WalletController` separado para operações específicas de carteira
- Rejeitado: Criaria confusão sobre se operações de carteira pertencem a `/wallets` ou `/transactions`
- Transações já existem, então crédito/débito se encaixam no conceito de transação

### 2. Modelo de Requisição de Transação

**Decision:** Criar novos DTOs de requisição `CreditRequest` e `DebitRequest` com campos `userId` e `value`.

**Rationale:**
- `TransactionRequest` existente tem `payer`, `payee`, `value` - inapropriado para operações de carteira única
- Contrato de API mais claro com modelos dedicados
- Permite regras de validação diferentes por tipo de operação

**Alternative Considered:** Reutilizar `TransactionRequest` com `payee` anulável para crédito, `payer` anulável para débito
- Rejeitado: Semântica confusa, mais difícil de validar, quebra suposições da lógica de transferência existente

### 3. Rastreamento de Tipo de Transação

**Decision:** Estender entidade `Transaction` para incluir um campo `type` (enum: TRANSFER, CREDIT, DEBIT) para distinguir tipos de transação.

**Rationale:**
- Trilha de auditoria precisa diferenciar tipos de transação
- Habilita relatórios e análises futuras
- Mudança mínima de schema

**Alternative Considered:** Usar tabelas separadas `CreditTransaction`, `DebitTransaction`
- Rejeitado: Super-engenharia para diferenciação simples, complica consultas

### 4. Estratégia de Validação

**Decision:** Criar novos validadores `ValidateCreditDebitUserType` (garante COMUM) e `ValidatePositiveAmount` (garante value > 0). Reutilizar `ValidateIfHasBalance` existente para operações de débito.

**Rationale:**
- Segue padrão de cadeia de validação existente
- `ValidateIfCommonUser` é específico de transferência (verifica campo payer), então precisamos de um novo validador para contexto de crédito/débito
- `ValidateIfHasBalance` pode ser reutilizado adaptando-o para trabalhar com contexto de usuário único
- Validação de valor positivo é comum a crédito e débito

**Alternative Considered:** Validação inline nos métodos de serviço
- Rejeitado: Quebra padrão de validação existente, mais difícil de testar independentemente

### 5. Autorização e Notificação

**Decision:** Pular serviço de autorização externa e notificação para operações de crédito/débito.

**Rationale:**
- Crédito/débito são operações internas de carteira, não transferências multi-parte requerendo aprovação externa
- Não há segunda parte para notificar
- Reduz latência e dependências externas para operações simples

**Alternative Considered:** Incluir autorização/notificação para consistência
- Rejeitado: Adiciona overhead desnecessário; specs não requerem

### 6. Organização da Camada de Serviço

**Decision:** Adicionar métodos `createCreditTransaction` e `createDebitTransaction` ao `TransactionService` existente.

**Rationale:**
- Centraliza lógica de transação
- Reutiliza fronteira `@Transactional` e repositório
- Fluxo similar ao `createTransferTransaction` existente

**Alternative Considered:** Criar `CreditDebitService` separado
- Rejeitado: Sobre-separação para operações que compartilham a maior parte da infraestrutura

### 7. Abordagem de Atomicidade

**Decision:** Usar anotação `@Transactional` do Spring nos métodos de serviço para garantir que atualização de carteira e criação de registro de transação sejam atômicas.

**Rationale:**
- Padrão existente já comprovado
- Gerenciamento de transação JPA/Hibernate lida com rollback em exceções
- Atende requisito de spec para atualizações atômicas

## Risks / Trade-offs

**[Risk]** Mudança na entidade Transaction (adicionar campo `type`) requer migração de banco de dados → **Mitigation:** Usar scripts de migração Flyway/Liquibase; compatível com versões anteriores (pode padronizar registros existentes para TRANSFER)

**[Risk]** Saldo de carteira poderia ficar negativo se requisições de débito concorrentes burlarem validação → **Mitigation:** Constraint de banco de dados em balance (CHECK balance >= 0) + nível de isolamento de transação (READ_COMMITTED ou superior)

**[Risk]** Endpoints de crédito/débito poderiam ser abusados sem limitação de taxa → **Mitigation:** Fora do escopo desta mudança; recomendamos adicionar limitação de taxa no nível de API gateway no futuro

**[Trade-off]** Não reutilizar `TransactionRequest` significa mais classes de requisição → Aceito: Contratos de API mais claros superam proliferação de classes

**[Trade-off]** Pular autorização/notificação reduz consistência com fluxo de transferência → Aceito: Semânticas de operação diferentes justificam fluxo diferente; pode adicionar depois se requisitos mudarem
