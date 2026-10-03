# Design

## Context

See proposal.md - Why for motivation.

O projeto segue uma arquitetura em camadas:
- **Controller**: Endpoints REST com Spring Web, anotações Swagger para documentação
- **Service**: Orquestração de lógica de negócio e coordenação de dependências
- **Repository**: Persistência com Spring Data JPA (atualmente usa Wallet entity com MySQL/H2)
- **Exception Handling**: GlobalHandler com @ControllerAdvice para tratamento centralizado

A entidade `Wallet` já existe com campos: id, name, cpf, email, password, balance, type (COMMON/MERCHANT). Não há endpoints de criação - apenas de transferência em `TransactionController`.

Padrões existentes que devemos seguir:
- DTOs separados para Request/Response
- Service com @Transactional para operações de escrita
- Validações customizadas quando necessário
- Tratamento de exceções via GlobalHandler

## Goals / Non-Goals

**Goals:**
- Implementar endpoint REST para criação de usuários seguindo o padrão arquitetural existente
- Garantir unicidade de CPF/CNPJ e email via validação de banco de dados
- Validar dados obrigatórios usando Spring Validation
- Retornar respostas apropriadas (201 Created, 400 Bad Request, 409 Conflict)
- Integrar com Swagger seguindo o padrão do TransactionController

**Non-Goals:**
- Autenticação/autorização para o endpoint (será público)
- Validação de formato de CPF/CNPJ (aceita qualquer string não-vazia)
- Validação de formato de email (aceita qualquer string não-vazia com validação básica)
- Hash/encriptação de senha (armazenamento em texto plano como está atualmente)
- Geração de saldo inicial diferente de zero
- Edição ou remoção de usuários (apenas criação)

## Decisions

### Decision 1: Seguir o padrão Controller → Service → Repository
**Rationale**: O projeto já utiliza esse padrão em TransactionController/TransactionService. Manter consistência facilita manutenção e compreensão do código.

**Alternatives considered**:
- Controller direto para Repository: rejeitado por não isolar regras de negócio
- Usar Use Cases separados: considerado over-engineering para uma operação simples de CRUD

**Chosen approach**:
- `UserController`: endpoint POST /users
- `UserService`: orquestra validações e criação
- `WalletRepository`: estendido com métodos de busca por CPF e email

### Decision 2: Usar Wallet entity existente ao invés de criar User entity
**Rationale**: A entidade `Wallet` já representa usuários/lojistas com todos os campos necessários. Criar uma nova entidade `User` duplicaria conceitos e complicaria o modelo.

**Alternatives considered**:
- Criar nova entidade User: rejeitado por duplicação
- Renomear Wallet para User: rejeitado por exigir refatoração massiva do código existente

**Chosen approach**: Reutilizar `Wallet` entity, usar "user" apenas no contexto da API (/users endpoint)

### Decision 3: Validação de unicidade no Service layer
**Rationale**: Validações de negócio (CPF e email únicos) devem ficar no Service para centralizar regras e permitir reuso.

**Alternatives considered**:
- Unique constraint no banco apenas: rejeitado porque queremos retornar 409 Conflict, não 500 Internal Server Error
- Validação no Controller: rejeitado porque viola separation of concerns

**Chosen approach**:
- Service verifica existência via Repository antes de salvar
- Lança exceção customizada (UserAlreadyExistsException) para duplicatas
- GlobalHandler intercepta e retorna 409 Conflict

### Decision 4: Usar Jakarta Validation para campos obrigatórios
**Rationale**: O projeto já usa spring-boot-starter-validation. Usar @NotBlank/@NotNull nas DTOs é idiomático e remove código boilerplate.

**Alternatives considered**:
- Validação manual no Service: rejeitado por ser verboso e propenso a erros
- Validação apenas no Controller: rejeitado porque DTOs validados são mais reutilizáveis

**Chosen approach**:
- UserRequest DTO com @NotBlank em name, cpf, email, password
- @NotNull em type enum
- Spring valida automaticamente com @Valid no Controller

### Decision 5: Retornar DTO separado (UserResponse) sem senha
**Rationale**: Segurança básica - nunca expor senhas em respostas. Usar DTO separado permite controle fino sobre campos expostos.

**Alternatives considered**:
- Retornar Wallet entity diretamente: rejeitado por expor senha e campos internos
- Usar @JsonIgnore em Wallet.password: rejeitado porque afeta todas as respostas, não só criação

**Chosen approach**:
- UserResponse DTO com campos: id, name, cpf, email, type (sem password)
- Mapper manual de Wallet → UserResponse

## Risks / Trade-offs

**[Risk]** Race condition em validação de unicidade → **Mitigation**: Unique constraint no banco como fallback (índices únicos em cpf e email). Se ocorrer, DataIntegrityViolationException será tratada como 409.

**[Risk]** Senhas em texto plano → **Mitigation**: Documentado em Non-Goals. Fora do escopo desta mudança, mas deve ser endereçado futuramente.

**[Trade-off]** Reutilizar Wallet vs criar User entity → Escolhemos reutilizar para simplicidade, mas o nome "Wallet" no domínio é semanticamente sobre carteiras/saldo, não usuários. Aceito porque refatoração massiva está fora do escopo.

**[Trade-off]** Validação básica de CPF/email vs validação completa → Escolhemos validação básica (não-vazio) para esta iteração. Validação de formato pode ser adicionada posteriormente sem breaking changes.
