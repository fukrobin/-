# Spring MVC 简介



MVC：

* Model：模型，JavaBean，用于三层之数据的传递。
* View：视图，通常为Jsp页面，用于向用户展示页面
* Controller：控制器，通常为各个Servlet，用于分析、处理并转发请求到业务层

Spring MVC的简要介绍：

SpringMVC 是一种基于 Java 的实现 MVC 设计模型的请求驱动类型的轻量级 Web 框架，属于 Spring FrameWork 的后续产品，已经融合在 Spring Web Flow 里面。Spring 框架提供了构建 Web 应用程序的全功能 MVC 模块。使用 Spring 可插入的 MVC 架构，从而在使用 Spring 进行 WEB 开发时，可以选择使用 Spring的 Spring MVC 框架或集成其他 MVC 开发框架，如 Struts1(现在一般不用)，Struts2 等。

SpringMVC 已经成为目前最主流的 MVC 框架之一，并且随着 Spring3.0 的发布，全面超越 Struts2，成为最优秀的 MVC 框架。它通过一套注解，让一个简单的 Java 类成为处理请求的控制器，而无须实现任何接口。同时它还支持RESTful 编程风格的请求。

# Spring MVC 入门程序

1. 搭建 maven项目，选择原型:``webapps``

2. 配置web.xml：配置spring mvc核心控制器（servlet）的映射：

   ```xml
   <!-- spring核心控制器配置 -->
     <servlet>
       <servlet-name>dispatcherServlet</servlet-name>
       <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
     </servlet>
     
     <servlet-mapping>
       <servlet-name>dispatcherServlet</servlet-name>
       <url-pattern>/</url-pattern>
     </servlet-mapping>
   ```

3. 配置spring mvc配置文件

4. 为控制器添加注解：@Controller，是@Component的特化

5. 映射前端请求到具体控制器的方法

   ```java
   @RequestMapping("/hello")
   public void requestMap() {
       System.out.println("Hello Spring MVC!");
   }
   ```

   此后访问 ``/hello``都会执行此方法

6. 配置web.xml使Tomcat启动时加载 Spring MVC的配置文件

   ```xml
   <servlet>
     <servlet-name>dispatcherServlet</servlet-name>
     <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
     <init-param>
       <param-name>contextConfigLocation</param-name>
       <param-value>classpath:springmvc.xml</param-value>
     </init-param>
     <load-on-startup>1</load-on-startup>
   </servlet>
   ```

7. 在Spring配置文件中配置 **视图解析对象**

   ```xml
   <!--  视图解析器对象  -->
   <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
       <!--  前缀，在此配置目录下寻找返回页面 -->
       <property name="prefix" value="/WEB-INF/pages/"/>
       <!-- 后缀，返回页面文件后缀 -->
       <property name="suffix" value=".jsp"/>
   </bean>
   ```

8. 在Spring配置文件中配置 Spring MVC的注解配置支持

   ```xml
   <!--开启spring mvc注解配置支持-->
   <mvc:annotation-driven/>
   ```

9. 编写index.jsp，测试访问 hello

   ```html
   <a href="hello">Hello Spring MVC</a>
   ```

10. 执行成功



疑问：为何可以访问到 ``WEB-InF``目录下的jsp文件，以我之前学习javaweb结论告诉我，``WEB-INF``目录是不能访问的（或者说不能随意访问），因为权限不够，单位和 Spring MVC能访问呢？