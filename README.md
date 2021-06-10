

![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/background-3228704_1920.jpg)

大家好，我是mbb

作为一名基于Spring摸爬滚打了数年的码农；各种无脑的苦力活，可以说至少占据了一半的变成人生；比如说，对象拷贝，无脑的get、set调用；但是基于MVC下，各种实体间的转换，又是必不可少的。

你平常都用什么方式来做对象拷贝呢？



### BeanUtils

因为是 Spring 自带的拷贝功能，所以出境率比较的高；但是在实际使用 BeanUtils 过程中，你是否遇到以下的一些小问题：

- 属性类型不一样，无法进行拷贝，如数据库中查出来的Date，想转换成时间戳返回给前端；不好意思！不行！另外处理；
- 只想拷贝部分字段，但是没办法忽略；对不起，不管三七二十一，一顿拷贝；完了再特殊处理；
- 无法对属性进行规则转换；比如数据库中查询出来的0和1想在转换成VO之后变成true和false；sorry！不支持，自行搞定；
- 性能低

虽然基础的拷贝功能可以做到，但是总觉得跟个糙汉子一样；很多细节都没有做处理，只能单独再做二次加工；



### MapStruct

既然 BeanUtils 各种别扭，那有没有更好的方式可以解决这些问题呢？

当然是有的；

那就是今天要详细介绍的对象拷贝的王者：MapStruct

上面说的这些问题，通通都能解决了；

上面把 BeanUtils 比作糙汉子，那 MapStruct 就可以称之为大家闺秀，心细如发，开发过程中能遇到的问题，他都给出了解决方案，完美帮你解决。



## MapStruct

### 什么是 MapStruct？

MapStruct 是一个代码生成器，它基于约定优于配置方法，极大地简化了 Java bean 类型之间映射的实现。

生成的映射代码使用简单的方法调用，因此速度快、类型安全且易于理解。  ---- 来源于官网

### 性能

以下是Java各种拷贝方式的耗时对比：

![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210609215436091.png)

### MapStruct的优点

#### 相比于手动get、set

无需手写转换工具类，减轻大量的体力活

#### 相比与其他动态映射

- **效率高**

  核心的转换逻辑并不是通过反射实现，而是通过编译时自动生成基于 getter/setter 转换实现类；

- **性能高**

  基于简单的get、set操作，效率达到最佳

- **编译时类型安全**

  只能映射相同名称或带映射标记的属性；

- **编译时产生错误报告**

  如果映射不完整或映射不正确则会在编译时抛出异常，代码将无法正常运行；

- **能明确查看转换的细节**

  编译生成的class对象可以看到详细的转换过程，方便快速定位转换过程中的问题。


#### MapStruct 常用的重要注解 :

- @Mapper 

  标记这个接口作为一个映射接口，并且是编译时 MapStruct 处理器的入口

- @Mapping

  解决源对象和目标对象中，属性名字不同的情况

- @Mappings

  当存在多个 @Mapping 需要配置；可以通过 @Mappings 批量指定

- Mappers.getMapper 

  Mapper 的 class 获取自动生成的实现对象，从而让客户端可以访问 Mapper 接口的实现

##  使用

### 测试代码



### 准备

- 依赖

  最新的版本可以通过下面的链接查看

  > https://mvnrepository.com/artifact/org.MapStruct/MapStruct-jdk8
  >
  > https://mvnrepository.com/artifact/org.MapStruct/MapStruct-processor

  ```xml
  <properties>
       <MapStruct.version>1.4.2.Final</MapStruct.version>
  </properties>
  ```

  ```xml
  <!-- https://mvnrepository.com/artifact/org.MapStruct/MapStruct-jdk8 -->
  <dependency>
       <groupId>org.MapStruct</groupId>
       <artifactId>MapStruct-jdk8</artifactId>
       <version>${MapStruct.version}</version>
  </dependency>
  
  <!-- https://mvnrepository.com/artifact/org.MapStruct/MapStruct-processor -->
  <dependency>
       <groupId>org.MapStruct</groupId>
       <artifactId>MapStructrocessor</artifactId>
       <version>${MapStruct.version}</version>
  </dependency>
  
  <!-- 非必须 注意：版本过高可能造成对象无法生成-->
  <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.16.22</version>
  </dependency>
  ```

- 基础测试对象

  ```java
  @Data
  @Builder
  @ToString
  public class UserDTO {
      private String name;
  
      private Integer age;
  
      private Date createTime;
  }
  ```

  ```java
  @Data
  @ToString
  public class UserVO {
      private String name;
  
      private Integer age;
  
      private Date createTime;
  }
  ```

  ```java
  @Data
  @ToString
  public class UserVO1 {
      private String name;
  
      private Integer age;
      
  	// 类型和VO对象不同
      private String createTime;
  }
  ```

  

### BeanUtils拷贝演示

简单的演示一下BeanUtils拷贝

```java
public class t1 {
    public static void main(String[] args) {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .build();

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDTO,userVO);
        System.out.println(userVO);

        UserVO1 userVO1 = new UserVO1();
        BeanUtils.copyProperties(userDTO,userVO1);
        System.out.println(userVO1);
    }
}
```

![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210609222306034.png)

可以看到，文章一开始说的问题，就慢慢在暴露了



### MapStruct基本功能演示

- 第一步，定义Mapper

  ```java
  // spring方式加载
  @Mapper(componentModel = "spring")
  public interface UserMapper {
      // default方式加载
      UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
  
      /**
       * 将DTO转VO
       *
       * @param userDTO
       * @return
       */
      UserVO userVO2UserDTO(UserDTO userDTO);
  }
  ```

  componentModel 属性用于指定自动生成的接口实现类的组件类型，这个属性支持四个值：

  - **default**: 这是默认的情况；通过ClassLoader加载
  - **spring**: 生成的实现类上面会自动添加一个@Component注解，可以通过Spring的 @Autowired方式进行注入
  - jsr330: 生成的实现类上会添加@javax.inject.Named 和@Singleton注解，可以通过 @Inject注解获取
  - cdi: 生成的映射器是一个应用程序范围的CDI bean，可以通过检索 @Inject

- 第二步，测试default和spring方式

  - default

    ```java
    UserDTO userDTO = UserDTO.builder()
            .name("张三")
            .age(10)
            .createTime(new Date())
            .build();
    UserVO userVO = UserMapper.INSTANCE.userVO2UserDTO(userDTO);
    System.out.println(userVO);
    ```

    ![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210609224843295.png)

    自动生成的UserMapperImpl.class

    ```java
    public class UserMapperImpl implements UserMapper {
        public UserMapperImpl() {
        }
    
        public UserVO userVO2UserDTO(UserDTO userDTO) {
            if (userDTO == null) {
                return null;
            } else {
                UserVO userVO = new UserVO();
                userVO.setName(userDTO.getName());
                userVO.setAge(userDTO.getAge());
                userVO.setCreateTime(userDTO.getCreateTime());
                return userVO;
            }
        }
    }
    ```

    ![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210610222255087.png)

  - spring方式

    ```
    @Autowired
    UserMapper userMapper;
        
    @Test
    void springTest() {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .build();
        UserVO userVO = userMapper.userVO2UserDTO(userDTO);
        System.out.println(userVO);
    }
    ```

    ![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210609225209962.png)

    自动生成的UserMapperImpl.class

    ```java
    @Component
    public class UserMapperImpl implements UserMapper {
        public UserMapperImpl() {
        }
    
        public UserVO userVO2UserDTO(UserDTO userDTO) {
            if (userDTO == null) {
                return null;
            } else {
                UserVO userVO = new UserVO();
                userVO.setName(userDTO.getName());
                userVO.setAge(userDTO.getAge());
                userVO.setCreateTime(userDTO.getCreateTime());
                return userVO;
            }
        }
    }
    ```

    可以看出实现类上面自动加上了 `@Component`，就可以通过 Spring 的方式注入对象并使用；



### 进一步封装

上面简单测试可以发现，需要做两个对象的转换，就得定义一个接口和数个互转的方法；

为了不用每次都去写那些重复的转换方法，这里对转换接口再向上做一次抽象；

- 定义基础的转换接口

  包含了最基本的4种转换方式

  ```java
  /**
   * 基础的对象转换Mapper
   *
   * @param <SOURCE> 源对象
   * @param <TARGET> 目标对象
   */
  public interface BaseMapper<SOURCE, TARGET> {
      TARGET to(SOURCE var1);
  
      List<TARGET> to(List<SOURCE> var1);
  
  
      SOURCE from(TARGET var1);
  
      List<SOURCE> from(List<TARGET> var1);
  }
  ```

- 修改UserMapper

  ```java
  @Mapper(componentModel = "spring")
  public interface UserMapper extends BaseMapper<UserDTO, UserVO> {
      UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
  }
  ```

- 测试

  使用方式没有产生任何变化

  ```java
  // default
  UserVO userVO = UserMapper.INSTANCE.to(userDTO);
  
  // spring
  UserVO userVO = userMapper.to(userDTO);
  ```

  

## 特殊场景

基础的场景已经会用了，但是往往实际的开发中，会面临各种各样奇奇怪怪的转换，这里就详细的列举一下各种特殊的情况。

### 日期转换

比如数据库中的日期对象 Date 需要转换成 yyyyMMdd 这样格式的对象

- 测试对象

  ```java
  @Data
  @ToString
  public class UserVO1 {
      private String name;
  
      private Integer age;
      
  	// 类型和VO对象不同
      private String createTime;
  }
  ```

- Mapper定义

  ```java
  @Mapper
  public interface User1Mapper extends BaseMapper<UserDTO, UserVO1>{
      User1Mapper INSTANCE = Mappers.getMapper(User1Mapper.class);
  
      @Mappings({
              @Mapping(source = "createTime",target = "createTime",dateFormat = "yyyyMMdd")
      })
      @Override
      UserVO1 to(UserDTO var1);
  }
  ```

- 测试

  ```java
  public class t2 {
      public static void main(String[] args) {
          UserDTO userDTO = UserDTO.builder()
                  .name("张三")
                  .age(10)
                  .createTime(new Date())
                  .build();
  
          UserVO1 userVO1 = User1Mapper.INSTANCE.to(userDTO);
          System.out.println(userVO1);
  
          List<UserDTO> userDTOS = new ArrayList<>();
          userDTOS.add(userDTO);
          List<UserVO1> userVO1s = User1Mapper.INSTANCE.to(userDTOS);
          System.out.println(userVO1s);
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210609232938540.png)



### 忽略指定字段

部分字段不进行拷贝操作；忽略主要是在Mapper的地方进行配置；

- 测试对象

  采用 UserDTO 和 UserVO1 进行测试

- Mapper

  ```java
  @Mapper
  public interface User4Mapper extends BaseMapper<UserDTO, UserVO1>{
      User4Mapper INSTANCE = Mappers.getMapper(User4Mapper.class);
  
      @Mappings({
              // 要忽略的字段
              @Mapping(target = "createTime",ignore = true)
      })
      @Override
      UserVO1 to(UserDTO var1);
  }
  ```

- 测试

  ```java
  public class t4 {
      public static void main(String[] args) {
          UserDTO userDTO = UserDTO.builder()
                  .name("张三")
                  .age(10)
                  .createTime(new Date())
                  .build();
  
          UserVO1 userVO1 = User4Mapper.INSTANCE.to(userDTO);
          System.out.println(userVO1);
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210610001850237.png)

 

### 多数据源拷贝

多个数据源对象的数据拷贝到一个对象中

- 测试对象

  ```java
  // UserDTO 略...
  
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public class AddressDTO {
      private String country;
  
      private String province;
  
      private String city;
  }
  ```

- Mapper

  ```java
  @Mapper(componentModel = "spring")
  public interface User2Mapper {
      User2Mapper INSTANCE = Mappers.getMapper(User2Mapper.class);
  
      // 如果无特殊字段，可以不配置Mappings
      // 会自动把两个源对象中的属性复制到咪表对象
      @Mappings({
              @Mapping(source = "userDTO.name",target = "name"),
              @Mapping(source = "addressDTO.country",target = "country")
      })
      UserVO2 to(UserDTO userDTO, AddressDTO addressDTO);
  }
  ```

- 测试

  ```java
  public class t3 {
      public static void main(String[] args) {
          UserDTO userDTO = UserDTO.builder()
                  .name("张三")
                  .age(10)
                  .createTime(new Date())
                  .build();
  
          AddressDTO addressDTO = AddressDTO.builder()
                  .country("中国")
                  .province("北京")
                  .city("北京")
                  .build();
          UserVO2 userVO2 = User2Mapper.INSTANCE.to(userDTO, addressDTO);
          System.out.println(userVO2);
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210609234930419.png)



### 不同属性名之间的转换

两个对象间不同名称间属性值拷贝

- 测试对象

  ```java
  // UserDTO 略...
  
  @Data
  @ToString
  public class UserVO3 {
      private String nickName;
  }
  ```

- Mapper

  ```java
  @Mapper(componentModel = "spring")
  public interface User3Mapper extends BaseMapper<UserDTO,UserVO3>{
      User3Mapper INSTANCE = Mappers.getMapper(User3Mapper.class);
  
      @Mapping(source = "name", target = "nickName")
      @Override
      UserVO3 to(UserDTO var1);
  }
  ```

- 测试

  ```java
  public class t3 {
      public static void main(String[] args) {
          UserDTO userDTO = UserDTO.builder()
                  .name("张三")
                  .age(10)
                  .createTime(new Date())
                  .build();
  
          UserVO3 userVO3 = User3Mapper.INSTANCE.to(userDTO);
          System.out.println(userVO3);
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210610000755470.png)

  

### 互相转换（反向转换）

如上示例，将的 UserDTO.name 转换为 UserVO3.nickName ；同时 UserVO3.nickName 也要能正常转换为 UserDTO.name，就可以通过`@InheritInverseConfiguration` 来实现

- 转换Mapper

  ```java
  @Mapper(componentModel = "spring")
  public interface User3Mapper extends BaseMapper<UserDTO, UserVO3> {
      User3Mapper INSTANCE = Mappers.getMapper(User3Mapper.class);
  
      @Mapping(source = "name", target = "nickName")
      @Override
      UserVO3 to(UserDTO var1);
  
      // name为 A==>B 的方法名
      @InheritInverseConfiguration(name = "to")
      @Override
      UserDTO from(UserVO3 var1);
  }
  ```

- 测试

  ```java
  /**
   * 不同属性名之间的映射
   */
  public class t3 {
      public static void main(String[] args) {
          UserDTO userDTO = UserDTO.builder()
                  .name("张三")
                  .age(10)
                  .createTime(new Date())
                  .build();
  
          UserVO3 userVO3 = User3Mapper.INSTANCE.to(userDTO);
          System.out.println(userVO3);
  
          UserDTO userDTO1 = User3Mapper.INSTANCE.from(userVO3);
          System.out.println(userDTO1);
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210610221336136.png)



### 自定义格式转换

批量将一种类型的数据转换为另一种格式的数据；这里测试将所有的Date数据全部转换为 yyyy-MM-dd 的文本

- 测试对象

  ```java
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public class UserDTO {
      private String name;
  
      private Integer age;
  
      private Date createTime;
  
      private Date updateTime;
  }
  ```

  ```java
  @Data
  @ToString
  public class UserVO1 {
      private String name;
  
      private Integer age;
  
      // 类型和VO对象不同
      private String createTime;
  
      // 类型和VO对象不同
      private String updateTime;
  }
  ```

  

- 自定义日期格式转换Mapper

  ```java
  public class DateMapper {
      public String toString(Date date) {
          return date != null ? new SimpleDateFormat("yyyy-MM-dd").format(date) : null;
      }
  
      public Date toDate(String date) {
          try {
              return date != null ? new SimpleDateFormat("yyyy-MM-dd").parse(date) : null;
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }
  }
  ```

- 定义对象转换Mapper

  ```java
  /**
   * 自定义日期格式转换映射器
   * uses = DateMapper.class
   */
  @Mapper(uses = DateMapper.class)
  public interface User5Mapper extends BaseMapper<UserDTO, UserVO1> {
      User5Mapper INSTANCE = Mappers.getMapper(User5Mapper.class);
  }
  ```

- 测试

  ```java
  public class t5 {
      public static void main(String[] args) {
          UserDTO userDTO = UserDTO.builder()
                  .name("张三")
                  .age(10)
                  .createTime(new Date())
                  .updateTime(new Date())
                  .build();
  
          UserVO1 userVO1 = User5Mapper.INSTANCE.to(userDTO);
          System.out.println(userVO1);
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210610130802106.png)

### 多种不同自定义转换作用于不同属性

- 测试场景

  对象中的 Date 字段转换成不同格式的时间文本，比如转换成 yyyy-MM-dd 和 yyyy/MM/dd 两种格式

- 测试对象

  同上

- 自定义时间转换器

  格式一

  ```
  @Named("dateMapper1")
  public class DateMapper1 {
      public String toString(Date date) {
          return date != null ? new SimpleDateFormat("yyyy-MM-dd").format(date) : null;
      }
  
      public Date toDate(String date) {
          try {
              return date != null ? new SimpleDateFormat("yyyy-MM-dd").parse(date) : null;
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }
  }
  ```

  测试二

  ```
  @Named("dateMapper2")
  public class DateMapper2 {
      public String toString(Date date) {
          return date != null ? new SimpleDateFormat("yyyy/MM/dd").format(date) : null;
      }
  
      public Date toDate(String date) {
          try {
              return date != null ? new SimpleDateFormat("yyyy/MM/dd").parse(date) : null;
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }
  }
  ```

- 定义对象转换Mapper

  ```java
  /**
   * 自定义不同日期格式转换映射器
   * uses = DateMapper.class
   */
  @Mapper(uses = {
          DateMapper1.class,
          DateMapper2.class
  })
  public interface User6Mapper extends BaseMapper<UserDTO, UserVO1> {
      User6Mapper INSTANCE = Mappers.getMapper(User6Mapper.class);
  
      @Mappings({
              @Mapping(source = "createTime", target = "createTime", qualifiedByName = {"dateMapper1"}),
              @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = {"dateMapper2"})
      })
      @Override
      UserVO1 to(UserDTO var1);
  }
  ```

- 测试

  ```java
  public class t6 {
      public static void main(String[] args) {
          UserDTO userDTO = UserDTO.builder()
                  .name("张三")
                  .age(10)
                  .createTime(new Date())
                  .updateTime(new Date())
                  .build();
  
          UserVO1 userVO1 = User5Mapper.INSTANCE.to(userDTO);
          System.out.println(userVO1);
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210610131542046.png)



### 数字类型转换

- 场景

  如果是基本的数据类型与文本之间的转换，默认情况下MapStruct 已经帮我们做好了，比如int与 string 的互转，就会自动通过String.valueof 以及 Integer.tostring 等方法进行转换了；

  但是还存在一些特殊场景；比如高精度转换低精度，需要取小数点后多少位等，就需要特殊处理；

  这里就来测试一个 double 转 string 保留两位小数的场景

- 测试对象

  UserDTO添加以下字段

  ```java
  private Double wallet;
  ```

  UserVO1添加以下字段

  ```java
  private String wallet;
  ```

- 定义对象转换Mapper

  ```java
  /**
   * 数值类型格式化
   */
  @Mapper
  public interface User7Mapper extends BaseMapper<UserDTO, UserVO1> {
      User7Mapper INSTANCE = Mappers.getMapper(User7Mapper.class);
  
      @Mapping(source = "wallet", target = "wallet", numberFormat = "$#.00")
      @Override
      UserVO1 to(UserDTO var1);
  
      @Mapping(source = "wallet", target = "wallet", numberFormat = "$#.00")
      @Override
      UserDTO from(UserVO1 var1);
  
      @IterableMapping(numberFormat = "$#.00")
      List<String> doubleList2String(List<Double> vas);
  
      @IterableMapping(numberFormat = "$#.00")
      List<Double> stringList2Double(List<String> vas);
  }
  ```

- 测试

  ```java
  /**
   * 数值类型转换格式化
   */
  public class t7 {
      public static void main(String[] args) {
          UserDTO userDTO = UserDTO.builder()
                  .name("张三")
                  .age(10)
                  .createTime(new Date())
                  .updateTime(new Date())
                  .wallet(10000.45678)
                  .build();
  
          UserVO1 userVO1 = User7Mapper.INSTANCE.to(userDTO);
          System.out.println(userVO1);
  
          UserDTO userDTO1 = User7Mapper.INSTANCE.from(userVO1);
          System.out.println(userDTO1);
  
          List<Double> vas = new ArrayList<>();
          vas.add(123.5585);
          vas.add(784.1565488);
          vas.add(12.11243);
          // string list转 double
          List<String> strings = User7Mapper.INSTANCE.doubleList2String(vas);
          System.out.println(strings);
  
          // double list 转 string
          List<Double> doubles = User7Mapper.INSTANCE.stringList2Double(strings);
          System.out.println(doubles);
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210610213916626.png)



### BigDecimal转换

- 测试对象

  UserDTO添加以下属性

  ```java
  private BigDecimal deposit;
  ```

  UserVO1添加以下属性

  ```java
  private String deposit;
  ```

- 定义对象转换Mapper

  ```java
  /**
   * BigDecimal转换
   */
  @Mapper
  public interface User8Mapper extends BaseMapper<UserDTO, UserVO1> {
      User8Mapper INSTANCE = Mappers.getMapper(User8Mapper.class);
  
      @Mapping(source = "deposit", target = "deposit", numberFormat = "#.##E0")
      @Override
      UserVO1 to(UserDTO var1);
  
      @Mapping(source = "deposit", target = "deposit", numberFormat = "#.##E0")
      @Override
      UserDTO from(UserVO1 var1);
  }
  ```

- 测试

  ```java
  /**
   * BigDecimal转换测试
   */
  public class t8 {
      public static void main(String[] args) {
          UserDTO userDTO = UserDTO.builder()
                  .name("张三")
                  .age(10)
                  .createTime(new Date())
                  .updateTime(new Date())
                  .wallet(10000.45678)
                  .deposit(new BigDecimal(10000000.324))
                  .build();
  
          UserVO1 userVO1 = User8Mapper.INSTANCE.to(userDTO);
          System.out.println(userVO1);
  
          UserDTO userDTO1 = User8Mapper.INSTANCE.from(userVO1);
          System.out.println(userDTO1);
      }
  }
  ```



### 嵌套属性的转换

当对象中嵌套对象，且需要转换的时候，可以通过配置不同对象间的映射关系来完成嵌套映射

- 测试对象

  UserDTO添加地址对象

  ```java
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public class UserDTO {
      // 略...
      
      private AddressDTO addressDTO;
  }
  ```

  UserVO2

  ```java
  @Data
  @ToString
  public class UserVO2 {
      private String name;
  
      private Integer age;
  
      private String country;
  
      private String province;
  
      private String city;
  }
  ```

- 测试需求

  将UserDTO.addressDTO.country 属性映射到 UserVO2.country

- 映射Mapper

  ```java
  /**
   * 嵌套对象的映射
   */
  @Mapper
  public interface User9Mapper extends BaseMapper<UserDTO, UserVO2> {
      User9Mapper INSTANCE = Mappers.getMapper(User9Mapper.class);
  
      @Mapping(source = "addressDTO.country", target = "country")
      @Override
      UserVO2 to(UserDTO userDTO);
  
      // 反向配置
      @InheritInverseConfiguration(name = "to")
      @Override
      UserDTO from(UserVO2 var1);
  }
  ```

- 测试代码

  ```java
  /**
   * 嵌套对象的映射
   */
  public class t9 {
      public static void main(String[] args) {
          UserDTO userDTO = UserDTO.builder()
                  .name("张三")
                  .age(10)
                  .addressDTO(AddressDTO.builder().country("中国").build())
                  .build();
  
          UserVO2 userVO2 = User9Mapper.INSTANCE.to(userDTO);
          System.out.println(userVO2);
  
          UserDTO userDTO1 = User9Mapper.INSTANCE.from(userVO2);
          System.out.println(userDTO1);
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210610222013803.png)



## BeanUtils与MapStruct性能对比

文章一开始就说到了 MapStruct 的性能要高于 BeanUtils ；经过了一轮使用之后，我们得来实测一下性能到底差多少？

- 测试场景

  分别通过MapStruct 和 BeanUtils 将相同对象转换100W次，看看整体的耗时

- 测试代码

  ```java
  /**
   * BeanUtils与MapStruct性能对比
   */
  public class t10 {
      public static void main(String[] args) {
          for (int j = 0; j < 10; j++) {
              Long start = System.currentTimeMillis();
              for (int i = 0; i < 1000000; i++) {
                  UserDTO userDTO = UserDTO.builder()
                          .name("张三")
                          .age(10)
                          .build();
  
                  UserVO userVO = new UserVO();
                  BeanUtils.copyProperties(userDTO, userVO);
              }
              System.out.println("BeanUtils 100W次转换耗时:" + (System.currentTimeMillis() - start));
  
              start = System.currentTimeMillis();
              for (int i = 0; i < 1000000; i++) {
                  UserDTO userDTO = UserDTO.builder()
                          .name("张三")
                          .age(10)
                          .build();
  
                  UserVO1 userVO1 = User1Mapper.INSTANCE.to(userDTO);
              }
              System.out.println("MapStruct 100W次转换耗时:" + (System.currentTimeMillis() - start));
              System.out.println();
          }
      }
  }
  ```

- 测试结果

  可以看出，相同的属性转换，发现性能确实不在一个数量级；

  ![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210610224936864.png)

  



## 问题

### 问题一

找不到属性名

```
Error:(15, 5) java: No property named "xxx" exists in source parameter(s). Did you mean "null"?
```

![](https://cdn.jsdelivr.net/gh/mbb2100/imgs/image-20210610000354909.png)

lombok版本过高，将版本调低点

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.16.22</version>
</dependency>
```

### 问题二

修改无效

可以将target目录删除重新编译测试；防止因为修改为编译导致不生效的情况。