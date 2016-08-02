# testng.util
Provides useful integrations with [Mockito](http://mockito.org/) and [Spring](https://spring.io/)
for [TestNG](http://testng.org/) test runner.
Feel free to include it into your project using [JitPack](https://jitpack.io/).

## ApplicationContextListener
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
Then it can be easily tested, using following test:
```java
@Listeners(ApplicationContextListener.class)
@ContextConfiguration(
  realObjects = { Service.class, RealService.class },
  spies = SpiedService.class)
class ServiceTest {
  @Autowired
  Service service;
  ...
}
```
Or using even more simple initialization:
```java
@Listeners(ApplicationContextListener.class)
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
