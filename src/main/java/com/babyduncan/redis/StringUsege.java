package com.babyduncan.redis;

/**
 * User: zgh
 * Date: 12-12-29
 * Time: 14:13
 */

import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * 对String类型的操作
 */
public class StringUsege {
    Jedis jedis = new Jedis("localhost", 6379);

    //String 的set操作

    /**
     * Available since 1.0.0.
     * Time complexity: O(1)
     * Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type.
     * Return value
     * Status code reply: always OK since SET can't fail.
     * Examples
     * redis> SET mykey "Hello"
     * OK
     * redis> GET mykey
     * "Hello"
     * redis>
     */
    @Test
    public void string_set() {
        jedis.set("foo", "bar");
        System.out.println(jedis.get("foo"));
        Assert.assertEquals("bar", jedis.get("foo"));
        jedis.set("foo", "new_bar");
        System.out.println(jedis.get("foo"));
        Assert.assertEquals("new_bar", jedis.get("foo"));
    }

    //String的setnx 操作 ,如果存在key，则无作为，如果不存在key，则set key value。

    /**
     * Available since 1.0.0.
     * Time complexity: O(1)
     * Set key to hold string value if key does not exist. In that case, it is equal to SET. When key already holds a value, no operation is performed. SETNX is short for "SET if N ot e X ists".
     * Return value
     * Integer reply, specifically:
     * 1 if the key was set
     * 0 if the key was not set
     * <p/>
     * if you want to know the design pattern you will go to "http://redis.io/commands/setnx"
     */
    @Test
    public void string_setnx() {
        jedis.set("foo1", "bar1");
        System.out.println(jedis.get("foo1"));
        jedis.setnx("foo1", "new_bar1");
        System.out.println(jedis.get("foo1"));
        Assert.assertEquals("bar1", jedis.get("foo1"));
        jedis.setnx("foosetnx", "barsetnx");
        System.out.println(jedis.get("foosetnx"));
        Assert.assertEquals("barsetnx", jedis.get("foosetnx"));
    }

    //String的setex操作，可以设置一个key的过期时间。

    /**
     * Available since 2.0.0.
     * Time complexity: O(1)
     * Set key to hold the string value and set key to timeout after a given number of seconds. This command is equivalent to executing the following commands:
     * SET mykey value
     * EXPIRE mykey seconds
     * SETEX is atomic, and can be reproduced by using the previous two commands inside an MULTI / EXEC block. It is provided as a faster alternative to the given sequence of operations, because this operation is very common when Redis is used as a cache.
     * An error is returned when seconds is invalid.
     * Return value
     * Status code reply
     * Examples
     * redis> SETEX mykey 10 "Hello"
     * OK
     * redis> TTL mykey
     * (integer) 10
     * redis> GET mykey
     * "Hello"
     * setex means "set expire" ,you can never think that is "set exists"
     *
     * @throws InterruptedException
     */
    @Test
    public void string_setex() throws InterruptedException {
        jedis.setex("babyduncan", 3, "guohaozhao");
        System.out.println(jedis.get("babyduncan"));
        Thread.sleep(10000);
        System.out.println(jedis.get("babyduncan"));
        Assert.assertNull(jedis.get("babyduncan"));
    }

    //String 的setrange操作，用于替换字符串的一部分

    /**
     * Available since 2.2.0.
     * Time complexity: O(1), not counting the time taken to copy the new string in place. Usually, this string is very small so the amortized complexity is O(1). Otherwise, complexity is O(M) with M being the length of the value argument.
     * Return value
     * Integer reply: the length of the string after it was modified by the command.
     * Examples
     * Basic usage:
     * redis> SET key1 "Hello World"
     * OK
     * redis> SETRANGE key1 6 "Redis"
     * (integer) 11
     * redis> GET key1
     * "Hello Redis"
     * Example of zero padding:
     * redis> SETRANGE key2 6 "Redis"
     * (integer) 11
     * redis> GET key2
     * "\u0000\u0000\u0000\u0000\u0000\u0000Redis"
     * redis>    数值过大，则如果空位补0
     */
    @Test
    public void string_setrange() {
        jedis.set("myemail", "BabyDuncan@qq.com");
        jedis.setrange("myemail", 11, "sina.com");
        System.out.println(jedis.get("myemail"));
        Assert.assertEquals(jedis.get("myemail"), "BabyDuncan@sina.com");
    }

    //redis的mset操作

    /**
     * Available since 1.0.1.
     * Time complexity: O(N) where N is the number of keys to set.
     * Sets the given keys to their respective values. MSET replaces existing values with new values, just as regular SET. See MSETNX if you don't want to overwrite existing values.
     * MSET is atomic, so all given keys are set at once. It is not possible for clients to see that some of the keys were updated while others are unchanged.
     * Return value
     * Status code reply: always OK since MSET can't fail.
     * Examples
     * redis> MSET key1 "Hello" key2 "World"
     * OK
     * redis> GET key1
     * "Hello"
     * redis> GET key2
     * "World"
     * redis>
     */
    @Test
    public void string_mset() {
        jedis.mset("msetk1", "msetv1", "msetk2", "msetv2");
        System.out.println(jedis.get("msetk1"));
        System.out.println(jedis.get("msetk2"));
        Assert.assertEquals("msetv1", jedis.get("msetk1"));
        Assert.assertEquals("msetv2", jedis.get("msetk2"));

    }

    //String 的msetnx操作

    /**
     * Available since 1.0.1.
     * Time complexity: O(N) where N is the number of keys to set.
     * Sets the given keys to their respective values. MSETNX will not perform any operation at all even if just a single key already exists.
     * Because of this semantic MSETNX can be used in order to set different keys representing different fields of an unique logic object in a way that ensures that either all the fields or none at all are set.
     * MSETNX is atomic, so all given keys are set at once. It is not possible for clients to see that some of the keys were updated while others are unchanged.
     * Return value
     * Integer reply, specifically:
     * 1 if the all the keys were set.
     * 0 if no key was set (at least one key already existed).
     * Examples
     * redis> MSETNX key1 "Hello" key2 "there"
     * (integer) 1
     * redis> MSETNX key2 "there" key3 "world"
     * (integer) 0
     * redis> MGET key1 key2 key3
     * 1) "Hello"
     * 2) "there"
     * 3) (nil)
     * redis>
     * 或者全都设置成功，或者没有一个设置成功，所有的操作全部回滚。
     */
    @Test
    public void string_msetnx() {
        jedis.msetnx("msetnxk1", "msetnxv1", "msetnxk2", "msetnxv2");
        System.out.println(jedis.get("msetnxk1"));
        System.out.println(jedis.get("msetnxk2"));
        Assert.assertEquals("msetnxv1", jedis.get("msetnxk1"));
        Assert.assertEquals("msetnxv2", jedis.get("msetnxk2"));
        //如果有一个设置没成功，那么这条命令的其他肯定也不成功。
        jedis.msetnx("msetnxk1", "new_msetnxv1", "msetnxk2", "new_msetnxv2", "msetnxk3", "new_msetnxv3");
        System.out.println(jedis.get("msetnxk1"));
        System.out.println(jedis.get("msetnxk2"));
        System.out.println(jedis.get("msetnxk3"));
        Assert.assertEquals("msetnxv1", jedis.get("msetnxk1"));
        Assert.assertEquals("msetnxv2", jedis.get("msetnxk2"));
        Assert.assertNull(jedis.get("msetnxk3"));
    }

    //String的get 命令

    /**
     * Available since 1.0.0.
     * Time complexity: O(1)
     * Get the value of key. If the key does not exist the special value nil is returned. An error is returned if the value stored at key is not a string, because GET only handles string values.
     * Return value
     * Bulk reply: the value of key, or nil when key does not exist.
     * Examples
     * redis> GET nonexisting
     * (nil)
     * redis> SET mykey "Hello"
     * OK
     * redis> GET mykey
     * "Hello"
     * redis>
     */
    @Test
    public void string_get() {
        Assert.assertNull(jedis.get("notexist"));
        jedis.set("foo", "bar");
        Assert.assertEquals("bar", jedis.get("foo"));
    }

    //String 的getset命令

    /**
     * Available since 1.0.0.
     * Time complexity: O(1)
     * Atomically sets key to value and returns the old value stored at key. Returns an error when key exists but does not hold a string value.
     * Design pattern
     * GETSET can be used together with INCR for counting with atomic reset. For example: a process may call INCR against the key mycounter every time some event occurs, but from time to time we need to get the value of the counter and reset it to zero atomically. This can be done using GETSET mycounter "0":
     * redis> INCR mycounter
     * (integer) 1
     * redis> GETSET mycounter "0"
     * "1"
     * redis> GET mycounter
     * "0"
     * redis>
     * Return value
     * Bulk reply: the old value stored at key, or nil when key did not exist.
     * Examples
     * redis> SET mykey "Hello"
     * OK
     * redis> GETSET mykey "World"
     * "Hello"
     * redis> GET mykey
     * "World"
     * redis>
     * 先get再set
     */
    @Test
    public void string_getset() {
        System.out.println(jedis.getSet("getset", "getset"));
        System.out.println(jedis.get("getset"));
    }

    //String类型的getrange操作

    /**
     * Available since 2.4.0.
     * Time complexity: O(N) where N is the length of the returned string. The complexity is ultimately determined by the returned length, but because creating a substring from an existing string is very cheap, it can be considered O(1) for small strings.
     * Warning: this command was renamed to GETRANGE, it is called SUBSTR in Redis versions <= 2.0.
     * Returns the substring of the string value stored at key, determined by the offsets start and end (both are inclusive). Negative offsets can be used in order to provide an offset starting from the end of the string. So -1 means the last character, -2 the penultimate and so forth.
     * The function handles out of range requests by limiting the resulting range to the actual length of the string.
     * Return value
     * Bulk reply
     * Examples
     * redis> SET mykey "This is a string"
     * OK
     * redis> GETRANGE mykey 0 3
     * "This"
     * redis> GETRANGE mykey -3 -1
     * "ing"
     * redis> GETRANGE mykey 0 -1
     * "This is a string"
     * redis> GETRANGE mykey 10 100
     * "string"
     * redis>
     */
    @Test
    public void string_getrange() {
        jedis.set("hello", "hell myfriends,I love you !");
        System.out.println(jedis.get("hello"));
        System.out.println(jedis.getrange("hello", 0, 16) + "fuck you !");
    }

    //String的mget操作

    /**
     * Available since 1.0.0.
     * Time complexity: O(N) where N is the number of keys to retrieve.
     * Returns the values of all specified keys. For every key that does not hold a string value or does not exist, the special value nil is returned. Because of this, the operation never fails.
     * Return value
     * Multi-bulk reply: list of values at the specified keys.
     * Examples
     * redis> SET key1 "Hello"
     * OK
     * redis> SET key2 "World"
     * OK
     * redis> MGET key1 key2 nonexisting
     * 1) "Hello"
     * 2) "World"
     * 3) (nil)
     * redis>
     */
    @Test
    public void string_mget() {
        List<String> l = jedis.mget("foo", "hello", "notakey");
        for (String s : l) {
            System.out.println(s);
        }
    }

    //String的incr操作

    /**
     * Available since 1.0.0.
     * Time complexity: O(1)
     * Increments the number stored at key by one. If the key does not exist, it is set to 0 before performing the operation. An error is returned if the key contains a value of the wrong type or contains a string that can not be represented as integer. This operation is limited to 64 bit signed integers.
     * Note: this is a string operation because Redis does not have a dedicated integer type. The string stored at the key is interpreted as a base-10 64 bit signed integer to execute the operation.
     * Redis stores integers in their integer representation, so for string values that actually hold an integer, there is no overhead for storing the string representation of the integer.
     * Return value
     * Integer reply: the value of key after the increment
     * Examples
     * redis> SET mykey "10"
     * OK
     * redis> INCR mykey
     * (integer) 11
     * redis> GET mykey
     * "11"
     * redis>
     */
    @Test
    public void string_incr() {
        jedis.set("mynum", "21");
        jedis.incr("mynum");
        Assert.assertEquals("22", jedis.get("mynum"));
        System.out.println(jedis.incr("what2"));
        System.out.println(jedis.get("what2"));
    }

    //string操作的incrby命令

    /**
     * Increments the number stored at key by increment. If the key does not exist, it is set to 0 before performing the operation. An error is returned if the key contains a value of the wrong type or contains a string that can not be represented as integer. This operation is limited to 64 bit signed integers.
     * See INCR for extra information on increment/decrement operations.
     * Return value
     * Integer reply: the value of key after the increment
     * Examples
     * redis> SET mykey "10"
     * OK
     * redis> INCRBY mykey 5
     * (integer) 15
     * redis>
     */
    @Test
    public void string_incrby() {
        int i = Integer.parseInt(jedis.get("num"));
        System.out.println(i);
        int j = jedis.incrBy("num", 20).intValue();
        Assert.assertEquals(j, i + 20);
    }

    //string 的decr 操    作

    /**
     * Available since 1.0.0.
     * Time complexity: O(1)
     * Decrements the number stored at key by one. If the key does not exist, it is set to 0 before performing the operation. An error is returned if the key contains a value of the wrong type or contains a string that can not be represented as integer. This operation is limited to 64 bit signed integers.
     * See INCR for extra information on increment/decrement operations.
     * Return value
     * Integer reply: the value of key after the decrement
     * Examples
     * redis> SET mykey "10"
     * OK
     * redis> DECR mykey
     * (integer) 9
     * redis> SET mykey "234293482390480948029348230948"
     * OK
     * redis> DECR mykey
     * ERR value is not an integer or out of range
     * redis>
     */
    @Test
    public void string_decr() {
        jedis.set("n", "10");
        jedis.decr("n");
        Assert.assertEquals(jedis.get("n"), "9");
        System.out.println(jedis.decr("notex"));
    }

    //string 的 decrby 操作

    /**
     * Available since 1.0.0.
     * Time complexity: O(1)
     * Decrements the number stored at key by decrement. If the key does not exist, it is set to 0 before performing the operation. An error is returned if the key contains a value of the wrong type or contains a string that can not be represented as integer. This operation is limited to 64 bit signed integers.
     * See INCR for extra information on increment/decrement operations.
     * Return value
     * Integer reply: the value of key after the decrement
     * Examples
     * redis> SET mykey "10"
     * OK
     * redis> DECRBY mykey 5
     * (integer) 5
     * redis>
     */
    @Test
    public void string_decrby() {
        jedis.set("m", "10");
        int i = jedis.decrBy("m", 5).intValue();
        Assert.assertEquals(i, 5);
        //如果没有value，则先置为0之后再做操作。
        int j = jedis.decrBy("notvalue", 12).intValue();
        Assert.assertEquals(j, -12);
    }

    //string 的append 操作

    /**
     * Available since 2.0.0.
     * Time complexity: O(1). The amortized time complexity is O(1) assuming the appended value is small and the already present value is of any size, since the dynamic string library used by Redis will double the free space available on every reallocation.
     * If key already exists and is a string, this command appends the value at the end of the string. If key does not exist it is created and set as an empty string, so APPEND will be similar to SET in this special case.
     * Return value
     * Integer reply: the length of the string after the append operation.
     * Examples
     * redis> EXISTS mykey
     * (integer) 0
     * redis> APPEND mykey "Hello"
     * (integer) 5
     * redis> APPEND mykey " World"
     * (integer) 11
     * redis> GET mykey
     * "Hello World"
     * redis>
     */
    @Test
    public void string_append() {
        jedis.set("foo", "bar");
        jedis.append("foo", "barbar");
        Assert.assertEquals(jedis.get("foo"), "barbarbar");
        jedis.append("freshvalue", "sameasset");
        Assert.assertEquals(jedis.get("freshvalue"), "sameasset");
    }

    //string的strlen 命令

    /**
     * Available since 2.2.0.
     * Time complexity: O(1)
     * Returns the length of the string value stored at key. An error is returned when key holds a non-string value.
     * Return value
     * Integer reply: the length of the string at key, or 0 when key does not exist.
     * Examples
     * redis> SET mykey "Hello world"
     * OK
     * redis> STRLEN mykey
     * (integer) 11
     * redis> STRLEN nonexisting
     * (integer) 0
     * redis>
     */
    @Test
    public void string_strlen() {
        jedis.set("foo", "bar");
        Assert.assertEquals(jedis.strlen("foo").intValue(), 3);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////               SOME OTHER MOETHODS               ////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //getbit 以及setbit  对其中的一bit位进行set和get

    /**
     * 名词解释： offset ： 偏移量
     * SETBIT key offset value
     * Available since 2.2.0.
     * Time complexity: O(1)
     * Sets or clears the bit at offset in the string value stored at key.
     * The bit is either set or cleared depending on value, which can be either 0 or 1. When key does not exist, a new string value is created. The string is grown to make sure it can hold a bit at offset. The offset argument is required to be greater than or equal to 0, and smaller than 232 (this limits bitmaps to 512MB). When the string at key is grown, added bits are set to 0.
     * Warning: When setting the last possible bit (offset equal to 232 -1) and the string value stored at key does not yet hold a string value, or holds a small string value, Redis needs to allocate all intermediate memory which can block the server for some time. On a 2010 MacBook Pro, setting bit number 232 -1 (512MB allocation) takes ~300ms, setting bit number 230 -1 (128MB allocation) takes ~80ms, setting bit number 228 -1 (32MB allocation) takes ~30ms and setting bit number 226 -1 (8MB allocation) takes ~8ms. Note that once this first allocation is done, subsequent calls to SETBIT for the same key will not have the allocation overhead.
     * Return value
     * Integer reply: the original bit value stored at offset.
     * Examples
     * redis> SETBIT mykey 7 1
     * (integer) 0
     * redis> SETBIT mykey 7 0
     * (integer) 1
     * redis> GET mykey
     * "\u0000"
     * redis>
     * SETBIT key offset value
     * <p/>
     * 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。
     * <p/>
     * 位的设置或清除取决于 value 参数，可以是 0 也可以是 1 。
     * <p/>
     * 当 key 不存在时，自动生成一个新的字符串值。
     * <p/>
     * 字符串会进行伸展(grown)以确保它可以将 value 保存在指定的偏移量上。当字符串值进行伸展时，空白位置以 0 填充。
     * <p/>
     * offset 参数必须大于或等于 0 ，小于 2^32 (bit 映射被限制在 512 MB 之内)。
     */

    /**
     * GETBIT key offset
     * Available since 2.2.0.
     * Time complexity: O(1)
     * Returns the bit value at offset in the string value stored at key.
     * When offset is beyond the string length, the string is assumed to be a contiguous space with 0 bits. When key does not exist it is assumed to be an empty string, so offset is always out of range and the value is also assumed to be a contiguous space with 0 bits.
     * Return value
     * Integer reply: the bit value stored at offset.
     * Examples
     * redis> SETBIT mykey 7 1
     * (integer) 0
     * redis> GETBIT mykey 0
     * (integer) 0
     * redis> GETBIT mykey 7
     * (integer) 1
     * redis> GETBIT mykey 100
     * (integer) 0
     * redis>
     * GETBIT key offset
     * <p/>
     * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)。
     * <p/>
     * 当 offset 比字符串值的长度大，或者 key 不存在时，返回 0 。
     */

    @Test
    public void string_setbit() {
        jedis.setbit("testbit", 10, true);
        Assert.assertTrue(jedis.getbit("testbit", 10));
        Assert.assertFalse(jedis.getbit("testbit", 11));

    }


    /**
     * BITCOUNT key [start] [end]
     * Available since 2.6.0.
     * Time complexity: O(N)
     * Count the number of set bits (population counting) in a string.
     * By default all the bytes contained in the string are examined. It is possible to specify the counting operation only in an interval passing the additional arguments start and end.
     * Like for the GETRANGE command start and end can contain negative values in order to index bytes starting from the end of the string, where -1 is the last byte, -2 is the penultimate, and so forth.
     * Non-existent keys are treated as empty strings, so the command will return zero.
     * Return value
     * Integer reply
     * The number of bits set to 1.
     * Examples
     * redis> SET mykey "foobar"
     * OK
     * redis> BITCOUNT mykey
     * (integer) 26
     * redis> BITCOUNT mykey 0 0
     * (integer) 4
     * redis> BITCOUNT mykey 1 1
     * (integer) 6
     * redis>
     * BITCOUNT key [start] [end]
     * <p/>
     * 计算给定字符串中，被设置为 1 的比特位的数量。
     * <p/>
     * 一般情况下，给定的整个字符串都会被进行计数，通过指定额外的 start 或 end 参数，可以让计数只在特定的位上进行。
     * <p/>
     * start 和 end 参数的设置和 GETRANGE 命令类似，都可以使用负数值：比如 -1 表示最后一个位，而 -2 表示倒数第二个位，以此类推。
     * <p/>
     * 不存在的 key 被当成是空字符串来处理，因此对一个不存在的 key 进行 BITCOUNT 操作，结果为 0 。
     * <p/>
     * 可用版本：
     * >= 2.6.0
     * 时间复杂度：
     * O(N)
     * 返回值：
     * 被设置为 1 的位的数量。
     * <p/>
     * redis> BITCOUNT bits
     * (integer) 0
     * <p/>
     * redis> SETBIT bits 0 1          # 0001
     * (integer) 0
     * <p/>
     * redis> BITCOUNT bits
     * (integer) 1
     * <p/>
     * redis> SETBIT bits 3 1          # 1001
     * (integer) 0
     * <p/>
     * redis> BITCOUNT bits
     * (integer) 2
     * <p/>
     * 模式：使用 bitmap 实现用户上线次数统计
     * <p/>
     * Bitmap 对于一些特定类型的计算非常有效。
     * <p/>
     * 假设现在我们希望记录自己网站上的用户的上线频率，比如说，计算用户 A 上线了多少天，用户 B 上线了多少天，诸如此类，以此作为数据，从而决定让哪些用户参加 beta 测试等活动 —— 这个模式可以使用 SETBIT 和 BITCOUNT 来实现。
     * <p/>
     * 比如说，每当用户在某一天上线的时候，我们就使用 SETBIT ，以用户名作为 key ，将那天所代表的网站的上线日作为 offset 参数，并将这个 offset 上的为设置为 1 。
     * <p/>
     * 举个例子，如果今天是网站上线的第 100 天，而用户 peter 在今天阅览过网站，那么执行命令 SETBIT peter 100 1 ；如果明天 peter 也继续阅览网站，那么执行命令 SETBIT peter 101 1 ，以此类推。
     * <p/>
     * 当要计算 peter 总共以来的上线次数时，就使用 BITCOUNT 命令：执行 BITCOUNT peter ，得出的结果就是 peter 上线的总天数。
     * <p/>
     * 更详细的实现可以参考博文(墙外) Fast, easy, realtime metrics using Redis bitmaps 。
     */

    @Test
    public void string_bitcount() {
        jedis.setbit("c", 0, true);
        jedis.setbit("c", 20, true);
//        Assert.assertEquals(2, jedis.bitcount("c"));
        //目前客户端暂不支持。ver 2.4

    }


    /**
     * BITOP operation destkey key [key ...]

     对一个或多个保存二进制位的字符串 key 进行位元操作，并将结果保存到 destkey 上。

     operation 可以是 AND 、 OR 、 NOT 、 XOR 这四种操作中的任意一种：

     BITOP AND destkey key [key ...] ，对一个或多个 key 求逻辑并，并将结果保存到 destkey 。
     BITOP OR destkey key [key ...] ，对一个或多个 key 求逻辑或，并将结果保存到 destkey 。
     BITOP XOR destkey key [key ...] ，对一个或多个 key 求逻辑异或，并将结果保存到 destkey 。
     BITOP NOT destkey key ，对给定 key 求逻辑非，并将结果保存到 destkey 。

     除了 NOT 操作之外，其他操作都可以接受一个或多个 key 作为输入。

     处理不同长度的字符串

     当 BITOP 处理不同长度的字符串时，较短的那个字符串所缺少的部分会被看作 0 。

     空的 key 也被看作是包含 0 的字符串序列。
     redis> SETBIT bits-1 0 1        # bits-1 = 1001
     (integer) 0

     redis> SETBIT bits-1 3 1
     (integer) 0

     redis> SETBIT bits-2 0 1        # bits-2 = 1011
     (integer) 0

     redis> SETBIT bits-2 1 1
     (integer) 0

     redis> SETBIT bits-2 3 1
     (integer) 0

     redis> BITOP AND and-result bits-1 bits-2
     (integer) 1

     redis> GETBIT and-result 0      # and-result = 1001
     (integer) 1

     redis> GETBIT and-result 1
     (integer) 0

     redis> GETBIT and-result 2
     (integer) 0

     redis> GETBIT and-result 3
     (integer) 1
     */
}
