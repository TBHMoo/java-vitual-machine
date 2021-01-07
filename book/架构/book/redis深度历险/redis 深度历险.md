redis 深度历险

【缓存】  
https://coolshell.cn/articles/17416.html

错误的套路  （先删除缓存，然后再更新数据库）
remove cache
update db
push cache 

两个并发操作，一个更新，一个查询。  更新操作删除缓存后，查询操作没有命中缓存，先把老数据读出来放到了缓存中，然后更新操作更新了数据库
w remove cache
   r  从cache去读数据，没有命中缓存，把老数据读取出来放到了缓存中
  update db
  push cache 

缓存更新的套路
Design Pattern （Cache Aside Pattern、  Read/Write Through Pattern ， Write Behind Caching Pattern）
Cache Aside Pattern （这是最常用最常用的pattern了）
失效： 
	a、 应用程序先从cache 获取数据，没有得到
	b、 从数据库中取数据，成功后
	c、 将数据放入缓存

命中： 
    a、 应用程序从cache 取数据，取到后返回

更新：
	a、 先把数据更新到数据库中，成功后，再让缓存失效

Q: 为什么不是写完数据库后更新缓存？ 
A: 主要是怕两个并发的写操作，导致脏数据

Cache Aside 这个就没有并发问题了吗？ 不是的，
比如 
一个读操作 
失效：                             
	 a、应用程序先从cache 获取数据，没有得到
	 b、从数据库中取数据，成功后
	 		一个写操作 更新：a、先把数据更新到数据库中，成功后，再让缓存失效
	 c、将数据放入cache 

这个情况会造成脏数据，
但，这个case理论上会出现，不过，实际上出现的概率会非常低，
因为这个条件需要发生在读缓存时失效，而且并发着一个写操作，写操作先于读操作完成。
而实际上数据库的写操作比读操作慢很多，而且还要锁表，而读操作必须在写操作前进入数据库，
而又要晚于写操作更新缓存，所以这些条件都具备的概率不大。


Read/Write Through Pattern（缓存和DB间的数据同步，交由缓存来处理）
我们可以看到，在上面的Cache Aside套路中，我们的应用代码需要维护两个数据存储，一个是缓存（Cache），一个是数据库（Repository）
而Read/Write Through 的套路是把更新 数据库的操作由缓存自己代理了

Read Through 套路就是在查询操作中更新缓存，也就是说，当缓存失效的时候（过期或者LRU换出），CacheAside
是由应用程序负责把数据加载入缓存，而Read Through则用缓存服务自己来加载，从而对应用方式透明的。

Write Through 当有数据更新时，如果命中缓存，则更新缓存，然后	由Cache自己更新数据库（这是一个同步操作）。
如果没有命中缓存，则直接更新数据库，然后返回。

Write Behind Caching Pattern 
Write Back套路，一句说就是，在更新数据的时候，只更新缓存，不更新数据库，而我们的缓存会异步地批量更新数据库。这个设计的好处就是让数据的I/O操作飞快无比（因为直接操作内存嘛 ），因为异步，write backg还可以合并对同一个数据的多次操作，所以性能的提高是相当可观的。

但是，其带来的问题是，数据不是强一致性的，而且可能会丢失（我们知道Unix/Linux非正常关机会导致数据丢失，就是因为这个事）。在软件设计上，我们基本上不可能做出一个没有缺陷的设计，就像算法设计中的时间换空间，空间换时间一个道理，有时候，强一致性和高性能，高可用和高性性是有冲突的。软件设计从来都是取舍Trade-Off

以上，我们没有考虑缓存（Cache）和持久层（Repository）的整体事务的问题。
https://coolshell.cn/articles/10910.html


Redis有哪些数据结构？
String  list  hash  set  sortedSet   
stream 借鉴了kafka的设计

使用过Redis分布式锁么，它是什么回事？ （原子指令，不会被线程切换上下文打断的执行的操作）
抢凳子游戏，10个人，抢一个凳子
一般用 setnx指令，占位，然后del 删除，但是可能出于某种原因，del语句未顺利执行，这个时候锁就永远不会被释放
所以，使用expire 在锁上增加过期时间，即   
setnx
expire
但是这个时候，expire还是可能不会顺利执行。 因为 setnx 和 expire 是两条指令，而不是原子指令。
redis 2.8之后，作者提供了 set 指令， 可以实现 setnx 和 expire 的原子操作
 > set lock_codehole true ex 5 nx 
 OK 
 do something critical ... 
 > del lock_codehole


假如Redis里面有1亿个key，其中有10w个key是以某个固定的已知的前缀开头的，如果将它们全部找出来？
keys *
1、正则表达式 O(N)  没有 offset limit  
2、就会导致 Redis 服务卡顿

scan （高位进位加法遍历  槽位游标） （类似 mysql limit offset 分页，每次请求带最小id）  
1、复杂度虽然也是 O(n)，但是它是通过游标分步进行的，不会阻塞线程
2、返回的结果可能会有重复，需要客户端去重复，这点非常重要
3、单次返回的结果是空的并不意味着遍历结束，而要看返回的游标值是否为零; 
4、遍历的过程中如果有数据修改，改动后的数据能不能遍历到是不确定的; 

字典结构，扩容 缩容
字典  一维数组  + 二维链表 的结构


使用过Redis做异步队列么，你是怎么用的？
rpush,lpop,  lpush,rpop , blockpop.

如果有大量的key需要设置同一时间过期，一般需要注意什么？


Redis如何做持久化的？


Pipeline有什么好处，为什么要用pipeline？
一次发送多个命令，节省往返时间。

事务通常和pipeline一起使用，redis事务和关系型数据库事务的区别？




Redis的同步机制了解么？
俄罗斯套娃现象，  环状buffer设置过小  大的话几十M就够了，一般几M
增量同步，
aof做增量持久化。, SDS还被用冲区（buffer)

全量同步(快照 rdb)
bgsave做镜像全量持久化， fork ,cow. + 增量AOF 

是否使用过Redis集群，集群的原理是什么？
redis sentinel 关注高可用，使用zookeeper 代替运维人员实时关注集群状态，做主从切换。
redis cluster  当单节点内存在高峰期不足时，使用cluster分片存储。



【分布式锁】

雪花算法  生成分布式唯一的流水号


https://github.com/jackfrued/Python-100-Days


