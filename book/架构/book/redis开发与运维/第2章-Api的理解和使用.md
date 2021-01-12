#字符串，哈希，列表，集合，有序集合
- Redis提供5中数据结构，每种数据结构都有多种内部编码实现
#Redis高性能因素
 - 纯内存存储、
 - IO多路复用、
 - 单线程架构
 
# 理解Redis单线程命令处理机制
- Redis使用了单线程架构和I/O多路复用模型来实现

# 高时间复杂度的命令 
- 字符串  keys, scan命令可以解决 keys命令带来的阻塞问题
- 哈希 hgetall , hscan
- 集合 smembers ,sscan
- 有序集合 zrange ,zscan



