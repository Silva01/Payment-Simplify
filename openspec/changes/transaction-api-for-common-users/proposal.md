# Proposal

## Why

O sistema atualmente suporta transações de transferência entre contas, mas não possui APIs dedicadas para operações de crédito (depósito) e débito (saque). Essas operações fundamentais de carteira são necessárias para permitir que usuários comuns adicionem fundos às suas carteiras e realizem saques, fornecendo capacidades completas de gerenciamento de carteira. Lojistas não devem ter acesso a essas operações conforme regras de negócio.

## What Changes

- Adicionar endpoint REST API para transações de crédito (depósitos) que aumenta o saldo da carteira do usuário
- Adicionar endpoint REST API para transações de débito (saques) que diminui o saldo da carteira do usuário
- Aplicar regra de negócio que apenas usuários comuns (tipo COMUM) podem realizar transações de crédito e débito
- Lojistas (tipo LOJISTA) são bloqueados de realizar operações de crédito e débito
- Validar saldo suficiente para operações de débito
- Criar registros de transação para trilha de auditoria de todas as operações de crédito e débito
- Retornar códigos de status HTTP apropriados e mensagens de erro para falhas de validação

## Capabilities

### New Capabilities
- `wallet-credit-debit`: Operações de transação de crédito e débito para carteiras de usuários comuns com regras de validação e autorização

### Modified Capabilities

## Impact

- Novos endpoints REST: `POST /transactions/credit` e `POST /transactions/debit`
- Novos tipos de transação além da funcionalidade de transferência existente
- Modificações de saldo de carteira através de operações de crédito/débito
- Repositório de transações armazenará registros de transações de crédito e débito
- Lógica de validação de tipo de usuário para restringir operações apenas a usuários comuns
- Validação de saldo para operações de débito para prevenir saldos negativos
