Considerando que já temos uma classe/entidade User com sua servisse e repositor correspondentes, o primeiro passo é inserir as dependências – no pom.xml em caso de maven ou settings.yaml no caso de gradle- do spring security e do jjwt (abaixo seguem dependências para pom.xml/maven):
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt</artifactId>
	<version>0.9.1</version>
</dependency>

A seguir, é criar uma classe que voi conter as configurações de segurança da aplicação.
Essa classe deve estender a interface WebSecurityConfigurerAdapter e sobrepor os 3 métodos configure da mesma.
Os métodos configure são (Códigos em kotlin):
1 - onfigure(auth: AuthenticationManagerBuilder) que será utilizado para autenticações de usuários.
2 - configure(http: HttpSecurity) é onde vamos liberar ou restringir acesso aos nossos endpoints.
3 - configure(web: WebSecurity) Configura acesso a recursos de front-end (Arquivos js, css, imagens, etc). 
Exemplo de corpo para o método configure(http: HttpSecurity):
override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/usuario/cadastrar").permitAll()
            .antMatchers("/auth").permitAll()
            .anyRequest().authenticated()
            .and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
Onde:
1.	.antMatchers("/usuario/cadastrar").permitAll() => determina que o endpoint "/usuario/cadastrar" vai ter acesso liberado sem autenticação.
2.	.anyRequest().authenticated() =>  determina que qualquer outro endpoint não liberado por .antMatchers(), só serão liberados depois de autenticação.

3.	.and().csrf().disable() => Csrf é uma abreviação para cross-site request forgery, que é um tipo de ataque hacker que acontece em aplicações web quando é usado login padrão (usuário + senha) e os dados guardados em cookies.
No spring a validação de csrf já vem ativada por padrão e para ter acesso aos endpoints, é preciso primeiro se autenticar com usuário e senha e para isso, é possível, quando não usando .and().csrf().disable(), usar .and().formLogin()  para abrir uma página padrão do spring de login.
Se a autenticação é via token, automaticamente a API está livre de ataque Csrf. Então o método .and().csrf().disable() desabilita isso para o Spring security não fazer a validação do token do csrf.
4.	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) => cria uma validação stateless, ou seja, sem estado. 

Exemplo de classe configurações de segurança:
@EnableWebSecurity
@Configuration
class SecurityConfigurations:WebSecurityConfigurerAdapter(){
    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(authenticationService).passwordEncoder(BCryptPasswordEncoder())
    }

    override fun configure(web: WebSecurity?) {
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/usuario/cadastrar").permitAll()
            .antMatchers("/auth").permitAll()
            .anyRequest().authenticated()
            .and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)


    }
}



Na classe/entidade User, ela deve estender UserDetails e sobrepor seus 7 métodos obrigatórios:
getAuthorities(),etPassword(),getUsername(),isAccountNonExpired(),isAccountNonLocked(),isCredentialsNonExpired() e isEnabled().
Sobre o método getAuthorities(), ele tem que retornar uma lista.
Os outros que retornarem boolean, devem retornar true.
Essa UserDetails a interface para dizer que essa é a classe que tem detalhes de um usuário.
Exemplo de classe User:
@Entity
class User:UserDetails{
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
        @field:NotBlank(message = "Email não pode estar em branco!")
        @field:Email(message = "Email informado é inválido!")
        @field:Column(unique = true)
        var login: String = ""
        @field:NotBlank
        @field:Size(min = 6)
        var pass: String = ""
        var registerDate = FormatedDate.formatter(LocalDate.now()) 

        constructor(email:String, password:String){
                val bCrypt = BCryptPasswordEncoder()
                this.login = email
                this.pass = bCrypt.encode(password)
        }

        fun comparePassword(passwordFromRequest: String):Boolean{
                val bCrypt = BCryptPasswordEncoder()
                return bCrypt.matches(passwordFromRequest,this.pass)
        }

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
                return mutableListOf()
        }

        override fun getPassword(): String {
                return this.pass
        }

        override fun getUsername(): String {
                return this.login
        }

        override fun isAccountNonExpired(): Boolean {
                return true
        }

        override fun isAccountNonLocked(): Boolean {
                return true
        }

        override fun isCredentialsNonExpired(): Boolean {
                return true
        }

        override fun isEnabled(): Boolean {
                return true
        }

}

Agora precisamos ensinar para o Spring como vai ser o processo de autenticação do nosso projeto.
Na classe configurações de segurança (a que estende WebSecurityConfigurerAdapter), vamos chamar a service que faz autenticação no método configure(auth: AuthenticationManagerBuilder), mas para isso, é claro, precisamos criar essa classe/servisse de autenticação.
Para dizermos para o Spring que essa service é a service que tem a lógica de autenticação, também vamos usar uma interface. Vamos implementar a interface UserDetailsService.
Teremos que sobrepor o método loadUserByUsername(username: String): UserDetails.
No corpo dele é feita a consulta no banco por um usuário cujo nome é igual ao passado no argumento. Esse nome pode ser qualquer coisa, pois ele vai ser o login da dupla login + senha para autenticação de um usuário.
Exemplo de servisse de autenticação:
@Service
class AuthenticationService:UserDetailsService {
    @Autowired
    lateinit var service: UserService
    override fun loadUserByUsername(username: String): UserDetails {
        var user = service.findByLogin(username)
        if(user.isPresent){
            return user.get()
        }

        throw Exception("Usuário não encontrado!")

    }
}
Na linha 4 acima, onde temos lateinit var service: UserService, subtende se que a servisse e a interface repository referente a User já tenham sido criadas ambas com seu método findByLogin(username).

Após isso, voltamos na nossa classe de configurações de segurança e injetamos essa classe criada acima:

@Autowired
    lateinit var authenticationService: AuthenticationService

E no método configure(auth: AuthenticationManagerBuilder) chamamos assim:

override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(authenticationService).passwordEncoder(BCryptPasswordEncoder())
    }

A autenticação tradicional, com usuário e senha, e o servidor, sempre que o usuário efetuando login, cria uma sessão para guardar essas informações. Mas isso não é uma boa prática no modelo REST. Isso vai contra um dos princípios do REST, que é de ser stateless. O ideal é que a nossa autenticação seja stateless.
No modelo trandicional, para cada usuário que estiver logado na aplicação vou ter um espaço na memória armazenando as informações. Isso consome espaço de memória, e se o servidor cair vou perder todas as sessões. Se eu quiser ter escalabilidade, se eu quiser ter um balanceamento de carga com múltiplos servidores, eu teria problema de compartilhamento.
No modelo REST o ideal é trabalhar com a autenticação de maneira stateless. Com o Spring security é possível fazer isso. Conseguimos explicar para o Spring que não é para ele criar a sessão, que toda vez que o usuário se logar vou fazer a lógica de autenticação, mas não é para criar uma session. Só que aí, nas próximas requisições que os clientes dispararem, o servidor não sabe se está logado ou não, porque não tem nada armazenado. O cliente vai ter que mandar alguma informação dizendo quem é ele, se ele está logado, se tem permissão para acessar. Isso geralmente é feito via tokens.

Para tornar a aplicação stateless, primeiramente precisamos configurar para autenticação de maneira programática.
Para fazer uma autenticação de maneira programática, manualmente, no Spring security, vamos precisar de uma classe chamada authentication manager. Precisamos injetar no nosso controller. Declaramos um atributo que vai ser injetado, do tipo authenticationManager. Chamaremos o atributo de authManager.
Porém, tem um pequeno detalhe. Essa classe é do Spring, mas ele não consegue fazer a injeção de dependências dela automaticamente, a não ser que nós configuremos isso. Por algum motivo, ela não vem configurada. Podemos fazer isso na nossa classe de configurações de segurança sobrecarregando o método authenticationManager(): AuthenticationManager da interface WebSecurityConfigurerAdapter.
A única coisa que vamos precisar fazer é colocar @Bean em cima do authenticationManager(): AuthenticationManager, porque aí o Spring sabe que esse método devolve o authenticationManager e conseguimos injetar no nosso controller. 
Exemplo:

@Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

Agora vamos elaborar o código que devolve o token. 
Para gerar o token é que vamos usar a biblioteca JWT, que até então só tínhamos colocado no projeto e não utilizado.
Para não deixar esse código solto, vamos isolar em uma classe service.
Exemplo:

@Service
class TokenService {

    fun generateToken(authentication: Authentication):String{
        val loggedInUser = authentication.principal as User
        val today = Date()
        val expirationDate = Date(today.time + 900000)
        return Jwts.builder()
            .setIssuer("Api Mercado Livre")
            .setSubject(loggedInUser.id.toString())
            .setIssuedAt(today)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 60  *1000))
            .signWith(SignatureAlgorithm.HS512,"secret").compact()
    }
}

Criamos agora uma DTO para ser usada ao retornar a chave token e seu tipo:

class TokenDto {
    var token:String = ""
    var type:String = ""

    constructor(token: String, type: String) {
        this.token = token
        this.type = type
    }
}

Depois criamos uma controller de autenticação.
Esse é o controller onde vai estar a lógica de autenticação e contém o endpoint chamado pelo client para logar o usuário no sistema.
Exemplo de controller para autenticação com geração de token:

@RestController
@RequestMapping("/auth")
class AuthenticationController {

    @Autowired
    lateinit var authManager:AuthenticationManager
    @Autowired
    lateinit var tokenService:TokenService

    @PostMapping
    fun authenticate(@RequestBody @Valid loginDto: LoginDto):ResponseEntity<Any>{
        var LoginDatas: UsernamePasswordAuthenticationToken = loginDto.converter()
        try {
            var authentication: Authentication = authManager.authenticate(LoginDatas)
            val token = tokenService.generateToken(authentication)
            return ResponseEntity(TokenDto(token,"Bearer"), HttpStatus.OK)
        }catch (e:Exception){
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
Veja que nos parâmetros do método authenticate acima, temos uma classe LoginDto. Ela é usada para recuperar o login e senha digitados pelo usuário.
Exemplo de LoginDto:

class LoginDto {

    var login: String = ""
    var pass: String = ""

    constructor(login: String, pass: String) {
        this.login = login
        this.pass = pass
    }

    fun converter():UsernamePasswordAuthenticationToken{
        return UsernamePasswordAuthenticationToken(login, pass)
    }
}


Agora Temos que colocar nossa lógica para pegar o token do cabeçalho, verificar se está ok e autenticar no Spring.
O primeiro passo é criar uma classe filter que vai estender a interface OndePerRquestFilter e estender o método doFilterInternal. É nesse método onde vai ter a logica que vai recuperar o token, validá-lo e autenticá-lo no spring.
Precisamos implementar também um método que vai recuperar o token. Nos argumento devemos ter um objeto do tipo HttpSeveletRequest. Esse método será chamado dentro do método doFilterInternal.
Exemplo de método/função que recupera o token:

fun recoverToken(request: HttpServletRequest):String?{
        var token = request.getHeader("Authorization")
        if(token == null || token.isEmpty() || !token.startsWith("Bearer ") ){
            return null
        }

        return token.substring(7,token.length)
    }

Precisamos de outro método que valida o token recuperado e também será chamado dentro do método doFilterInternal.
Esse método deve ser implementado na classe TokenService criada aqui, por esse motivo, a classe filter deve ter uma injeção de TokenService, porém em classes filters não conseguimos fazer injeção de dependências, então vamos precisar injetar na classe de configurações de segurança, pois passaremos essa injeção na implementação do objeto da classe filter feita na classe de configurações do (Use: @Autowired laterinit var tokenService:TokenService e não se esqueça de adicionar um campo do tipo TokenService na classe filter).
Exemplo de método de validação de token:

fun isValidToken(token:String?):Boolean{
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token)
            return true;
        }catch (e:Exception){
            return false
        }
    }


Precisamos alterar o método/função configure(http: HttpSecurity) adicionando .and().addFilterBefore(AuthenticationViaTokenFilter(tokenService, userRepository),UsernamePasswordAuthenticationFilter::class.java):

override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/usuario/cadastrar").permitAll()
            .antMatchers("/auth").permitAll()
            .anyRequest().authenticated()
            .and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilterBefore(AuthenticationViaTokenFilter(tokenService, userRepository),UsernamePasswordAuthenticationFilter::class.java)

    }

Obs.: Veja que entre os argumentos do construtor de AuthenticationViaTokenFilter, além de um objeto do tipo TokenService, também tem um objeto de userRepository, ou seja, a repository referente ao usuário foi também injetada na classe de configurações de segurança. Precisamos injetar essa repository (Use: @Autowired laterinit var repository:UserRepository não se esqueça de adicionar um campo do tipo TokenService na classe filter).


Agora, não classe filter, precisamos criar o método que autentica o usuário, mas antes disso, na classe TokenService, vamos implementar um método que recupera o usuário usando o id do usuário contido no no token.
Exemplo de método que recupera o usuário:

fun getUserId(token:String):Long{
        var claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).body
        return claims.subject as Long
    }

Agora podemos implementar o método que autentica o usuário.
Exemplo de implementação:

fun autenticateClient(token:String){
        var userId = tokenService.getUserId(token)
        val user = userRepository.findById(userId).get()
        val authentication = UsernamePasswordAuthenticationToken(user,null,user.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

Por fim, implementamos ao lógica do método xxx.
Exemplo:

