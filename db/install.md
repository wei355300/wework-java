- 安装数据库
` yum install -y mysql-server`

- 设置账号
默认账号: `root@localhost`, 密码为空
```
-- 将密码调整为 123456
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';

```

- 初始化

```

```

- 操作指令

启动数据库
```
systemctl start  mysqld.service
```

查看状态
```
systemctl status  mysqld.service
```


