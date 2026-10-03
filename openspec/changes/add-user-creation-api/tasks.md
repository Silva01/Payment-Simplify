# Tasks

## 1. Create DTOs for User Creation API

- [x] 1.1 Create UserRequest DTO with fields (name, cpf, email, password, type) and Jakarta Validation annotations (@NotBlank, @NotNull) and verify the class compiles without errors
- [x] 1.2 Create UserResponse DTO with fields (id, name, cpf, email, type) excluding password and verify the class compiles without errors
- [x] 1.3 Create unit tests for UserRequest validation to verify @NotBlank constraints on name, cpf, email, password and @NotNull on type trigger validation errors when violated

## 2. Extend Repository Layer

- [x] 2.1 Add method `Optional<Wallet> findByCpf(String cpf)` to WalletRepository and verify compilation succeeds
- [x] 2.2 Add method `Optional<Wallet> findByEmail(String email)` to WalletRepository and verify compilation succeeds
- [x] 2.3 Create or update database migration to add unique constraints on cpf and email columns in WALLETS table and verify migration runs successfully

## 3. Create Exception Handling

- [x] 3.1 Create UserAlreadyExistsException extending RuntimeException with constructors for cpf and email conflict messages and verify the class compiles
- [x] 3.2 Add @ExceptionHandler for UserAlreadyExistsException in GlobalHandler returning HTTP 409 Conflict with ErrorMessage and verify compilation succeeds
- [x] 3.3 Add @ExceptionHandler for DataIntegrityViolationException in GlobalHandler returning HTTP 409 Conflict as fallback for race conditions and verify compilation succeeds
- [x] 3.4 Add @ExceptionHandler for MethodArgumentNotValidException in GlobalHandler returning HTTP 400 Bad Request for validation errors and verify compilation succeeds

## 4. Implement Service Layer

- [x] 4.1 Create UserService interface with method `UserResponse createUser(UserRequest request)` and verify compilation succeeds
- [x] 4.2 Create UserServiceImpl implementing UserService with WalletRepository dependency and verify compilation succeeds
- [x] 4.3 Implement createUser method: check CPF uniqueness via findByCpf and throw UserAlreadyExistsException if exists, verify unit test for duplicate CPF passes
- [x] 4.4 Implement createUser method: check email uniqueness via findByEmail and throw UserAlreadyExistsException if exists, verify unit test for duplicate email passes
- [x] 4.5 Implement createUser method: create new Wallet with balance set to BigDecimal.ZERO and save to repository, verify unit test for successful creation passes
- [x] 4.6 Implement createUser method: map saved Wallet to UserResponse (excluding password) and return, verify unit test confirms response does not contain password field
- [x] 4.7 Add @Transactional annotation to createUser method and verify the annotation is present via inspection

## 5. Implement Controller Layer

- [x] 5.1 Create UserController with @RestController and @RequestMapping("/users") annotations and UserService dependency and verify compilation succeeds
- [x] 5.2 Implement POST endpoint method with @PostMapping, @ResponseStatus(HttpStatus.CREATED), @Valid @RequestBody UserRequest parameter returning UserResponse and verify compilation succeeds
- [x] 5.3 Add Swagger annotations (@Tag, @Operation, @ApiResponse) following the pattern from TransactionController and verify compilation succeeds
- [x] 5.4 Create integration test for successful common user creation verifying POST /users returns 201 and correct UserResponse without password
- [x] 5.5 Create integration test for successful merchant creation verifying POST /users returns 201 and correct UserResponse without password
- [x] 5.6 Create integration test for duplicate CPF rejection verifying POST /users returns 409 Conflict with appropriate error message
- [x] 5.7 Create integration test for duplicate email rejection verifying POST /users returns 409 Conflict with appropriate error message
- [x] 5.8 Create integration test for missing name field verifying POST /users returns 400 Bad Request with validation error message
- [x] 5.9 Create integration test for missing cpf field verifying POST /users returns 400 Bad Request with validation error message
- [x] 5.10 Create integration test for missing email field verifying POST /users returns 400 Bad Request with validation error message
- [x] 5.11 Create integration test for missing password field verifying POST /users returns 400 Bad Request with validation error message
- [x] 5.12 Create integration test for missing type field verifying POST /users returns 400 Bad Request with validation error message
- [x] 5.13 Create integration test for invalid type value verifying POST /users returns 400 Bad Request with validation error message
- [x] 5.14 Create integration test verifying new user has balance initialized to zero by querying the database after creation

## 6. Integration Verification

- [x] 6.1 Run full test suite with `mvn test` and verify all tests pass
- [x] 6.2 Start application with `mvn spring-boot:run` and verify it starts without errors
- [x] 6.3 Access Swagger UI at /swagger-ui/index.html and verify POST /users endpoint is documented with correct request/response schemas
- [x] 6.4 Test POST /users via Swagger UI or curl with valid data and verify response is 201 with UserResponse containing id, name, cpf, email, type (no password)
- [x] 6.5 Test POST /users with duplicate CPF and verify response is 409 Conflict
- [x] 6.6 Test POST /users with duplicate email and verify response is 409 Conflict
- [x] 6.7 Test POST /users with missing fields and verify response is 400 Bad Request for each missing field
