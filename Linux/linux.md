# 遇见的问题及解决办法

1. VMware 中CentOS7安装后无Ip地址
   1. 虚拟机网络修改为**NAT模式**
   2. 修改 ``/etc/sysconfig/neywork-scrpits/ifcfg-en33`` 中 ONBOOT属性值为true
   3. 重启服务：``service network restart``
   4. ifconfig查看，已经恢复正常

2. 使用 exec 22<> /etc/tcp/www.baidu.com/80命令时提示目录不存在
   1. 查看发现不存在/etc/tcp目录
   2. 目录修改为``**/dev/tcp**``

1. cd /proc/$$/fd
2. lsof -p $$

# 知识点

## exec连接百度

1. exec 8<> /dev/tcp/www.baidu.com/80
   * 打开一个socket连接，文件描述符为8
   * 查看开启的文件描述符：``cd /proc/$$/fd   ll``
   * 关闭文件描述符：
     * exec 8<& -
   * 命令由内核完成
2. echo -e "GET / HTTP/1.0\n" 1>& 8
   * ``"GET / HTTP/1.0\n"``为请求头，必须按照格式书写
3. cat 0<& 8