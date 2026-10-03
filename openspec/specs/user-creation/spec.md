# User Creation Specification

## Purpose

Permite a criação de novos usuários (comuns e lojistas) no sistema através de API REST, garantindo unicidade de CPF/CNPJ e e-mail conforme regras de negócio estabelecidas.

## Requirements

### Requirement: System accepts user creation via REST API
The system SHALL provide a REST endpoint to create new users (common users and merchants) with required personal data.

#### Scenario: Successful common user creation
- **WHEN** a POST request is made to `/users` with valid data `{"name": "João Silva", "cpf": "12345678900", "email": "joao@example.com", "password": "securePass123", "type": "COMMON"}`
- **THEN** the system responds with HTTP 201 and returns user data `{"id": 1, "name": "João Silva", "cpf": "12345678900", "email": "joao@example.com", "type": "COMMON"}` without password

#### Scenario: Successful merchant creation
- **WHEN** a POST request is made to `/users` with valid data `{"name": "Loja ABC", "cpf": "98765432100", "email": "loja@example.com", "password": "securePass456", "type": "MERCHANT"}`
- **THEN** the system responds with HTTP 201 and returns merchant data `{"id": 2, "name": "Loja ABC", "cpf": "98765432100", "email": "loja@example.com", "type": "MERCHANT"}` without password

### Requirement: System enforces CPF/CNPJ uniqueness
The system SHALL reject user creation when CPF/CNPJ already exists in the system.

#### Scenario: Duplicate CPF rejection
- **WHEN** a POST request is made to `/users` with a CPF that already exists in database
- **THEN** the system responds with HTTP 409 Conflict and error message indicating CPF is already registered

#### Scenario: Duplicate CNPJ rejection
- **WHEN** a POST request is made to `/users` with a CNPJ that already exists in database
- **THEN** the system responds with HTTP 409 Conflict and error message indicating CNPJ is already registered

### Requirement: System enforces email uniqueness
The system SHALL reject user creation when email already exists in the system.

#### Scenario: Duplicate email rejection
- **WHEN** a POST request is made to `/users` with an email that already exists in database
- **THEN** the system responds with HTTP 409 Conflict and error message indicating email is already registered

### Requirement: System validates mandatory fields
The system SHALL validate that all required fields (name, cpf, email, password, type) are provided and not empty.

#### Scenario: Missing name field
- **WHEN** a POST request is made to `/users` without the name field or with empty name
- **THEN** the system responds with HTTP 400 Bad Request and error message indicating name is required

#### Scenario: Missing CPF field
- **WHEN** a POST request is made to `/users` without the cpf field or with empty cpf
- **THEN** the system responds with HTTP 400 Bad Request and error message indicating CPF is required

#### Scenario: Missing email field
- **WHEN** a POST request is made to `/users` without the email field or with empty email
- **THEN** the system responds with HTTP 400 Bad Request and error message indicating email is required

#### Scenario: Missing password field
- **WHEN** a POST request is made to `/users` without the password field or with empty password
- **THEN** the system responds with HTTP 400 Bad Request and error message indicating password is required

#### Scenario: Missing type field
- **WHEN** a POST request is made to `/users` without the type field
- **THEN** the system responds with HTTP 400 Bad Request and error message indicating type is required

#### Scenario: Invalid type value
- **WHEN** a POST request is made to `/users` with type value other than "COMMON" or "MERCHANT"
- **THEN** the system responds with HTTP 400 Bad Request and error message indicating invalid type value

### Requirement: System initializes user balance to zero
The system SHALL create new users with balance initialized to zero.

#### Scenario: New user has zero balance
- **WHEN** a new user is successfully created
- **THEN** the user's wallet balance is set to 0.00

### Requirement: System does not expose password in responses
The system SHALL never include password in API responses for security reasons.

#### Scenario: Password not in success response
- **WHEN** user creation is successful
- **THEN** the response payload does not contain the password field

#### Scenario: Password not in error response
- **WHEN** user creation fails with validation or conflict error
- **THEN** the error response does not contain or expose the password value
