# Bean 등록 방식 정리
- @Configuration + @Bean : 명시적으로 Bean 등록
- @Service : 컴포넌트 스캔을 통한 자동 Bean 등록

## 1. @Configuration + @Bean 방식

~~~java
@Configuration
public class BeanConfig {
    @Bean
    public UserService userService() {
        return new UserService();
    }
}
~~~
~~~java
public class UserService {
    public void createUser(String name) {
        System.out.println(name + "님을 생성했습니다.");
    }
}
~~~

동작 원리  
1. @Configuration이 붙은 클래스는 설정 클래스로 인식되어 Spring이 BeanConfig를 인스턴스화 
2. @Bean 메서드를 실행하여 UserService 객체를 생성 → Spring 컨테이너에 등록됨
3. 다른 Bean에서 @Autowired로 UserService를 요청하면, Spring 컨테이너가 등록된 Bean을 주입

## 2. @Service 방식

~~~java
@Service
public class UserService2 {
    public void createUser(String name) {
        System.out.println(name + "님을 생성했습니다.");
    }
}
~~~
- @Service 어노테이션을 붙이면 Spring이 클래스를 직접 Bean으로 등록 
- 내부적으로는 @Component를 포함하고 있어, 자동 컴포넌트 스캔에 의해 Bean으로 관리됨

## 3. 두 방식 비교
일반적으로는 @Service, @Controller, @Repository 같은 컴포넌트 스캔 기반 어노테이션을 사용하는 게 간단하고 편리함.
하지만 경우에 따라 직접 Bean을 등록해야 할 때가 있음.

### @Bean이 필요한 상황
1. 외부 라이브러리 클래스 Bean 등록 
   - 내가 작성한 코드가 아닌 외부 라이브러리 객체는 어노테이션을 붙일 수 없음
   - 예시: RestTemplate, ObjectMapper, DataSource

2. 생성 로직이 복잡할 때
   - 단순히 new가 아니라 의존 객체 연결, 설정, 초기화 로직이 필요한 경우
    ~~~ java
    @Bean
    public UserService userService() {
        UserRepository repo = new UserRepository();
        return new UserService(repo);
    }
    ~~~

3. 조건부 Bean 등록이 필요할 때
   - 설정 값이나 환경(Profile)에 따라 Bean을 선택해야 하는 경우
    ~~~ java
    @Bean
    @ConditionalOnProperty(name="useSpecialService", havingValue="true")
    public SpecialService specialService() {
        return new SpecialService();
    }
    ~~~

4. 인터페이스 구현체 선택
   - 구현체가 여러 개일 때, 어떤 것을 Bean으로 사용할지 코드에서 직접 제어 가능
    ~~~ java
    @Bean
    public PaymentService paymentService() {
        if (usePaypal) {
            return new PaypalPaymentService();
        } else {
            return new KakaoPaymentService();
        }
    }
    ~~~

## 4. 정리
| 상황               | 이유                     | 예시                         |
|--------------------|--------------------------|------------------------------|
| 외부 라이브러리    | 코드 수정 불가           | RestTemplate, ObjectMapper   |
| 복잡한 초기화 로직 | 생성 시 추가 작업 필요   | UserService + Repository 연결 |
| 조건부 Bean        | 환경/설정에 따라 Bean 결정 | 프로파일별 Service 선택      |
| 인터페이스 구현 선택 | 구현체를 직접 선택       | PaymentService 구현체 선택    |


👉 @Service: 단순 서비스 로직, 스프링이 자동으로 Bean 등록  
👉 @Bean: 자동 스캔만으로는 부족할 때, Bean 생성을 직접 제어해야 할 때 사용

일반 서비스 클래스 → @Service  
외부 라이브러리, 조건부 Bean, 복잡한 생성 로직 → @Bean