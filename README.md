# TODO앱 백엔드 서버 만들기 + Security
### 회원가입, 로그인 기능을 추가해주세요.

## STEP 3 (필수 구현 기능)
- [x] 회원가입, 로그인 기능을 추가해주세요.
- 회원가입, 로그인 기능 JWT Token

<details>
<summary> JwtPlugin </summary>
<div markdown="1">

```kotlin

    @Component
    class JwtPlugin(
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.secret}") private val secret: String,
    @Value("\${auth.jwt.accessTokenExpirationHour}") private val accessTokenExpirationHour: Long
    ) {


    fun validateToken(jwt: String): Result<Jws<Claims>> {
        return kotlin.runCatching {
            val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)
        }
    }

    fun generateAccessToken(subject: String, email: String, role: String): String {
        return generateToken(subject, email, role, Duration.ofHours(accessTokenExpirationHour))
    }

    private fun generateToken(subject: String, email: String, role: String, expirationPeriod: Duration): String {
        val claims: Claims = Jwts.claims()
            .add(mapOf("role" to role, "email" to email))
            .build()

        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
        val now = Instant.now()

        return Jwts.builder()
            .subject(subject)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(expirationPeriod)))
            .claims(claims)
            .signWith(key)
            .compact()
        }
    }
    
```
</div>
</details>


<details>
<summary> UserService </summary>
<div markdown="1">

```kotlin
@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
) {

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email) ?: throw ModelNotFoundException("User", null)

        if (user.role.name != request.role || !passwordEncoder.matches(request.password, user.password)) {
            throw InvalidCredentialException()
        }

        return LoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = user.id.toString(),
                email = user.email,
                role = user.role.name
            )
        )
    }

    fun signUp(request: SignUpRequest): UserResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalStateException("Email is already in use")
        }

        return userRepository.save(
            User(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                profile = Profile(
                    nickname = request.nickname
                ),
                role = when (request.role) {
                    "ADMIN" -> UserRole.ADMIN
                    "GENERAL" -> UserRole.GENERAL
                    else -> throw IllegalArgumentException("Invalid role")
                }
            )
        ).toResponse()
    }

    fun updateUserProfile(userId: Long, updateUserProfileRequest: UpdateUserProfileRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        user.profile = Profile(
            nickname = updateUserProfileRequest.nickname
        )

        return userRepository.save(user).toResponse()
    }

}
```

</div>
</details>

---
- [x] 로그인 한 사용자
- 자신의 할 일 수정, 삭제

<details>
<summary> updateTodo(Controller) </summary>
<div markdown="1">

```kotlin
@PreAuthorize("hasRole('ADMIN') or hasRole('GENERAL')")
@PutMapping("/todos/{todoId}")
fun updateTodo(
    @PathVariable todoId: Long,
    @RequestBody updateTodoRequest: UpdateTodoRequest
): ResponseEntity<TodoResponse> {
    return ResponseEntity.ok(todoService.updateTodo(todoId, updateTodoRequest))
}
```
</div>
</details>

<details>
<summary> updateTodo(Service) </summary>
<div markdown="1">

```kotlin
@Transactional
    fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse {
        val result = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)

        result.title = request.title
        result.content = request.content
        result.writer = request.writer
        return todoRepository.save(result).toTodoResponse()
    }
```
</div>
</details>

<details>
<summary> deleteTodo(Controller) </summary>
<div markdown="1">

```kotlin
@PreAuthorize("hasRole('ADMIN') or hasRole('GENERAL')")
@DeleteMapping("/todos/{todoId}")
    fun deleteTodo(@PathVariable todoId: Long): ResponseEntity<Unit> {
        return ResponseEntity.ok(todoService.deleteTodo(todoId))
}
```
</div>
</details>

<details>
<summary> deleteTodo(Service) </summary>
<div markdown="1">

```kotlin
@Transactional
fun deleteTodo(todoId: Long) {
    val result = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
    todoRepository.delete(result)
}
```
</div>
</details>


- 자신의 댓글 수정, 삭제

<details>
<summary> updateComment(Controller) </summary>
<div markdown="1">

```kotlin
 @PreAuthorize("hasRole('ADMIN') or hasRole('GENERAL')")
    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable todoId: Long,
        @PathVariable commentId: Long,
        @RequestBody  updateCommentRequest: UpdateCommentRequest
    ): ResponseEntity<CommentResponse> {
        val commentUp: CommentResponse = todoService.updateComment(todoId, commentId, updateCommentRequest)
        return ResponseEntity.ok(commentUp)
    }
```
</div>
</details>

<details>
<summary> updateComment(Service) </summary>
<div markdown="1">

```kotlin
@Transactional
    fun updateComment(
        todoId: Long,
        commentId: Long,
        request: UpdateCommentRequest,
    ): CommentResponse {
        val commentUp = commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)

        commentUp.commentContent = request.commentContent

        return commentRepository.save(commentUp).toCommentResponse()
    }
```

</div>
</details>

<details>
<summary> deleteComment(Controller) </summary>
<div markdown="1">

```kotlin
@PreAuthorize("hasRole('ADMIN') or hasRole('GENERAL')")
@DeleteMapping("/{commentId}")
fun deleteComment(
    @PathVariable todoId: Long,
    @PathVariable commentId: Long
): ResponseEntity<Unit> {
    return ResponseEntity.ok(
        todoService.deleteComment(todoId, commentId)
    )
}
```
</div>
</details>

<details>
<summary> deleteComment(Service) </summary>
<div markdown="1">

```kotlin
@Transactional
fun deleteComment(
    todoId: Long,
    commentId: Long
) {
    val todoDe = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
    val commentDe =
        commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("Comment", commentId)
    commentRepository.deleteById(commentId)

    todoDe.deleteComment(commentDe)

    todoRepository.save(todoDe)
}
```
</div>
</details>

