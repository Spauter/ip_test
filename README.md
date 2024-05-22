### 需要准备的

Nacos、JDK11、Maven、Redis、Mysql 8.0+配置存在并已经启动

### 准备步骤

1.使用Mysql工具导入配置文件里的sql脚本

2.登录Nacos,创建命名空间IP_test,ID为“089be718-0452-4597-a46c-ac69580396c4”。导入配置文件中的压缩文件，

3.修改mysql8.yml,redis_local.yml配置文件里面地址和端口号

4.在项目中修改所有resources文件夹里的bootstrap.yml中所有nacos地址

```yaml
spring:
  cloud:
    nacos:
      config:
        server-addr: http://192.168.24.128:8848
      discovery:
        server-addr:  http://192.168.24.128:8848
```
5.修改ip_test_index中resources/static/index.html下列地址为ip_test_intercept启动后地址

```html
<div><a href="http://192.168.0.9:11420/doc.html">接口测试文档地址</a> </div>
```
6.修改ip_test_index/resources/static/index.js的请求的地址为ip_test_intercept启动后地址

```js
    axios.get('http://192.168.0.9:11420/intercept/current_count', {
        headers: {
            'diy_name': 'Bloduc Spauter'
        }
    })
//省略其它代码
```

~~把前端文件放在ip_test_index的资源文件夹中static下~~
~~网关和负载均衡看情况~~