<!-- TOC -->

- [Vue](#vue)
  - [数据与属性](#数据与属性)
  - [生命周期钩子](#生命周期钩子)
- [模板语法](#模板语法)
  - [插值](#插值)
  - [指令](#指令)
- [计算属性和侦听器](#计算属性和侦听器)
  - [计算属性](#计算属性)
  - [侦听器](#侦听器)
- [Class与Style绑定](#class与style绑定)
  - [绑定HTML Class](#绑定html-class)
- [组件基础](#组件基础)
  - [通过prop向子组件传递数据](#通过prop向子组件传递数据)

<!-- /TOC -->

# Vue

Vue是一个渐进式的框架
* 可以作为应用的一部分嵌入，意思是你可以逐渐的重构原来未使用Vue的项目

* 解耦数据和视图
* 可复用的组件
* 前端路由技术
* 状态管理
* 虚拟DOM

## 数据与属性
* 在Vue实例的data属性中添加对象，其中的属性可以与DOM节点双向绑定
* 在DOM标签中使用 ``v-bind``也可以双向绑定
* 再HTML标签中使用``{{msg}}``方式匹配属性
* 不可以双向绑定的情况：
  1. 在Vue实例创建后再添加属性
  2. 使用``Object.freeze()``
* 属性书写格式：按照JSON格式书写
```js
// 我们的数据对象
var data = { a: 1 }
```
* 其他属性一些属性以 $开头（以区分用户定义的属性）
```js
var data = { a: 1 }
var vm = new Vue({
  el: '#example',
  data: data
})       
vm.$data === data // => true
vm.$el === document.getElementById('example') // =>true     
// $watch 是一个实例方法
vm.$watch('a', function (newValue, oldValue) {
  // 这个回调将在 `vm.a` 改变后调用
})
```

## 生命周期钩子

生命周期图示:
![vueLifeCycle](image/Vuelifecycle.png)

使用：
```js
new Vue({
  data: {
    a: 1
  },
  created: function () {
    // `this` 指向 vm 实例
    console.log('a is: ' + this.a)
  }
})
```
> 不要再回调中使用箭头函数：``created: () => console.log(this.a) ``，因为箭头函数中没有this，`this` 会作为变量一直向上级词法作用域查找，直至找到为止，经常导致 `Uncaught TypeError: Cannot read property of undefined` 或 `Uncaught TypeError: this.myMethod is not a function` 之类的错误。

# 模板语法

## 插值
1. Mustache语法：``<p>{{message}}</p>``，双大括号将数据解释为**普通文本**
2. `v-html`：输出`HTML`代码(忽略其中的数据绑定)
   * 动态渲染的任意 `HTML` 可能会非常危险，因为它很容易导致 `XSS` 攻击。请只对可信内容使用 `HTML` 插值，绝不要对用户提供的内容使用插值。
3. 动态属性Attribute
   * `<div v-bind:id="dynamicId"></div>`
4. JavaScript表达式
  ```js
  {{ number + 1 }}
  {{ ok ? 'YES' : 'NO' }}
  {{ message.split('').reverse().join('') }}
  <div v-bind:id="'list-' + id"></div>
  ```
## 指令

1. `v-if`：值为`false`时会移除标签
2. `v-bind`
3. `v-on:click="doSomething"`
4. 动态参数
   1. `<a v-bind:[attributeName]="url"> ... </a>`
   2. `<a v-on:[eventName]="doSomething"> ... </a>`
   3. 不能使用带有**空格和引号**的表达式，否则会触发警告，如：`<a v-bind:['foo' + bar]="value"> ... </a>`
   4. 使用模板时，需要避免使用大写字符命名键名，因为浏览器会全部强制转换为小写。
5. 修饰符
   1. `<form v-on:submit.prevent="onSubmit">...</form>`

6. `v-bind`缩写
   1. `<a :href="url">...</a>`
   2. `<a :[key]="url"> ... </a>`

7. `v-on`缩写
   1. `<a @click="doSomething">...</a>`
   2. `<a @[event]="doSomething"> ... </a>`

# 计算属性和侦听器

## 计算属性
```HTMl
<div id="example">
  <p>Original message: "{{ message }}"</p>
  <p>Computed reversed message: "{{ reversedMessage }}"</p>
</div>
```

```js
var vm = new Vue({
  el: '#example',
  data: {
    message: 'Hello'
  },
  computed: {
    // 计算属性的 getter
    reversedMessage: function () {
      // `this` 指向 vm 实例
      return this.message.split('').reverse().join('')
    }
  }
})
```
* `reversedMessage`始终和`message`值有关
* 可以将`reversedMessage`看成`getter`方法
* 与直接使用 `method` 的唯一不同在于，`computed`中的方法会进行缓存，并且只有在线管的响应式依赖进行改变时才进行重新的计算

**代替侦听属性`watch`使用**
```js
  computed: {
    fullName: function () {
      return this.firstName + ' ' + this.lastName
    }
  }
  // 而不是
  watch: {
    firstName: function (val) {
      this.fullName = val + ' ' + this.lastName
    },
    lastName: function (val) {
      this.fullName = this.firstName + ' ' + val
    }
  }
```
**计算属性的setter**
```js
// ...
computed: {
  fullName: {
    // getter
    get: function () {
      return this.firstName + ' ' + this.lastName
    },
    // setter
    set: function (newValue) {
      var names = newValue.split(' ')
      this.firstName = names[0]
      this.lastName = names[names.length - 1]
    }
  }
}
// ...
```
* 此后，更新 `fullname` 值时会调用 `setter` 方法，更新 `firstname` 和 `lastname`

## 侦听器

实例:
```html
<div id="watch-example">
  <p>
    Ask a yes/no question:
    <input v-model="question">
  </p>
  <p>{{ answer }}</p>
</div>
```
```js
let watchExample = new Vue({
        el: '#watch-example',
        data: {
            question: '',
            answer: `I cannot give you an answer until you ask a question!`
        },
        watch: {
            question: function (newValue, oldValue) {
                this.answer = 'Waiting for you to stop typing...';
                this.debouncedGetAnswer();
            }
        },
        created: function () {
            this.debouncedGetAnswer = _.debounce(this.getAnswer, 500)
        },
        methods: {
            getAnswer: function () {
                if (this.question.indexOf('?') === -1) {
                    this.answer = 'Questions usually contain a question mark. ;-)';
                    return;
                }
                this.answer = 'Thinking...';
                let vm = this;
                $.ajax({
                    url: 'https://yesno.wtf/api',
                    success: function (result) {
                        console.log(result);
                        vm.answer = result.answer;
                    },
                    error: function (error) {
                        vm.answer = 'Error! Could not reach the API. ' + error;
                    }
                })
            }
        }
    });
```
* `_.debounce(func, [wait=0], [options={}])`
  * 用于防止抖动，只有在最后一次访问 `func` 后指定延时才能真正调用
  * 区别于`_.throttle(func, [wait=0], [options={}])`，此函数的意义时再指定时间内只能调用一次，主要区别在于此函数是立即触发

# Class与Style绑定

## 绑定HTML Class

1. `<div v-bind:class="{ active: isActive }"></div>`
   * class active的存在与否与 `isActive`相关
   * 同时使用多个：`v-bind:class="{ active: isActive, 'text-danger': hasError }"`
2. 值可以是对象值
3. 值可以是 `computed` 属性中的函数

# 组件基础

1. 组件是一个可复用的 `Vue` 实例，因此可以接收 `new Vue` 相同的选项，除 el
2. 作为 **自定义元素** 在通过 `new Vue` 创建的根实例中使用。

**Demo**
1. 定义组件
```js
// 定义一个名为 button-counter 的新组件
Vue.component('button-counter', {
  data: function () {
    return {
      count: 0
    }
  },
  template: '<button v-on:click="count++">You clicked me {{ count }} times.</button>'
})
```

2. 使用
```html
<div id="components-demo">
  <button-counter></button-counter>
</div>
<script>
new Vue({ el: '#components-demo' })
</script>
```

**特点**
1. 任意次数复用（直接看成新的HTML中的标签）
2. `data` 必须是一个函数 `function`，因此每个实例都可以维护一份被返回对象的独立的拷贝

## 通过prop向子组件传递数据

1. title
```js
Vue.component('blog-post', {
  props: ['title'],
  template: '<h3>{{ title }}</h3>'
})
```
