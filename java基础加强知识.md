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

