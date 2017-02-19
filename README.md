# testng.util
Provides useful integrations with [Mockito](http://mockito.org/) and [Spring](https://spring.io/)
for [TestNG](http://testng.org/) test runner.
Feel free to include it into your project using [JitPack](https://jitpack.io/).

## SpringContextListener
Allows you quickly build and run the test against application context, filled either with real objects,
or mocks and spies instead.
Consider having following class composition:
```java
class Service {
  @Autowired
  MockedService mockedService;
  @Autowired
  SpiedService spiedService;
  @Autowired
  RealService realService;
}
```
Then it can be tested, using following testNG test suite:
```java
@Listeners(SpringContextListener.class)
class ServiceTest {
  @Real
  Service service;
  @Real
  RealService realService;
  @Spied
  SpiedService spiedService;
  ...
}
```

It also supports defining required Application context using @Configuration classes.
```java
@Listeners(SpringContextListener.class)
@ContextConfiguration(configClasses = SomeSpringConfigurationClass.class)
...
```

Tests can be organized using hierarchy of classes and interfaces (e.g. your test suite can implement interface or extend class with @ContextConfiguration annotation).
