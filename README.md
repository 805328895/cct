# cct
基于 spring 的分布式事物

事物中心  server 服务 先启动


启动类上面加上注解 @EnableCct  开启注解
方法上加上 @CctTransactional 开启分布式事物
对代码无入侵
