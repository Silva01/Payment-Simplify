# Proposal

## Why

O sistema Payment Simplify atualmente possui a funcionalidade de transferência entre usuários e lojistas, mas não possui endpoints REST para criação de novos usuários (wallets). Para que o sistema seja completo e utilizável, é necessário permitir que novos usuários e lojistas se cadastrem no sistema através de uma API REST, respeitando as regras de negócio já estabelecidas.

## What Changes

- Adicionar endpoint REST `POST /users` para criação de usuários comuns e lojistas
- Implementar validações de unicidade para CPF/CNPJ e e-mail
- Implementar validação de dados obrigatórios (nome completo, CPF/CNPJ, e-mail e senha)
- Retornar resposta adequada com os dados do usuário criado (sem senha)
- Implementar tratamento de erros para casos de duplicidade e validação de dados
- Adicionar documentação Swagger para o novo endpoint

## Capabilities

### New Capabilities
- `user-creation`: Permite a criação de novos usuários (comuns e lojistas) no sistema através de API REST, com validações de unicidade de CPF/CNPJ e e-mail, e validação de dados obrigatórios

### Modified Capabilities
<!-- No existing capabilities are being modified - this is a new feature that adds functionality without changing existing behavior -->

## Impact

**Affected code:**
- Novo controller para gerenciar requisições de criação de usuário
- Novo service para orquestrar a lógica de criação e validações
- Novo use case para implementar as regras de negócio de criação
- Novas classes de request/response DTOs
- Extensão do `WalletRepository` para suportar consultas de unicidade
- Extensão do `GlobalHandler` para tratar novos erros de validação

**APIs:**
- Novo endpoint público: `POST /users`
- Entrada: `{ "name": "string", "cpf": "string", "email": "string", "password": "string", "type": "COMMON|MERCHANT" }`
- Saída de sucesso (201): `{ "id": "long", "name": "string", "cpf": "string", "email": "string", "type": "string" }`
- Saídas de erro: 400 (validação), 409 (conflito de CPF/email), 500 (erro interno)

**Dependencies:**
- Nenhuma nova dependência externa necessária
- Utiliza bibliotecas já existentes: Spring Web, Spring Data JPA, Spring Validation, Lombok

**Systems:**
- Banco de dados: nova query para verificar unicidade antes de inserção
- Integração com Swagger para documentação do endpoint
