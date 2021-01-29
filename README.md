# weblogic_hpcmd
weblogic t3 deserialization rce, support replying message from rmi sever. The original project fork from: [weblogic_cmd](https://github.com/5up3rc/weblogic_cmd), intend to keep updating

1. 利用文章见：[Weblogic T3 反序列化回显利用(CVE-2020-2555)](https://hpdoger.cn/2021/01/28/title:%20Weblogic%20T3%20%E5%8F%8D%E5%BA%8F%E5%88%97%E5%8C%96%E5%9B%9E%E6%98%BE%E5%88%A9%E7%94%A8(CVE-2020-2555)/)
2. 直接通过加载字节码的方式来加载class，执行无文件生成。通过绑定rmi来实现回显。
3. 支持t3s
4. 原版本利用链为CVE-2015-4852
5. 支持StreamMessageImpl,MarshalledObject绕过


# Update
1、增加CVE-2020-2555的利用

# 使用说明
  -H 远程目标主机
  -P 远程目标端口
  -C 需要执行的命令
  -T 可选的绕过方式
  -U 删除绑定的rmi实例
  -B 通过payload直接调用系统命令－针对没法回显的情况下使用
  -os 指定目标操作系统
  -https 使用tls的指定
  -shell 以shell的方式展现
  -upload 上传文件 需要配合-src -dst
  -src 需要上传的文件路径
  -dst 需要上传文件至目标的路径
  -noExecPath 在某些没有/bin/bash 或者cmd.exe情况下使用
  -C2555 使用CVE-2020-2555的gadget攻击
