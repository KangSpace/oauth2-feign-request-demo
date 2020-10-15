# Spring Cloud oAuth2 Feign Client Requests Demo

### 介绍(Introduction)   
该项目用于展示如何在SpringCloud项目中使用Feign来调用oAuth2认证的接口。  

### 项目实现功能：  
* 简单的oAuth2 SpringSecurity认证([spring-security-architecture](https://spring.io/guides/topicals/spring-security-architecture)
  ,[securing-web](https://spring.io/guides/gs/securing-web/))
  (SpringSecurity认证流程图: https://img-blog.csdnimg.cn/20181202095539982.png)
* 使用FeignClient获取oAuth2 Basic认证凭据(即token),并缓存维护
> 即进行oAuth2 Basic认证 
* 使用FeignClient使用token请求已知服务接口
> 即使用AccessToken请求服务接口
* 使用Redis作为分布式锁(RedLock)及数据缓存中间件
> 全局缓存AccessToken

### 流程图

### 项目说明
为了简单说明Feign来调用oAuth2认证的接口(并缓存access_toek),所以在项目中实现了简单的认证服务,即任务服务，接口服务(被调接口)都
在本项目中实现
* [test.http](test.http) 接口测试文件

### TODO List  
* [x] 基础功能开发
* [ ] 测试
* [ ] Redis分布式锁使用redission redLock
* [ ] 流程图
* [ ] 项目说明
* [ ] 优化