
# 重要数据配置保护
```
配置文件: src/main/resources/secure.yml
中用于设置需要保护的数据, 比如: 数据的(地址, 账号, 密码 等),
具体用法可查看 src/main/resources/secure.yml.tpl 的注释
```


# 在 idea 中配置启动参数
```
本地测试:
在程序参数中设置: --spring.profiles.active=dev
```

# 设置java.library.path的值（Mac/Linux/Windows）
https://www.cnblogs.com/easonjim/p/9445282.html
```
export LB_LIBRARY_PATH=/root/lib
java -jar chatdata-0.0.1-SNAPSHOT.jar

java -Djava.library.path=/root/lib -jar target/chatdata-0.0.1-SNAPSHOT.jar

```


# 企业微信会话存档解密示例
https://blog.csdn.net/u011056339/article/details/105704995

# 企业微信会话内容存档开发期间遇到的一些坑
http://www.588zj.com/article/qywxDev


```bash
mvn clean package -Dmaven.test.skip=true 
```

# 上传jar文件到测试机器 并 启动
```bash
# ./cmd.sh restart
./cmd.sh stop
./cmd.sh status
./cmd.sh start  

```


# 测试地址

## 解析内容
```bash
curl -i -X POST -H "token: eb7706db-cde3-4804-af79-155e3dacf392" http://localhost:8082/api/wework/msg/parse
```

## 同步内容
```bash
curl -i -X POST -H "token: eb7706db-cde3-4804-af79-155e3dacf392" http://localhost:8082/api/wework/msg/sync
```

## 获取登录账号基本信息
```bash
curl -i -X GET -H "token: df102559-9321-4a51-8c3d-bf1c3c87dc27" http://localhost:8082/api/qc/wework/account/basic
```

## 登录

```bash
curl -i -X POST http://localhost:8082/api/login?mobile=123&password=123

curl -i -X POST -H "content-type: application/json" -d "{mobile: \"123\",password: \"123\"}" http://localhost:8082/api/login
```


## 联系人相关

- 获取员工

```bash
curl -i -X GET http://localhost:8082/api/contact/employee/1\?Token\=account-1234-token
```
