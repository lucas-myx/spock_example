## Spock 是什么？

斯波克是国外一款优秀的测试框架，基于BDD思想，功能强大，能够让我们的测试代码规范化，结构层次清晰，结合 groovy 动态语言的特点以及自身提供的各种标签让编写测试代码更加高效和简洁，提供一种通用、简单、结构化的描述语言

引用官网的介绍如下([http://spockframework.org](http://spockframework.org))

> Spock是一个Java和Groovy应用程序的测试和规范框架。 它之所以能在人群中脱颖而出，是因为它优美而富有表现力的规范语言。
> 斯波克的灵感来自JUnit、RSpec、jMock、Mockito、Groovy、Scala、Vulcans

简单说 Spock 的特点如下:

 - 让我们的测试代码更规范，内置多种标签来规范单测代码的语义，从而让我们的测试代码结构清晰，更具可读性，降低后期维护难度 
 -  提供多种标签，比如: where、with、thrown...... 帮助我们应对复杂的测试场景
 - 使用 groovy 这种动态语言来编写测试代码，可以让我们编写的测试代码更简洁，适合敏捷开发，提高编写单测代码的效率
 - 遵从BDD行为驱动开发模式，不单是为了测试覆盖率而测试，有助于提升代码质量 
 - IDE兼容性好，自带 mock 功能

## 为什么使用 Spock? Spock 和 JUnit、JMock、Mockito 的区别在哪里？
现有的单测框架比如 junit、jmock、mockito 都是相对独立的工具，只是针对不同的业务场景提供特定的解决方案

Junit 单纯用于测试，不提供 mock 功能

微服务已经是互联网公司的主流技术架构，大部分的系统都是分布式，很多业务功能需要依赖底层接口返回的数据才能继续剩下的流程，或者从数据库/Redis等存储设备上获取，或是从配置中心的某个配置获取

这样就导致如果我们想要测试代码逻辑是否正确，就必须把这些依赖项(接口、Redis、DB、配置中心...)给 mock 掉

如果接口不稳定或有问题则会影响我们代码的正常测试，所以我们要把调用接口的地方给**模拟**掉，让它返回指定的结果(提前准备好的数据，而不是真实的调用接口)，这样才能往下验证我们自己的代码是否正确，符合预期逻辑和结果

JMock 或 Mockito 虽然提供了 mock 功能，可以把接口等依赖屏蔽掉，但不提供对静态类静态方法的 mock，PowerMock 或 Jmockit 虽然提供静态类和方法的 mock，但它们之间需要整合(junit+mockito+powermock)，语法繁琐，而且这些工具并没有告诉你“**单元测试代码到底应该怎么写？**”

工具多了也会导致不同的人写出的单元测试代码五花八门，风格迥异。。。

Spock 通过提供规范描述，定义多种标签(given、when、then、where等)去描述代码“应该做什么”，输入条件是什么，输出是否符合预期，从语义层面规范代码的编写。

Spock 自带 Mock 功能，使用简单方便(也支持扩展其他mock框架，比如 power mock)，再加上groovy动态语言的强大语法，能写出简洁高效的测试代码，同时更方便直观的验证业务代码行为流转，增强我们对代码执行逻辑的可控性。

## 背景和初衷
网上关于 Spock 的资料比较简单，包括官网的 demo，无法解决我们项目中的复杂业务场景，需要找到一套适合自己项目的成熟解决方案，所以觉得有必要把我们项目中使用 Spock 的经验分享出来, 帮助大家提升单测开发的效率和验证代码质量。

在熟练掌握 Spock 后我们项目组整体的单测**开发效率提升了50%以上**，代码可读性和维护性都得到了改善和提升，并且对代码质量的提升有明显的作用

## 适合人群
写 Java 单元测试的开发小伙伴和测试同学，所有的演示代码运行在 IntelliJ IDEA中，spring-boot 项目，基于 Spock 1.3-groovy-2.5 版本

## Spock如何解决传统单元测试开发中的痛点
### 1. 单元测试代码开发的成本和效率
复杂场景的业务代码，在分支(if/else)很多的情况下，编写单测代码的成本会相应增加，势必增加写单元测试的成本

举个我们生产环境发生的一起事故：有个功能上线1年多一直都正常，没有出过问题，但最近有个新的调用方请求的数据不一样，走到了代码中一个不常用的分支逻辑，导致了bug，直接抛出异常阻断了主流程，好在调用方请求量不大。。。

估计当初写这段代码的同学也认为很小几率会走到这个分支，虽然当时也写了单元测试代码，但分支较多，**刚好漏掉了这个分支逻辑的测试，给日后上线留下了隐患**

这也是我们平时写单元测试最常遇到的问题：要达到分支覆盖率高要求的情况下，if/else有不同的结果，传统的单测写法可能要多次调用，才能覆盖全部的分支场景，一个是写单测麻烦，同时也会增加单测代码的冗余度

虽然可以使用junit的@parametered参数化注解或者dataprovider的方式，但还是不够方便直观，而且如果其中一次分支测试case出错的情况下，报错信息也不够详尽。

比如下面的示例演示代码，根据输入的身份证号码识别出生日期、性别、年龄等信息，这个方法的特点就是有很多if...else...的分支嵌套逻辑

```java
/**
 * 身份证号码工具类<p>
 * 15位：6位地址码+6位出生年月日（900101代表1990年1月1日出生）+3位顺序码
 * 18位：6位地址码+8位出生年月日（19900101代表1990年1月1日出生）+3位顺序码+1位校验码
 * 顺序码奇数分给男性，偶数分给女性。
 * @author 公众号:Java老K
 * 个人博客:www.javakk.com
 */
public class IDNumberUtils {
    /**
     * 通过身份证号码获取出生日期、性别、年龄
     * @param certificateNo
     * @return 返回的出生日期格式：1990-01-01 性别格式：F-女，M-男
     */
    public static Map<String, String> getBirAgeSex(String certificateNo) {
        String birthday = "";
        String age = "";
        String sex = "";

        int year = Calendar.getInstance().get(Calendar.YEAR);
        char[] number = certificateNo.toCharArray();
        boolean flag = true;
        if (number.length == 15) {
            for (int x = 0; x < number.length; x++) {
                if (!flag) return new HashMap<>();
                flag = Character.isDigit(number[x]);
            }
        } else if (number.length == 18) {
            for (int x = 0; x < number.length - 1; x++) {
                if (!flag) return new HashMap<>();
                flag = Character.isDigit(number[x]);
            }
        }
        if (flag && certificateNo.length() == 15) {
            birthday = "19" + certificateNo.substring(6, 8) + "-"
                    + certificateNo.substring(8, 10) + "-"
                    + certificateNo.substring(10, 12);
            sex = Integer.parseInt(certificateNo.substring(certificateNo.length() - 3,
                    certificateNo.length())) % 2 == 0 ? "女" : "男";
            age = (year - Integer.parseInt("19" + certificateNo.substring(6, 8))) + "";
        } else if (flag && certificateNo.length() == 18) {
            birthday = certificateNo.substring(6, 10) + "-"
                    + certificateNo.substring(10, 12) + "-"
                    + certificateNo.substring(12, 14);
            sex = Integer.parseInt(certificateNo.substring(certificateNo.length() - 4,
                    certificateNo.length() - 1)) % 2 == 0 ? "女" : "男";
            age = (year - Integer.parseInt(certificateNo.substring(6, 10))) + "";
        }
        Map<String, String> map = new HashMap<>();
        map.put("birthday", birthday);
        map.put("age", age);
        map.put("sex", sex);
        return map;
    }
}
```
针对上面这种场景，spock提供了where标签，让我们可以通过表格的方式方便测试多种分支

### 2. 单元测试代码的可读性和后期维护
微服务架构下，很多场景需要依赖其他接口返回的结果才能验证自己代码的逻辑，这样就需要使用mock工具，但JMock或Mockito的语法比较繁琐，再加上单测代码不像业务代码那么直观，不能完全按照业务流程的思路写单测，以及开发同学对单测代码可读性的不重视，最终导致测试代码难于阅读，维护起来更是难上加难

可能自己写完的测试，过几天再看就云里雾里了(当然添加注释会好很多)，再比如改了原来的代码逻辑导致单测执行失败，或者新增了分支逻辑，单测没有覆盖到，随着后续版本的迭代，会导致单测代码越来越臃肿和难以维护

Spock提供多种语义标签，如: given、when、then、expect、where、with、and 等，**从行为上规范单测代码**，每一种标签对应一种语义，让我们的单测代码结构具有层次感，功能模块划分清晰，便于后期维护

Spock自带mock功能，使用上简单方便（Spock也支持扩展第三方mock框架，比如power mock）保证代码更加规范，结构模块化，边界范围清晰，可读性强，便于扩展和维护，用自然语言描述测试步骤，让非技术人员也能看懂测试代码

比如下面的业务代码: 

调用用户接口或者从数据库获取用户信息，然后做一些转换和判断逻辑(这里的业务代码只是列举常见的业务场景，方便演示)

```java
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public UserVO getUserById(int uid){
        List<UserDTO> users = userDao.getUserInfo();
        UserDTO userDTO = users.stream().filter(u -> u.getId() == uid).findFirst().orElse(null);
        UserVO userVO = new UserVO();
        if(null == userDTO){
            return userVO;
        }
        userVO.setId(userDTO.getId());
        userVO.setName(userDTO.getName());
        userVO.setSex(userDTO.getSex());
        userVO.setAge(userDTO.getAge());
        // 显示邮编
        if("上海".equals(userDTO.getProvince())){
            userVO.setAbbreviation("沪");
            userVO.setPostCode(200000);
        }
        if("北京".equals(userDTO.getProvince())){
            userVO.setAbbreviation("京");
            userVO.setPostCode(100000);
        }
        // 手机号处理
        if(null != userDTO.getTelephone() && !"".equals(userDTO.getTelephone())){
            userVO.setTelephone(userDTO.getTelephone().substring(0,3)+"****"+userDTO.getTelephone().substring(7));
        }

        return userVO;
    }
}
```
Spock自带的mock语法也非常简单："userDao.getUserInfo() >> [user1, user2]"

两个右箭头">>"表示即模拟getUserInfo接口的返回结果，再加上使用的groovy语言，可以直接使用"[]"中括号表示返回的是List类型(具体语法会在下一篇讲到)

### 3. 单元测试不仅仅是为了达到覆盖率统计，更重要的是验证业务代码的健壮性、逻辑的严谨性以及设计的合理性
在项目初期为了赶进度，可能没时间写单测，或者这个时期写的单测只是为了达到覆盖率要求（因为有些公司在发布前会使用jacoco等单测覆盖率工具来设置一个标准，比如新增代码必须达到80%的覆盖率才能发布）

再加上传统的单测是使用java这种强类型语言写的，以及各种底层接口的mock导致写起单测来繁琐费时

这时写的单测代码比较粗糙，颗粒度比较大，缺少对单测结果值的有效验证，这样的单元测试对代码质量的验证和提升无法完全发挥作用，更多的是为了测试而测试

最后大家不得不接受“**虽然写了单测，但却没什么鸟用**”的结果

提升单测代码的可控性，方便验证业务代码的逻辑正确和是否合理, 这正是BDD(行为驱动开发)思想的一种体现

因为代码的可测试性是衡量代码质量的重要标准, 如果代码不容易测试, 那就要考虑重构了, 这也是单元测试的一种正向作用

## Spock代码讲解 - mock第三方依赖
从这章开始会列举一些典型业务场景下如何使用Spock开发测试代码，具体功能和用法，以及groovy语法特点等(为方便演示，所有业务代码均为示例代码)

### 1. Spock自带的mock用法
在上一篇讲单元测试代码可读性和维护性的问题时举了一种业务场景，即接口调用，我们的用户服务需要调用用户中心接口获取用户信息，代码如下：

```java
/**
 * 用户服务
 * @author 公众号:Java老K
 * 个人博客:www.javakk.com
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public UserVO getUserById(int uid){
        // 调用获取户信息的接口
        List<UserDTO> users = userDao.getUserInfo();
        UserDTO userDTO = users.stream().filter(u -> u.getId() == uid).findFirst().orElse(null);
        UserVO userVO = new UserVO();
        if(null == userDTO){
            return userVO;
        }
        userVO.setId(userDTO.getId());
        userVO.setName(userDTO.getName());
        userVO.setSex(userDTO.getSex());
        userVO.setAge(userDTO.getAge());
        // 显示邮编
        if("上海".equals(userDTO.getProvince())){
            userVO.setAbbreviation("沪");
            userVO.setPostCode(200000);
        }
        if("北京".equals(userDTO.getProvince())){
            userVO.setAbbreviation("京");
            userVO.setPostCode(100000);
        }
        // 手机号处理
        if(null != userDTO.getTelephone() && !"".equals(userDTO.getTelephone())){
            userVO.setTelephone(userDTO.getTelephone().substring(0,3)+"****"+userDTO.getTelephone().substring(7));
        }

        return userVO;
    }
}
```
其中userDao是使用spring注入的用户中心服务的实例对象，我们只有拿到了用户中心的返回的users，才能继续下面的逻辑(根据uid筛选用户，DTO和VO转换，邮编、手机号处理等)

所以正常的做法是把userDao的getUserInfo()方法mock掉，模拟一个我们指定的值，因为我们真正关心的是拿到users后自己代码的逻辑，这是我们需要重点验证的地方

按照上面的思路使用Spock编写的测试代码如下：

```java
package com.javakk.spock.service

import com.javakk.spock.dao.UserDao

import spock.lang.Specification
import spock.lang.Unroll

/**
 * 用户服务测试类
 * @author 公众号:Java老K
 * 个人博客:www.javakk.com
 */
class UserServiceTest extends Specification {
    def userService = new UserService()
    def userDao = Mock(UserDao)

    void setup() {
        userService.userDao = userDao
    }

    def "GetUserById"() {
        given: "设置请求参数"
        def user1 = new UserDTO(id:1, name:"张三", province: "上海")
        def user2 = new UserDTO(id:2, name:"李四", province: "江苏")

        and: "mock掉接口返回的用户信息"
        userDao.getUserInfo() >> [user1, user2]

        when: "调用获取用户信息方法"
        def response = userService.getUserById(1)

        then: "验证返回结果是否符合预期值"
        with(response) {
            name == "张三"
            abbreviation == "沪"
            postCode == 200000
        }
    }
}
```
如果要看junit如何实现可以参考上一篇的对比图，这里主要讲解spock的代码:

"def userDao = Mock(UserDao)" 这一行代码使用spock自带的Mock方法构造一个userDao的mock对象，如果要模拟userDao方法的返回，只需userDao.方法名() >> "模拟值" 的方式，两个右箭头的方式即可

setup方法是每个测试用例运行前的初始方法，类似于junit的@before

"GetUserById"方法是单测的主要方法，可以看到分为4个模块:given、and、when、then，用来区分不同单测代码的作用：

 - given: 输入条件(前置参数) 
 - when: 执行行为(mock接口、真实调用) 
 - then: 输出条件(验证结果) 
 - and: 衔接上个标签，补充的作用

每个标签后面的双引号里可以添加描述，说明这块代码的作用(非强制)，如when: "调用获取用户信息方法"

因为spock使用groovy作为单测开发语言，所以代码量上比使用java写的会少很多，比如given模块里通过构造函数的方式创建请求对象

```java
given: "设置请求参数"
def user1 = new UserDTO(id:1, name:"张三", province: "上海")
def user2 = new UserDTO(id:2, name:"李四", province: "江苏")
```
实际上UserDTO.java 这个类并没有3个参数的构造函数，是groovy帮我们实现的，groovy默认会提供一个包含所有对象属性的构造函数

而且调用方式上可以指定属性名，类似于key:value的语法，非常人性化，方便我们在属性多的情况下构造对象，如果使用java写，可能就要调用很多setXXX()方法才能完成对象初始化的工作
```java
and: "mock掉接口返回的用户信息"
userDao.getUserInfo() >> [user1, user2]
```
这个就是spock的mock用法，即当调用userDao.getUserInfo()方法时返回一个List，list的创建也很简单，中括号"[]"即表示list，groovy会根据方法的返回类型自动匹配是数组还是list，而list里的对象就是之前given块里构造的user对象

其中 ">>" 就是指定返回结果，类似mockito的when().thenReturn()语法，但更简洁一些

如果要指定返回多个值的话可以使用3个右箭头">>>"，比如：

userDao.getUserInfo() >>> [[user1,user2],[user3,user4],[user5,user6]]

也可以写成这样：

userDao.getUserInfo() >> [user1,user2] >> [user3,user4] >> [user5,user6]

即每次调用userDao.getUserInfo()方法返回不同的值

如果mock的方法带有入参的话，比如下面的业务代码：

```java
public List<UserDTO> getUserInfo(String uid){
    // 模拟用户中心服务接口调用
    List<UserDTO> users = new ArrayList<>();
    return users;
}
```
这个getUserInfo(String uid)方法，有个参数uid，这种情况下如果使用spock的mock模拟调用的话，可以使用下划线"_"匹配参数，表示任何类型的参数，多个逗号隔开，类似与mockito的any()方法

如果类中存在多个同名函数，可以通过 "_ as 参数类型" 的方式区别调用，类似下面的语法：

```java
// _ 表示匹配任意类型参数
List<UserDTO> users = userDao.getUserInfo(_);

// 如果有同名的方法，使用as指定参数类型区分
List<UserDTO> users = userDao.getUserInfo(_ as String);
```
when模块里是真正调用要测试方法的入口:userService.getUserById()

then模块作用是验证被测方法的结果是否正确，符合预期值，所以这个模块里的语句必须是boolean表达式，类似于junit的assert断言机制，但你不必显示的写assert，这也是一种**约定优于配置**的思想

then块中使用了spock的with功能，可以验证返回结果response对象内部的多个属性是否符合预期值，这个相对于junit的assertNotNull或assertEquals的方式更简单一些

### 2. where用法

上面的业务代码有3个 if 判断，分别是对邮编和手机号的处理逻辑：

```java
// 显示邮编
if("上海".equals(userDTO.getProvince())){
    userVO.setAbbreviation("沪");
    userVO.setPostCode(200000);
}
if("北京".equals(userDTO.getProvince())){
    userVO.setAbbreviation("京");
    userVO.setPostCode(100000);
}
// 手机号处理
if(null != userDTO.getTelephone() && !"".equals(userDTO.getTelephone())){
    userVO.setTelephone(userDTO.getTelephone().substring(0,3)+"****"+userDTO.getTelephone().substring(7));
}
```
现在的单元测试如果要完全覆盖这3个分支就需要构造不同的请求参数多次调用被测试方法才能走到不同的分支，在上一篇中介绍了 spock 的 where 标签可以很方便的实现这种功能，代码如下：
```java
@Unroll
def "当输入的用户id为:#uid 时返回的邮编是:#postCodeResult，处理后的电话号码是:#telephoneResult"() {
    given: "mock掉接口返回的用户信息"
    userDao.getUserInfo() >> users

    when: "调用获取用户信息方法"
    def response = userService.getUserById(uid)

    then: "验证返回结果是否符合预期值"
    with(response) {
        postCode == postCodeResult
        telephone == telephoneResult
    }

    where: "表格方式验证用户信息的分支场景"
    uid | users                         || postCodeResult | telephoneResult
    1   | getUser("上海", "13866667777") || 200000         | "138****7777"
    1   | getUser("北京", "13811112222") || 100000         | "138****2222"
    2   | getUser("南京", "13833334444") || 0              | null
}

def getUser(String province, String telephone){
    return [new UserDTO(id: 1, name: "张三", province: province, telephone: telephone)]
}
```
where模块第一行代码是表格的列名，多个列使用"|"单竖线隔开，"||"双竖线区分输入和输出变量，即左边是输入值，右边是输出值

格式如下:

输入参数1 | 输入参数2 || 输出结果1 | 输出结果2

而且intellij idea支持format格式化快捷键，因为表格列的长度不一样，手动对齐比较麻烦

表格的每一行代表一个测试用例，即被测方法被测试了3次，每次的输入和输出都不一样，刚好可以覆盖全部分支情况

比如uid、users都是输入条件，其中users对象的构造调用了getUser方法，每次测试业务代码传入不同的user值，postCodeResult、telephoneResult表示对返回的response对象的属性判断是否正确

第一行数据的作用是验证返回的邮编是否是"200000"，第二行是验证邮编是否是"100000"，第三行的邮编是否是"0"(因为代码里没有对南京的邮编进行处理，所以默认值是0)

这个就是where+with的用法，更符合我们实际测试的场景，既能覆盖多种分支，又可以对复杂对象的属性进行验证

其中在第2行定义的测试方法名是使用了groovy的字面值特性：
```
@Unroll
def "当输入的用户id为:#uid 时返回的邮编是:#postCodeResult，处理后的电话号码是:#telephoneResult"() {
```
即把请求参数值和返回结果值的字符串里动态替换掉，"#uid、#postCodeResult、#telephoneResult" 井号后面的变量是在方法内部定义的，前面加上#号，实现占位符的功能

@Unroll注解，可以把每一次调用作为一个单独的测试用例运行，这样运行后的单测结果更直观：

## Spock代码讲解 - if esle 分支场景测试
### 1. expect + where

如果业务比较复杂，对应的代码实现会有不同的分支逻辑，类似下面的伪代码：

```java
if () {
    if () {
        // 代码逻辑
    } else {
        // 代码逻辑
    }
} else if () {
    for () {
        if () {
            // 代码逻辑
        } else {
            // 代码逻辑
            return result;
        }
    }
}
```
这样的 if else 嵌套代码因为业务的原因很难避免，如果要测试这样的代码，保证**覆盖到每一个分支逻辑**的话，使用传统的Junit单元测试代码写起来会很痛苦和繁琐，虽然可以使用Junit的@parametered参数化注解或者dataprovider的方式，但还是不够直观，调试起来也不方便

下面就结合具体业务代码讲解Spock如何解决这种问题，还是先看下业务代码逻辑：

```java
/**
 * 身份证号码工具类<p>
 * 15位：6位地址码+6位出生年月日（900101代表1990年1月1日出生）+3位顺序码
 * 18位：6位地址码+8位出生年月日（19900101代表1990年1月1日出生）+3位顺序码+1位校验码
 * 顺序码奇数分给男性，偶数分给女性。
 * @author 公众号:Java老K
 * 个人博客:www.javakk.com
 */
public class IDNumberUtils {
    /**
     * 通过身份证号码获取出生日期、性别、年龄
     * @param certificateNo
     * @return 返回的出生日期格式：1990-01-01 性别格式：F-女，M-男
     */
    public static Map<String, String> getBirAgeSex(String certificateNo) {
        String birthday = "";
        String age = "";
        String sex = "";

        int year = Calendar.getInstance().get(Calendar.YEAR);
        char[] number = certificateNo.toCharArray();
        boolean flag = true;
        if (number.length == 15) {
            for (int x = 0; x < number.length; x++) {
                if (!flag) return new HashMap<>();
                flag = Character.isDigit(number[x]);
            }
        } else if (number.length == 18) {
            for (int x = 0; x < number.length - 1; x++) {
                if (!flag) return new HashMap<>();
                flag = Character.isDigit(number[x]);
            }
        }
        if (flag && certificateNo.length() == 15) {
            birthday = "19" + certificateNo.substring(6, 8) + "-"
                    + certificateNo.substring(8, 10) + "-"
                    + certificateNo.substring(10, 12);
            sex = Integer.parseInt(certificateNo.substring(certificateNo.length() - 3,
                    certificateNo.length())) % 2 == 0 ? "女" : "男";
            age = (year - Integer.parseInt("19" + certificateNo.substring(6, 8))) + "";
        } else if (flag && certificateNo.length() == 18) {
            birthday = certificateNo.substring(6, 10) + "-"
                    + certificateNo.substring(10, 12) + "-"
                    + certificateNo.substring(12, 14);
            sex = Integer.parseInt(certificateNo.substring(certificateNo.length() - 4,
                    certificateNo.length() - 1)) % 2 == 0 ? "女" : "男";
            age = (year - Integer.parseInt(certificateNo.substring(6, 10))) + "";
        }
        Map<String, String> map = new HashMap<>();
        map.put("birthday", birthday);
        map.put("age", age);
        map.put("sex", sex);
        return map;
    }
}
```
根据输入的身份证号码识别出生日期、性别、年龄等信息，逻辑不复杂，就是分支多，我们来看下Spock代码是如何测试这种情况：
```java
class IDNumberUtilsTest extends Specification {

    @Unroll
    def "身份证号:#idNo 的生日,性别,年龄是:#result"() {
        expect: "when + then 组合"
        IDNumberUtils.getBirAgeSex(idNo) == result

        where: "表格方式测试不同的分支逻辑"
        idNo                 || result
        "310168199809187333" || ["birthday": "1998-09-18", "sex": "男", "age": "22"]
        "320168200212084268" || ["birthday": "2002-12-08", "sex": "女", "age": "18"]
        "330168199301214267" || ["birthday": "1993-01-21", "sex": "女", "age": "27"]
        "411281870628201"    || ["birthday": "1987-06-28", "sex": "男", "age": "33"]
        "427281730307862"    || ["birthday": "1973-03-07", "sex": "女", "age": "47"]
        "479281691111377"    || ["birthday": "1969-11-11", "sex": "男", "age": "51"]
    }
}
```
在测试方法体的第一行使用了expect标签，它的作用是when + then标签的组合，即 "什么时候做什么 + 然后验证什么结果" 组合起来

即当调用IDNumberUtils.getBirAgeSex(idNo) 方法时，验证结果是result，result如何验证对应的就是where里的result一列的数据，当输入参数idNo是"310168199809187333"时，返回结果是: ["birthday": "1998-09-18", "sex": "男", "age": "22"]

expect可以单独使用，可以不需要where，只是在这个场景需要

## Spock代码讲解 - 异常测试

### 异常场景
有些方法需要抛出异常来中断或控制流程，比如参数校验的逻辑: 不能为null，不符合指定的类型，list不能为空等验证，如果校验不通过则抛出checked异常，这个异常一般都是我们封装的业务异常信息，比如下面的业务代码：

```java
/**
 * 校验请求参数user是否合法
 * @param user
 * @throws APIException
 */
public void validateUser(UserVO user) throws APIException {
    if(user == null){
        throw new APIException("10001", "user is null");
    }
    if(null == user.getName() || "".equals(user.getName())){
        throw new APIException("10002", "user name is null");
    }
    if(user.getAge() == 0){
        throw new APIException("10003", "user age is null");
    }
    if(null == user.getTelephone() || "".equals(user.getTelephone())){
        throw new APIException("10004", "user telephone is null");
    }
    if(null == user.getSex() || "".equals(user.getSex())){
        throw new APIException("10005", "user sex is null");
    }
    if(null == user.getUserOrders() || user.getUserOrders().size() <= 0){
        throw new APIException("10006", "user order is null");
    }
    for(OrderVO order : user.getUserOrders()) {
        if (null == order.getOrderNum() || "".equals(order.getOrderNum())) {
            throw new APIException("10007", "order number is null");
        }
        if (null == order.getAmount()) {
            throw new APIException("10008", "order amount is null");
        }
    }
}
```
APIException是我们封装的业务异常，主要包含errorCode，errorMessage属性：

```java
/**
 * 自定义业务异常
 */
public class APIException extends RuntimeException {
    private String errorCode;
    private String errorMessage;

    setXXX...
    getXXX...
}
```
这个大家应该都很熟悉，针对这种抛出多个不同错误码和错误信息的异常，如果我们使用Junit的方式测试，会比较麻烦，就目前我使用过的方法，如果是单个异常还好，多个的就不太好写测试代码

最常见的写法可能是下面这样:

```java
@Test 
public void testException() {
  UserVO user = null;
  try {
    validateUser(user);
  } catch (APIException e) {
    assertThat(e.getErrorCode(), "10001");
    assertThat(e.getErrorMessage(), "user is null");
  }

  UserVO user = new UserVO();
  try {
    validateUser(user);
  } catch (APIException e) {
    assertThat(e.getErrorCode(), "10002");
    assertThat(e.getErrorMessage(), "user name is null");
  }
  ...
}
```
当然可以使用junit的ExpectedException方式:

```java
@Rule
public ExpectedException exception = ExpectedException.none();
exception.expect(APIException.class); // 验证抛出异常的类型是否符合预期
exception.expectMessage("Order Flight return null exception"); //验证抛出异常的错误信息
```
或者使用@Test(expected = APIException.class) 注解

但这两种方式都有缺陷:

@Test方式不能指定断言的异常属性，比如errorCode，errorMessage

ExpectedException的方式也只提供了expectMessage的api，对自定义的errorCode不支持，尤其像上面的有很多分支抛出多种不同异常码的情况

### thrown
我们来看下Spock是如何解决的，Spock内置thrown()方法，可以捕获调用业务代码抛出的预期异常并验证，再结合where表格的功能，可以很方便的覆盖多种自定义业务异常，代码如下:

```java
/**
 * 校验用户请求参数的测试类
 * @author 公众号:Java老K
 * 个人博客:www.javakk.com
 */
class UserControllerTest extends Specification {

    def userController = new UserController()

    @Unroll
    def "验证用户信息的合法性: #expectedMessage"() {
        when: "调用校验用户方法"
        userController.validateUser(user)

        then: "捕获异常并设置需要验证的异常值"
        def exception = thrown(expectedException)
        exception.errorCode == expectedErrCode
        exception.errorMessage == expectedMessage

        where: "表格方式验证用户信息的合法性"
        user           || expectedException | expectedErrCode | expectedMessage
        getUser(10001) || APIException      | "10001"         | "user is null"
        getUser(10002) || APIException      | "10002"         | "user name is null"
        getUser(10003) || APIException      | "10003"         | "user age is null"
        getUser(10004) || APIException      | "10004"         | "user telephone is null"
        getUser(10005) || APIException      | "10005"         | "user sex is null"
        getUser(10006) || APIException      | "10006"         | "user order is null"
        getUser(10007) || APIException      | "10007"         | "order number is null"
        getUser(10008) || APIException      | "10008"         | "order amount is null"
    }

    def getUser(errCode) {
        def user = new UserVO()
        def condition1 = {
            user.name = "杜兰特"
        }
        def condition2 = {
            user.age = 20
        }
        def condition3 = {
            user.telephone = "15801833812"
        }
        def condition4 = {
            user.sex = "男"
        }
        def condition5 = {
            user.userOrders = [new OrderVO()]
        }
        def condition6 = {
            user.userOrders = [new OrderVO(orderNum: "123456")]
        }

        switch (errCode) {
            case 10001:
                user = null
                break
            case 10002:
                user = new UserVO()
                break
            case 10003:
                condition1()
                break
            case 10004:
                condition1()
                condition2()
                break
            case 10005:
                condition1()
                condition2()
                condition3()
                break
            case 10006:
                condition1()
                condition2()
                condition3()
                condition4()
                break
            case 10007:
                condition1()
                condition2()
                condition3()
                condition4()
                condition5()
                break
            case 10008:
                condition1()
                condition2()
                condition3()
                condition4()
                condition5()
                condition6()
                break
        }
        return user
    }
}
```
主要代码就是在"验证用户信息的合法性"的测试方法里，其中在then标签里用到了Spock的thrown()方法，这个方法可以捕获我们要测试的业务代码里抛出的异常

thrown方法的入参expectedException，是我们自己定义的异常变量，这个变量放在where标签里就可以实现验证多种异常情况的功能(intellij idea格式化快捷键可以自动对齐表格)

expectedException的类型是我们调用的validateUser方法里定义的APIException异常，我们可以验证它的所有属性，errorCode、errorMessage是否符合预期值

另外在where标签里构造请求参数时调用的getUser()方法使用了groovy的闭包功能，即case里面的condition1，condition2的写法

groovy的闭包(closure) 类似Java的lambda表达式，这样写主要是为了复用之前的请求参数，所以使用了闭包，当然也可以使用传统的new对象之后，setXXX的方式构造请求对象

## Spock代码讲解 - void方法测试
### void方法
void方法的测试不能像前面几篇介绍的那样在then标签里验证返回结果，因为void方法没有返回值

一般来说无返回值的方法，内部逻辑会修改入参的属性值，比如参数是个对象，那代码里可能会修改它的属性值，虽然没有返回，但还是可以通过校验入参的属性来测试void方法

还有一种更有效的测试方式，就是验证方法内部逻辑和流程是否符合预期，比如:

 - 应该走到哪个分支逻辑？
 - 是否执行了这一行代码?
 -  for循环中的代码执行了几次？
 - 变量在方法内部的变化情况？

先看一个void方法的业务代码示例：

```java
/**
 * 根据汇率计算金额
 * @param userVO
 */
public void setOrderAmountByExchange(UserVO userVO){
    if(null == userVO.getUserOrders() || userVO.getUserOrders().size() <= 0){
        return ;
    }
    for(OrderVO orderVO : userVO.getUserOrders()){
        BigDecimal amount = orderVO.getAmount();
        // 获取汇率(调用汇率接口)
        BigDecimal exchange = moneyDAO.getExchangeByCountry(userVO.getCountry());
        amount = amount.multiply(exchange); // 根据汇率计算金额
        orderVO.setAmount(amount);
    }
}

```
这个void方法主要是遍历userVO下面的订单，通过调用汇率接口计算订单的外币金额，然后再赋值给userVO.orderVO.amount，所以他的核心逻辑在for循环里，那么我们的测试重点就是验证for循环里面的逻辑是否符合预期，金额计算是否正确

### 代码实现
直接看Spock的测试代码如何写:

```java
/**
 * 用户服务测试类
 * @author 公众号:Java老K
 * 个人博客:www.javakk.com
 */
class UserServiceTest extends Specification {
    def userService = new UserService()
    def moneyDAO = Mock(MoneyDAO)

    void setup() {
        userService.userDao = userDao
        userService.moneyDAO = moneyDAO
    }

    def "测试void方法"() {
        given: "设置请求参数"
        def userVO = new UserVO(name:"James", country: "美国")
        userVO.userOrders = [new OrderVO(orderNum: "1", amount: 10000), new OrderVO(orderNum: "2", amount: 1000)]

        when: "调用设置订单金额的方法"
        userService.setOrderAmountByExchange(userVO)

        then: "验证调用获取最新汇率接口的行为是否符合预期: 一共调用2次, 第一次输出的汇率是0.1413, 第二次是0.1421"
        2 * moneyDAO.getExchangeByCountry(_) >> 0.1413 >> 0.1421

        and: "验证根据汇率计算后的金额结果是否正确"
        with(userVO){
            userOrders[0].amount == 1413
            userOrders[1].amount == 142.1
        }
    }
}
```
主要是then标签里的语法: "2 * moneyDAO.getExchangeByCountry(_) >> 0.1413 >> 0.1421"，这行代码表示moneyDAO的getExchangeByCountry()方法会被执行2次，第一次输出的结果是0.1413，第二次输出的接口是0.1421

"2 * moneyDAO.getExchangeByCountry(_) >> 0.1413 >> 0.1421" 这行代码也可以分开写，比如只写前面的""2 * moneyDAO.getExchangeByCountry(_)"

"2 * " 表示方法实际执行的次数， 如果不是2次则不符合预期，单元测试会失败，看你具体的传参，比如在given标签里我们构造的user下面有2个order，订单号分别是1，2，金额分别是1w，1k

那么在调用void方法时，for循环就会循环2次，所以可以通过这样的写法验证我们调用汇率接口的方法是否执行了，以及执行次数

最后在with()方法里会对入参userVO里的订单金额amount进行校验，因为我们设置的两单订单金额分别是1w和1k，then标签已经对汇率接口的返回结果mock了2个不同的汇率值:0.1413、0.1421，那么转换后的外币金额就是1413和142.1元

你也可以将代码改成"1 * moneyDAO.getExchangeByCountry(_)"，然后运行单测，会提示相应的错误信息：

报错信息说明实际执行(invoke)了2次

执行几次就写几次，没有执行过就是"0 * "，这正是BDD行为驱动开发思想的体现

(power mock的thenVerify()也可以实现这样的功能，只不过Spock语法更简洁一些)

### void + where
如果要结合where测试多分支的void方法时，需要注意一点，因为Spock要求where标签里的表格至少含有两列，如果你的where只是验证入参，也就是只有一列需要验证，那么可以用"_" 表示另外一列值，代码类似下面这样的写法:

```java
where:
requsetParam | _
userVO1      | _
userVO2      | _
userVO3      | _
```
使用“_”表示任意输入或输出

## Spock代码讲解 - 静态方法测试
这一章主要讲解Spock如何扩展第三方power mock对静态方法进行测试

### 实现原理
前面的文章讲到Spock的单测代码是继承自Specification基类，而Specification又是基于Junit的注解@RunWith()实现的：
```java
@RunWith(Sputnik.class)
@SuppressWarnings("UnusedDeclaration")
public abstract class Specification extends MockingApi {
```
powermock的PowerMockRunner也是继承自Junit，所以使用powermock的@PowerMockRunnerDelegate()注解可以指定Spock的父类Sputnik去代理运行power mock，这样就可以在Spock里使用powermock去模拟静态方法、final方法、私有方法等

其实Spock自带的GroovyMock可以对groovy文件的静态方法mock，但对Java代码的支持不完整，只能mock当前Java类的静态方法，官方给出的解释如下:
([http://spockframework.org/spock/docs/1.3/all_in_one.html#_mocking_static_methods](http://spockframework.org/spock/docs/1.3/all_in_one.html#_mocking_static_methods))

因为我们项目中存在很多调用静态方法的代码，现阶段考虑重构业务代码的成本过高，所以这里使用扩展power mock的方式测试静态方法

### Spock 代理 power mock
先看下需要测试的业务代码示例:

```java
public UserVO getUserByIdStatic(int uid){
    List<UserDTO> users = userDao.getUserInfo();
    UserDTO userDTO = users.stream().filter(u -> u.getId() == uid).findFirst().orElse(null);
    UserVO userVO = new UserVO();
    if(null == userDTO){
        return userVO;
    }
    userVO.setId(userDTO.getId());
    userVO.setName(userDTO.getName());
    userVO.setSex(userDTO.getSex());
    if("上海".equals(userDTO.getProvince())){
        userVO.setAbbreviation("沪");
        userVO.setPostCode(200000);
    }
    if("北京".equals(userDTO.getProvince())){
        userVO.setAbbreviation("京");
        userVO.setPostCode(100000);
    }
    if(null != userDTO.getTelephone() && !"".equals(userDTO.getTelephone())){
        userVO.setTelephone(userDTO.getTelephone().substring(0,3)+"****"+userDTO.getTelephone().substring(7));
    }
    // 静态方法调用 身份证工具类
    Map<String, String> idMap = IDNumberUtils.getBirAgeSex(userDTO.getIdNo());
    userVO.setAge(idMap.get("age")!=null ? Integer.parseInt(idMap.get("age")) : 0);
    // 静态方法调用 记录日志
    LogUtils.info("response user:", userVO.toString());
    return userVO;
}
```
在倒数第4行和倒数第2行代码分别调用了 "身份证工具类IDNumberUtils.getBirAgeSex()" 和 "LogUtils.info()" 日志记录的方法，如果要对这两个静态方法进行mock，我们可以使用Spock+power mock的方式:

```java
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.spockframework.runtime.Sputnik
import spock.lang.Specification

/**
 * 测试静态方法mock
 * @Author: www.javakk.com
 * @Description: 公众号:Java老K
 * @Date: Created in 20:53 2020/7/16
 * @Modified By:
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(Sputnik.class)
@PrepareForTest([LogUtils.class, IDNumberUtils.class])
@SuppressStaticInitializationFor(["com.javakk.spock.util.LogUtils"])
class UserServiceStaticTest extends Specification {
    def processor = new UserService()
    def dao = Mock(UserDao)

    void setup() {
        processor.userDao = dao
        // mock静态类
        PowerMockito.mockStatic(LogUtils.class)
        PowerMockito.mockStatic(IDNumberUtils.class)
    }

    def "GetUserByIdStatic"() {
        given: "设置请求参数"
        def user1 = new UserDTO(id:1, name:"张三", province: "上海")
        def user2 = new UserDTO(id:2, name:"李四", province: "江苏")
        def idMap = ["birthday": "1992-09-18", "sex": "男", "age": "28"]

        and: "mock掉接口返回的用户信息"
        dao.getUserInfo() >> [user1, user2]

        and: "mock静态方法返回值"
        PowerMockito.when(IDNumberUtils.getBirAgeSex(Mockito.any())).thenReturn(idMap)

        when: "调用获取用户信息方法"
        def response = processor.getUserByIdStatic(1)

        then: "验证返回结果是否符合预期值"
        with(response) {
            name == "张三"
            abbreviation == "沪"
            postCode == 200000
            age == 28
        }
    }
}
```
在UserServiceStaticTest类的头部使用@PowerMockRunnerDelegate(Sputnik.class)注解，交给Spock代理执行，**这样既可以使用 Spock + groovy 的各种功能，又可以使用power mock的对静态，final等方法的mock**

@SuppressStaticInitializationFor(["com.javakk.spock.util.LogUtils"])
这行代码的作用是限制LogUtils类里的静态代码块初始化，因为LogUtils类在第一次调用时会加载一些本地资源配置，比较耗费时间，所以可以使用power mock禁止初始化

然后在setup()方法里对两个静态类进行mock设置，PowerMockito.mockStatic(LogUtils.class)，PowerMockito.mockStatic(IDNumberUtils.class)

最后在GetUserByIdStatic测试方法里对getBirAgeSex()方法指定返回默认值:PowerMockito.when(IDNumberUtils.getBirAgeSex(Mockito.any())).thenReturn(idMap)

(power mock的具体用法网上资料很多，这里不展开说明)

运行时在控制台会输出:
Notifications are not supported for behaviour ALL_TESTINSTANCES_ARE_CREATED_FIRST

这是powermock的警告信息，不影响运行结果

另外，**如果你的单元测试代码不需要对静态方法, final方法mock, 就没必要使用power mock, 使用Spock自带的Mock()就足够了**

因为power mock的原理是在编译期通过ASM字节码修改工具修改我们的代码，然后使用自己的classLoader加载，加载的静态方法越多会相应的增加测试时长

## Spock高级用法 - 动态mock
这一章讲解Spock自带的mock功能如何和power mock组合使用，发挥更强大的作用

### 动态mock静态方法 (spock where + power mock)
在上一篇的例子中使用power mock让静态方法返回一个指定的值，那能不能每次返回不同的值呢？

我们先看下什么场景需要这样做:

```java
/**
 * 静态方法多分支场景
 * @param userVO
 * @return
 */
public List<OrderVO> getUserOrdersBySource(UserVO userVO){
    List<OrderVO> orderList = new ArrayList<>();
    OrderVO order = new OrderVO();
    if ("APP".equals(HttpContextUtils.getCurrentSource())) { // 手机来源
        if("CNY".equals(HttpContextUtils.getCurrentCurrency())){ // 人民币
            // TODO 针对App端的订单，并且请求币种为人民币的业务逻辑...
            System.out.println("source -> APP, currency -> CNY");
        } else {
            System.out.println("source -> APP, currency -> !CNY");
        }
        order.setType(1);
    } else if ("WAP".equals(HttpContextUtils.getCurrentSource())) { // H5来源
        // TODO 针对H5端的业务逻辑...
        System.out.println("source -> WAP");
        order.setType(2);
    } else if ("ONLINE".equals(HttpContextUtils.getCurrentSource())) { // PC来源
        // TODO 针对PC端的业务逻辑...
        System.out.println("source -> ONLINE");
        order.setType(3);
    }
    orderList.add(order);
    return orderList;
}
```
这段代码的 if else 分支逻辑主要是依据HttpContextUtils这个工具类的静态方法 getCurrentSource() 和 getCurrentCurrency() 的返回值决定流程的

这样的业务代码也是我们平时写单测经常遇到的场景，如果能让HttpContextUtils.getCurrentSource() 静态方法每次mock出不同的值，就可以很方便的覆盖if else的全部分支逻辑

Spock的where标签可以方便的和power mock结合使用，让power mock模拟的静态方法每次返回不同的值，代码如下:

```java
/**
 * 测试静态方法mock
 * @Author: www.javakk.com
 * @Description: 公众号:Java老K
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(Sputnik.class)
@PrepareForTest([HttpContextUtils.class])
class OrderServiceStaticTest extends Specification {

    def orderService = new OrderService()

    void setup() {
        // mock静态类
        PowerMockito.mockStatic(HttpContextUtils.class)
    }

    /**
     * 测试spock的mock和power mock静态方法组合用法的场景
     */
    @Unroll
    def "当来源是#source时，订单类型为:#type"() {
        given: "mock当前上下文的请求来源"
        PowerMockito.when(HttpContextUtils.getCurrentSource()).thenReturn(source)

        and: "mock当前上下文的币种"
        PowerMockito.when(HttpContextUtils.getCurrentCurrency()).thenReturn(currency)

        when: "调用获取用户订单列表"
        def orderList = orderService.getUserOrdersBySource(new UserVO())

        then: "验证返回结果是否符合预期值"
        with(orderList) {
            it[0].type == type
        }

        where: "表格方式验证订单信息的分支场景"
        source   | currency || type
        "APP"    | "CNY"    || 1
        "APP"    | "USD"    || 1
        "WAP"    | ""       || 2
        "ONLINE" | ""       || 3
    }
}
```
powermock的thenReturn方法返回的值是 source 和 currency 两个**变量**，不是具体的数据，这两个变量对应where标签里的前两列 "source | currency" 

这样的写法就可以每次测试业务方法时，让HttpContextUtils.getCurrentSource()和HttpContextUtils.getCurrentCurrency() 返回不同的来源和币种，就能轻松的覆盖if和else的分支代码

即**Spock使用where表格的方式让power mock具有了动态mock的功能**

### 动态mock接口 (spock mock + power mock + where)
上个例子讲了把power mock返回的mock值作为变量放在where里使用，以达到动态mock静态方法的功能，这里再介绍一种动态mock 静态+final变量的用法，还是先看业务代码，了解这么做的背景:

```java
/**
 * 静态final变量场景
 * @param orders
 * @return
 */
public List<OrderVO> convertUserOrders(List<OrderDTO> orders){
    List<OrderVO> orderList = new ArrayList<>();
    for (OrderDTO orderDTO : orders) {
        OrderVO orderVO = OrderMapper.INSTANCE.convert(orderDTO); // VO DTO 属性转换
        if (1 == orderVO.getType()) {
            orderVO.setOrderDesc("App端订单");
        } else if(2 == orderVO.getType()) {
            orderVO.setOrderDesc("H5端订单");
        } else if(3 == orderVO.getType()) {
            orderVO.setOrderDesc("PC端订单");
        }
        orderList.add(orderVO);
    }
    return orderList;
}
```
这段代码里的for循环第一行调用了"OrderMapper.INSTANCE.convert()"转换方法，将orderDTO转换为orderVO，然后根据type的值走不同的分支

而OrderMapper是一个接口，代码如下:

```java
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 订单属性转换
 */
@Mapper
public interface OrderMapper {

    // 即使不用static final修饰，接口里的变量默认也是静态、final的
    static final OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mappings({})
    OrderVO convert(OrderDTO requestDTO);
}
```
"INSTANCE"是接口OrderMapper里定义的变量，接口里的变量默认都是static final的，所以我们要先把这个INSTANCE静态final变量mock掉，这样才能调用它的方法convert() 返回我们想要的值

OrderMapper这个接口是mapstruct工具的用法，mapstruct是做对象属性映射的一个工具，它会自动生成OrderMapper接口的实现类，生成对应的set、get方法，把orderDTO的属性值赋给orderVO属性，比使用反射的方式好很多(具体用法自行百度)

看下Spock如何写这个单元测试:

```java
/**
 * 测试spock的mock和powermock静态final变量结合的用法
 */
@Unroll
def "ConvertUserOrders"() {
    given: "mock掉OrderMapper的静态final变量INSTANCE,并结合spock设置动态返回值"
    def orderMapper = Mock(OrderMapper.class)
    Whitebox.setInternalState(OrderMapper.class, "INSTANCE", orderMapper)
    orderMapper.convert(_) >> order

    when: "调用用户订单转换方法"
    def userOrders = orderService.convertUserOrders([new OrderDTO()])

    then: "验证返回结果是否符合预期值"
    with(userOrders) {
        it[0].orderDesc == desc
    }

    where: "表格方式验证订单属性转换结果"
    order                || desc
    new OrderVO(type: 1) || "App端订单"
    new OrderVO(type: 2) || "H5端订单"
    new OrderVO(type: 3) || "PC端订单"
}
```
1. 首先使用Spock自带的Mock()方法，将OrderMapper类mock为一个模拟对象orderMapper，"def orderMapper = Mock(OrderMapper.class)"

2. 然后使用power mock的Whitebox.setInternalState()对OrderMapper接口的static final常量INSTANCE赋值(Spock不支持静态常量的mock)，赋的值正是使用spock mock的对象orderMapper

3. 使用Spock的mock模拟convert()方法调用，"orderMapper.convert(_) >> order"，再结合where表格，实现动态mock接口的功能

主要就是这3行代码:

```java
def orderMapper = Mock(OrderMapper.class) // 先使用Spock的mock
Whitebox.setInternalState(OrderMapper.class, "INSTANCE", orderMapper) // 将第一步mock的对象orderMapper 使用power mock赋值给静态常量INSTANCEmock
orderMapper.convert(_) >> order // 结合where模拟不同的返回值
```
这样就可以使用Spock mock结合power mock测试静态常量，达到覆盖if else不同分支逻辑的功能

由此可见Spock可以和power mock深度结合，测试一些特殊的场景，也可以按照这个思路继续挖掘其他用法

## 注意事项
Spock虽然好用，但要应用到实际项目中还是需要注意几个问题，下面讲下我们公司在使用过程中遇到的一些问题和解决方案

### 1. 版本依赖
要使用Spock首先需要引入相关依赖，目前使用下来和我们项目兼容的Spock版本是1.3-groovy-2.5，以maven为例(gradle可以参考官网)，完整的pom依赖如下:

```xml
<spock.version>1.3-groovy-2.5</spock.version>
<groovy.version>2.5.4</groovy.version>
 
<!-- spock -->
<dependency>
    <groupId>org.spockframework</groupId>
    <artifactId>spock-core</artifactId>
    <version>${spock.version}</version>
    <scope>test</scope>
</dependency>
<!-- spock和spring集成 -->
<dependency>
    <groupId>org.spockframework</groupId>
    <artifactId>spock-spring</artifactId>
    <version>${spock.version}</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <scope>test</scope>
</dependency>
<!-- spock依赖的groovy -->
<dependency>
    <groupId>org.codehaus.groovy</groupId>
    <artifactId>groovy-all</artifactId>
    <type>pom</type>
    <version>${groovy.version}</version>
    <exclusions>
        <exclusion>
            <artifactId>groovy-test-junit5</artifactId>
            <groupId>org.codehaus.groovy</groupId>
        </exclusion>
        <exclusion>
            <artifactId>groovy-testng</artifactId>
            <groupId>org.codehaus.groovy</groupId>
        </exclusion>
    </exclusions>
</dependency>
 
<!--groovy 编译-->
<plugin>
    <groupId>org.codehaus.gmavenplus</groupId>
    <artifactId>gmavenplus-plugin</artifactId>
    <version>1.6</version>
    <executions>
        <execution>
            <goals>
                <goal>compile</goal>
                <goal>compileTests</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
Spock是使用groovy语言写单测的，所以需要引入groovy-all的依赖

在引入 groovy-all 包时排除了 groovy-test-junit5 和 groovy-testng，这两个包和和 power mock 有冲突，在执行 mvn test 会导致NPE的问题

如果你的项目中没有用过groovy，还需要添加groovy的maven编译插件，这样才能编译我们用Spock写的单元测试

**引入groovy依赖后可能会出现版本冲突的问题**，因为如果你的项目引用了springboot-start-base这样的集合式jar包，它里面也会引用groovy，有可能跟我们引入的groovy包版本出现冲突，或者公司的一些框架也会引用groovy的包，如果版本不一致也有可能冲突，需要排下包

然后执行 mvn clean compile 验证下是否有冲突，如果能成功编译就没有这个问题

目前Spock的最新版本是2.0以上，在Spock 2.x 的版本里官方团队**已经移除Sputnik，不再支持代理运行power mock的方式**

因为**Spock 2.0是基于JUnit5**，我们项目以前的单元测试代码都是基于Junit4编写的，换成Junit5后，需要修改现有的java单测，比如指定代理运行，使用power mock的地方要换成Junit5的扩展语法

对现有使用Junit4 + power mock/jmockit的方式改变较大，为降低迁移成本没有使用最新的Spock2.X版本

如果你的项目之前就是使用Junit5写单测的，那么可以使用Spock2.X的版本，2.0以上版本使用power mock可以参考官方提供的解决方案：

([https://github.com/spockframework/spock/commit/fa8bd57cbb2decd70647a5b5bc095ba3fdc88ee9](https://github.com/spockframework/spock/commit/fa8bd57cbb2decd70647a5b5bc095ba3fdc88ee9))

后续我也会优先在我的博客([http://javakk.com/](http://javakk.com/))推出 Spock2.x 版本的使用教程

### 2. 创建单元测试文件
编译(mvn clean compile)通过之后，用spock编写的groovy类型的单测代码不能放在原来的test/java目录下面

因为按照groovy的约定，默认编译groovy包下的单测，所以需要建个groovy文件夹存放spock的单测代码

这样也方便区分原来Java单测和用Spock写的单测代码

另外记得别忘了标记groovy目录为测试源目录(Test Source Root)

groovy文件夹右键 → Mark Directory as → Test Sources Root）

第一次运行spock单测代码时如果提示"no test suite exist"的错误，可以右键recompile下

还有记得创建的单测文件类型是Groovy Class，不是Java Class类型

最后使用intellij idea的快捷键创建单元测试，在需要测试的类或方法上右键IDE的菜单，选择"Go To → Create New Test" 选择我们已经创建好的groovy文件夹:

这样就自动生成了groovy类型的单测文件了

### 3. 运行单元测试

执行mvn test，按照上面两步的配置保证spock单测代码运行成功后可以执行mvn clean test命令，跑一下这个项目的单测用例

(这一步不是必须的，但如果公司加了单测覆盖率的统计时，在cicd系统发布时或merge request to release代码合并到release分支时，会先执行mvn test类似的指令，确保所有的单元测试运行成功)

如果你的项目和我们一样既有Java单测又有Spock单测，需要确保两种单测都能执行成功(目前我们项目的spock单测和java单测在公司的CICD系统以及git上都能兼容和通过测试覆盖率要求)

另外按照Spock的规范，单测代码文件的命名应该是以Spec为后缀的，如果你严格按照这个规范命名单测文件，比如"OrderServiceSpec.groovy"，那么需要在maven-surefire-plugin测试插件里添加以Spec为后缀的配置：

```xml
<plugin>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>${surefire.version}</version>
    <configuration>
        <includes>
            <include>**/*Spec.java</include>
            <include>**/*Test.java</include>
        </includes>
    </configuration>
</plugin>
```
但是我是直接使用IDE生成单元测试，intellij idea自动生成的单测后缀还是“Test”，所以不存在这个问题，如果你也是这样，可以忽略这个问题

### 4. 单元测试规范

Spock虽然使用方便，但还是要遵循单元测试的规范来，比如单元测试一般是针对方法或类的维度去测试的，也就是说我们关注的重点是当前类或方法内部的逻辑

如果当前被测方法依赖了其他层或module的逻辑，最好mock掉，尽量**不要跨层测试**，这属于功能测试或集成测试的范畴

比如使用@SpringBootTest注解，默认会把当前方法依赖的下一层引用也注入进来，其实完全可以交给Spock去控制，可以不需要SpringBootTest

后续如果新的问题或注意事项会持续更新

## 小结
### 优点
 - 遵循BDD模式、功能强大、语义规范、可读性好、易于维护、富有表现力 
 - 更灵活的控制测试行为，专注代码的逻辑测试而不是书写语法上
 - 用自然语言描述测试步骤（非技术人员也能看懂测试用例） 
 - 兼容mock框架，可以和项目中的java单测代码共存，降低迁移成本 
 - IDE支持良好
 - Groovy动态语言，DSL，语法简洁，约定优于配置，适合敏捷开发

### 缺点
1. 学习成本

Spock本身没什么学习成本，因为它的限制条件不多：必须有至少一个标签、有when必有then

主要是groovy语言，如果用过的会觉得很容易(其实真的很容易)，没用过的也不用担心，因为只要你会java，就会groovy，花个半小时在网上看下它的基本语法即可，因为我们只是用来写单元测试

groovy还有一个特点就是你可以在Spock的单测代码里完全用java代码写，因为groovy完全兼容java语法，或者java和groovy混着写都没问题，因为最终都是编译成class执行的，JVM虚拟机不关心源文件是什么语言

(其实groovy的用途很广，像我们的Jenkins里的pipeline、Elasticsearch、hadoop框架中很多插件都是使用groovy开发的)

2. 单测代码执行时间

groovy语法的简洁可以简单理解为语法糖(其实不完全是，在jvm中执行使用的是invokeDynamic指令)，语法糖会相应的增加jvm构建AST语法树的时间

大家在运行的时候可能会注意到spock代码的编译要比java的单测代码慢一些(视代码复杂度而言，平均大概慢1-2s)，但是执行的时间和java的差不多，如果对这个有要求，慎重使用，最好自己本地验证下 

3. Spock不支持静态、final方法的mock

关于这一点在前面的文章里已经讲过，所以需要引入power mock，也没必要重复造轮子

另外在Spock代码里不能使用power mock的注解，比如@InjectMock，Spock有一些兼容问题，可以使用PowerMockito.mock()的方式代替注解，但可能没有注解的语法简洁

### 总体上利大于弊
有时候你觉得单测代码很难写，说明被测试的代码本身不够合理，需要去关注代码本身的逻辑，设计是否合理，重构业务代码，让你的代码变得容易测试

因为代码的可测试性也是衡量代码质量的重要标准

Spock只是个工具，如果用它都无法解决你的单测case，那就需要把更多的注意力放在业务代码的设计上

总之Spock不能保证让你爱上写单测，但至少不会反感

### 初衷

网上关于Spock的资料过于简单，包括官网的demo，无法解决我们项目中的复杂业务场景，需要找到一套适合自己项目的成熟解决方案

所以觉得有必要把我们项目中使用Spock的经验分享出来，帮助大家解决实际问题或带来一些启发，如果你在使用过程中遇到问题可以在公众号或我的博客([http://javakk.com/](http://javakk.com/))里留言交流

### 参考资料
官网: 
[http://spockframework.org/](http://spockframework.org/)

官网文档:
[http://spockframework.org/spock/docs/1.3/all_in_one.html](http://spockframework.org/spock/docs/1.3/all_in_one.html)

github:
[https://github.com/spockframework/spock](https://github.com/spockframework/spock)

代码示例:
[https://github.com/spockframework/spock-example](https://github.com/spockframework/spock-example)
