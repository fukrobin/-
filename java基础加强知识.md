- [测试](#测试)
  - [黑盒测试](#黑盒测试)
  - [白盒测试](#白盒测试)
- [反射](#反射)
- [注解](#注解)
  - [定义](#定义)
  - [理解](#理解)
  - [作用](#作用)
  - [自定义注解](#自定义注解)
    - [元注解](#元注解)
    - [使用](#使用)
- [函数式数据处理](#函数式数据处理)
  - [引入流](#引入流)
  - [使用流](#使用流)
    - [筛选和切片](#筛选和切片)
    - [映射](#映射)
  - [查找和匹配](#查找和匹配)
  - [规约](#规约)
  - [数值流](#数值流)
  - [构建流](#构建流)
# 测试

## 黑盒测试

无关代码，只关心输入值与输出值的对应关系

## 白盒测试

借助Junit

1. 注解：@Test
2. Junit5：方法名包含test即可

# 反射

机制:将类的各个组成部分（field、method等）封装为其他对象

好处：

1. 可以在和层序运行过程中操作这些对象
2. 可以解耦，提高程序的可扩展性

* 获取构造器
* 获取字段
* 获取方法

# 注解

## 定义

注解（Annotation）JDK1.5之后的新特性，也叫元数据，与类、接口、枚举在同一层次。它可以声明在包、类、字段、方法、局部变量、方法参数的前面，用来对这些参数进行说明、注释

## 理解

类似于注释

注释：给程序员看的

注解：给计算机程序看的

## 作用

1. 编写文档：通过代码标识的元数据生成文档【生成javadoc文档：@link、@see、@param、@return等】
2. 代码分析：通过代码标识的元数据对代码分析【反射】
3. 编写检查：通过代码标识的元数据让编译器能够实现基本的编译检查【**Override**】

## 自定义注解

```java
public @interface MyAnnnotation {
}
```

* 注解本质上就是一个接口

  ```java
  public interface MyAnno extend java.lang.annotation.Anntation{}
  ```

* 可定义成员方法，返回值类型：**基本数据类型、String、枚举、注解、之前的类型的数组**

* 定义属性后，在使用时需要赋值或给一个**默认值**：default

  ```java
  public @interface MyAnnnotation {

      String name() default "张三";
  }
  ```

* 只有一个属性、且属性名为value，使用时可以省略名称

* 数组属性使用**{}**赋值（只有一个值可以省略**{}**）

### 元注解

> 用于描述注解的注解

1. @Target：描述注解能够作用的位置
2. @Retention：描述注解保留的截断
3. @Documented：描述注解是否被抽取到挨批文档
4. @Inherited：描述注解是否被子类继承

### 使用





# 函数式数据处理

## 引入流

出发点：

1. 不够好的集合API
2. 并行代码书写过于复杂、难以调试

特点：

1. 允许以声明式的方式处理数据集合（类似SQL查询语句，可以简单看作高级迭代器）
2. 透明的并行处理
3. 只能遍历一次
4. 通常为内部迭代，只需要传入筛选行为

JDK7与JDK8的同一种操作的代码实现：

JDK7：

```java
List<Dish> lowCaloricDishes = new ArrayList<>();
for(Dish d: menu){
    if(d.getCalories() < 400) {
        lowCaloricDishes.add(d);
    }
}
Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
    public int compare(Dish d1, Dish d2){
        return Integer.compare(d1.getCalories(), d2.getCalories());
    }
});
List<String> lowCaloricDishesName = new ArrayList<>();
for(Dish d: lowCaloricDishes){
    lowCaloricDishesName.add(d.getName());
}
```

JDK 8

```java
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

List<String> lowCaloricDishesName =  
    menu.stream()
    .filter(d -> d.getCalories() < 400)
    .sorted(comparing(Dish::getCalories))
 	.map(Dish::getName)
 	.collect(toList());
// 为了利用多核架构并行执行这段代码，你只需要把stream()换成parallelStream()：
List<String> lowCaloricDishesName =
    menu.parallelStream()
    .filter(d -> d.getCalories() < 400)
    .sorted(comparing(Dishes::getCalories))
    .map(Dish::getName)
 	.collect(toList());
```

* filter、sorted、map和collect等操作是与具体线程模型无关的高层次构件，它们的内部实现可以是单线程的，也可能透明地充分利用多核架构！
* 声明性——更简洁，更易读
*  可复合——更灵活
* 可并行——性能更好



流是什么？简单定义：从支持数据处理操作的源生成的元素序列。

* 元素序列：，流也提供了一个接口，可以访问特定元素类型的一组有序值。**集合讲的是数据，流讲的是计算。**
* 源：流会使用一个提供数据的源，如集合、数组或输入/输出资源。**从有序集合生成流时会保留原有的顺序。由列表生成的流，其元素顺序与列表一致。**
* 数据处理操作：流的数据处理功能支持类似于数据库的操作，流操作可以顺序执行，也可并行执行。
* 流水线：很多流操作本身会返回一个流，可以用于实现**延迟**和**短路**。流水线的操作可以看作对数据源进行数据库式查询。
* 内部迭代：与使用迭代器显式迭代的集合不同，流的迭代操作是在背后进行的。

***

**流操作**

* 中间操作：
  * 结果：另一个流
  * 特点：除非触发终端操作，否则中间操作不会执行任何处理
* 终端操作：
  * 结果：任何非流
  * 特点：消耗所有中间操作（流水线）

**使用流**

* 一个数据源（如集合）来执行一个查询；

* 一个中间操作链，形成一条流的流水线；

* 一个终端操作，执行流水线，并能生成结果。



## 使用流

内容

* 筛选、切片、匹配
* 查找、匹配、规约
* 使用数值范围等流
* 从多个元创造流



内部迭代优点:

* Stream API可以背后做出多种优化
* Stream API可以决定是否并行



### 筛选和切片

1. 用谓词（返回boolean的函数）筛选
2. <font color="purple">**distinct()**</font>：选择不同的元素
3. <font color="purple">limit(n)</font>：截断流，返回不超过给定长度的流
4. skip(n)：跳过流中前n个元素（与limit互补）



### 映射

类似SQL语句的选择，Stream可以使用map、flatMap选择某些信息



1. map：应用于流中的每一个元素

   ```java
   <R> Stream<R>	map(Function<? super T,? extends R> mapper);
   List<Integer> dishNameLengths = menu.stream()
    	.map(Dish::getName)
    	.map(String::length)
    	.collect(toList());
   ```

2. flatMap：把一个流中的每个值都换成另一个流，然后把所有的流连接起来成为一个流。

Demo：

```java
List<Integer> numbers1 = Arrays.asList(1, 2, 3);
List<Integer> numbers2 = Arrays.asList(3, 4);
List<int[]> pairs =
    numbers1.stream()
 	.flatMap(i ->
             numbers2.stream()
	 		.filter(j -> (i + j) % 3 == 0)
	 		.map(j -> new int[]{i, j})
    )  
    .collect(toList());
// 输出：[(2, 4), (3, 3)]。
```

## 查找和匹配

* <font color="red">``boolean	anyMatch(Predicate<? super T> predicate)`` </font>
  查找流中是否有元素能够匹配给定的谓词
* <font color="red">``boolean	allMatch(Predicate<? super T> predicate)`` </font>
  查找流中是否所有元素都匹配给定的谓词
* <font color="red">``boolean	noneMatch(Predicate<? super T> predicate)`` </font>
  查找流中是否所有元素都不匹配给定的谓词
* <font color="red">``Optional<T>	findAny()`` </font>
  返回当前流中的任意元素，找到后就立即结束
* <font color="red">``Optional<T>	findFirst()`` </font>
  查找第一个元素
> findAny的并行限制小于findFirst

**Optional简介**：
Optional<T>类（java.util.Optional）是一个容器类，代表一个值存在或不存在。
* isPresent()将在Optional包含值的时候返回true, 否则返回false。
* ifPresent(Consumer<T> block)会在值存在的时候执行给定的代码块。
* T get()会在值存在时返回值，否则抛出一个NoSuchElement异常。
* T orElse(T other)会在值存在时返回值，否则返回一个默认值。

## 规约
一些查询需要将流中的所有元素结合起来，得到一个值，如：Integer，这样的查询叫做规约。

<font color="red">T	reduce(T identity, BinaryOperator<T> accumulator)</font>
* identity: 初始值
* accumulator：关联、无干扰的无状态函数，用于组合两个值

Example:
1. 求和：``int sum = numbers.stream().reduce(0, Integer::sum);``
2. 最大值：``Optional<Integer> max = numbers.stream().reduce(Integer::max);``
3. 最小值：``Optional<Integer> max = numbers.stream().reduce(Integer::min);``

好处：内部迭代，使得Stream可以选择并行执行 reduce
并行执行的代价：传递给reduce的Lambda表达式不能改变状态 ，并且操作必须满足结合律。

**流操作**
无状态：例如filter、map等操作，都是从输入流获取一个元素，并在输出流输出0或1个元素，他们没有内部状态(假设用户传入的Lambda没有可变状态)
有状态：reduce、max、min需要内部状态积累结果，sort、distinct在排序、删除是也需要知道先前的状态


![image-20200709154456198](E:\Markdown\学习记录\image\streamApi.png)

## 数值流

```java
int calories = menu.stream()
  .map(Dish::getCalories) 
  .reduce(0, Integer::sum);
```
问题：存在隐含的装箱成本
解决：使用原始类型特化流：IntStream、LongStream、DoubleStream，分别将流中的元素特化为interesting、long、double，从而避免了装箱的成本
映射：
1. 转为数值流：mapToInt、mapToDouble、mapToLong
2. 转为对象流：boxed

**数值范围**
IntStream、LongStream
* range，不包含结束值
* rangeClosed：包含结束值

## 构建流

1. 由值构建
2. 由数组构建
3. 由文件生成
4. 由函数生成