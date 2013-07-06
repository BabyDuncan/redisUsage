package com.babyduncan.redis;

import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;

/**
 * User: zgh
 * Date: 13-1-4
 * Time: 17:32
 */
public class ListUsage {
    Jedis jedis = new Jedis("localhost");


    /**
     * Available since 1.0.0.
     * Time complexity: O(1)
     * Insert all the specified values at the head of the list stored at key. If key does not exist, it is created as empty list before performing the push operations. When key holds a value that is not a list, an error is returned.
     * It is possible to push multiple elements using a single command call just specifying multiple arguments at the end of the command. Elements are inserted one after the other to the head of the list, from the leftmost element to the rightmost element. So for instance the command LPUSH mylist a b c will result into a list containing c as first element, b as second element and a as third element.
     * Return value
     * Integer reply: the length of the list after the push operations.
     * History
     * >= 2.4: Accepts multiple value arguments. In Redis versions older than 2.4 it was possible to push a single value per command.
     * Examples
     * redis>  LPUSH mylist "world"
     * (integer) 1
     * redis>  LPUSH mylist "hello"
     * (integer) 2
     * redis>  LRANGE mylist 0 -1
     * 1) "hello"
     * 2) "world"
     * redis>
     * <p/>
     * left push -->>>>>>
     */
    @Test
    public void list_lpush() {
        jedis.flushAll();
        //you can use it like jedis.lpush("mylist","1","2","3");  under a higher version redis.
        jedis.lpush("mylist", "hello");
        jedis.lpush("mylist", "hello2");
        jedis.lpush("mylist", "hello3");
        jedis.lpush("mylist", "hello4");
        //0 means the first one.-1 means the last one
        System.out.println(jedis.lrange("mylist", 0, -1));
        //so here -1 equals 3
        System.out.println(jedis.lrange("mylist", 0, 3));
    }

    /**
     * Available since 1.0.0.
     * Time complexity: O(1)
     * Insert all the specified values at the tail of the list stored at key. If key does not exist, it is created as empty list before performing the push operation. When key holds a value that is not a list, an error is returned.
     * It is possible to push multiple elements using a single command call just specifying multiple arguments at the end of the command. Elements are inserted one after the other to the tail of the list, from the leftmost element to the rightmost element. So for instance the command RPUSH mylist a b c will result into a list containing a as first element, b as second element and c as third element.
     * Return value
     * Integer reply: the length of the list after the push operation.
     * History
     * >= 2.4: Accepts multiple value arguments. In Redis versions older than 2.4 it was possible to push a single value per command.
     * Examples
     * redis>  RPUSH mylist "hello"
     * (integer) 1
     * redis>  RPUSH mylist "world"
     * (integer) 2
     * redis>  LRANGE mylist 0 -1
     * 1) "hello"
     * 2) "world"
     * redis>
     * right push    <<< ----
     */
    @Test
    public void list_rpush() {
        jedis.rpush("rightlist", "1");
        jedis.rpush("rightlist", "2");
        jedis.rpush("rightlist", "3");
        jedis.rpush("rightlist", "4");
        System.out.println(jedis.lrange("rightlist", 0, -1));
    }

    /**
     * Available since 2.2.0.
     * Time complexity: O(N) where N is the number of elements to traverse before seeing the value pivot. This means that inserting somewhere on the left end on the list (head) can be considered O(1) and inserting somewhere on the right end (tail) is O(N).
     * Inserts value in the list stored at key either before or after the reference value pivot.
     * When key does not exist, it is considered an empty list and no operation is performed.
     * An error is returned when key exists but does not hold a list value.
     * Return value
     * Integer reply: the length of the list after the insert operation, or -1 when the value pivot was not found.
     * Examples
     * redis> RPUSH mylist "Hello"
     * (integer) 1
     * redis> RPUSH mylist "World"
     * (integer) 2
     * redis> LINSERT mylist BEFORE "World" "There"
     * (integer) 3
     * redis> LRANGE mylist 0 -1
     * 1) "Hello"
     * 2) "There"
     * 3) "World"
     * redis>
     * <p/>
     * after means when pop these items : after  so do before
     * after and before commands under pop situation  not push situation
     */
    @Test
    public void list_linsert() {
        jedis.flushAll();
        jedis.lpush("myleftlist", "1");
        jedis.lpush("myleftlist", "2");
        jedis.lpush("myleftlist", "3");
        jedis.lpush("myleftlist", "4");
        //we can image list is :  4321
        jedis.linsert("myleftlist", Client.LIST_POSITION.AFTER, "2", "21");
        System.out.println(jedis.lrange("myleftlist", 0, -1));
        jedis.linsert("myleftlist", Client.LIST_POSITION.BEFORE, "2", "32");
        System.out.println(jedis.lrange("myleftlist", 0, -1));
        //有一个注意的地方，before和after的基准是按照顺序查找的第一个，唯一的话不涉及这种状况，不唯一的话按照第一个算。
        /**
         * redis 127.0.0.1:6379> lpush l 1
         (integer) 1
         redis 127.0.0.1:6379> lpush l 1
         (integer) 2
         redis 127.0.0.1:6379> lpush l 1
         (integer) 3
         redis 127.0.0.1:6379> lpush l 1
         (integer) 4
         redis 127.0.0.1:6379> lpush l 1
         (integer) 5
         redis 127.0.0.1:6379> lrange l 0 -1
         1) "1"
         2) "1"
         3) "1"
         4) "1"
         5) "1"
         redis 127.0.0.1:6379> linsert l before 1 2
         (integer) 6
         redis 127.0.0.1:6379> lrange l 0 -1
         1) "2"
         2) "1"
         3) "1"
         4) "1"
         5) "1"
         6) "1"
         redis 127.0.0.1:6379> linsert l after 1 2
         (integer) 7
         redis 127.0.0.1:6379> lrange l 0 -1
         1) "2"
         2) "1"
         3) "2"
         4) "1"
         5) "1"
         6) "1"
         7) "1"
         redis 127.0.0.1:6379>
         */
    }

    /**
     * Available since 1.0.0.
     * Time complexity: O(N) where N is the length of the list. Setting either the first or the last element of the list is O(1).
     * Sets the list element at index to value. For more information on the index argument, see LINDEX.
     * An error is returned for out of range indexes.
     * Return value
     * Status code reply
     * Examples
     * redis> RPUSH mylist "one"
     * (integer) 1
     * redis> RPUSH mylist "two"
     * (integer) 2
     * redis> RPUSH mylist "three"
     * (integer) 3
     * redis> LSET mylist 0 "four"
     * OK
     * redis> LSET mylist -2 "five"
     * OK
     * redis> LRANGE mylist 0 -1
     * 1) "four"
     * 2) "five"
     * 3) "three"
     * redis>
     */
    @Test
    public void list_lset() {
        //very simple
        jedis.lpush("lsetlist", "1");
        jedis.lpush("lsetlist", "1");
        jedis.lpush("lsetlist", "1");
        jedis.lset("lsetlist", 0, "start");
        System.out.println(jedis.lrange("lsetlist", 0, -1));
    }

    /**
     * Available since 1.0.0.
     * Time complexity: O(N) where N is the length of the list.
     * Removes the first count occurrences of elements equal to value from the list stored at key. The count argument influences the operation in the following ways:
     * count > 0: Remove elements equal to value moving from head to tail.
     * count < 0: Remove elements equal to value moving from tail to head.
     * count = 0: Remove all elements equal to value.
     * For example, LREM list -2 "hello" will remove the last two occurrences of "hello" in the list stored at list.
     * Note that non-existing keys are treated like empty lists, so when key does not exist, the command will always return 0.
     * Return value
     * Integer reply: the number of removed elements.
     * Examples
     * redis> RPUSH mylist "hello"
     * (integer) 1
     * redis> RPUSH mylist "hello"
     * (integer) 2
     * redis> RPUSH mylist "foo"
     * (integer) 3
     * redis> RPUSH mylist "hello"
     * (integer) 4
     * redis> LREM mylist -2 "hello"
     * (integer) 2
     * redis> LRANGE mylist 0 -1
     * 1) "hello"
     * 2) "foo"
     * redis>
     */
    @Test
    public void list_lrem() {
        // jedis.flushAll();
        jedis.lpush("lremlist", "hello");
        jedis.lpush("lremlist", "hello");
        jedis.lpush("lremlist", "world");
        jedis.lpush("lremlist", "hello");
        jedis.lpush("lremlist", "foobar");
        jedis.lpush("lremlist", "hello");
        //this list is hello foobar hello world hello hello
        jedis.lrem("lremlist", 1, "hello");
        //the most left hello will be removed
        System.out.println(jedis.lrange("lremlist", 0, -1));
        //now list is   foobar hello world hello hello
        Assert.assertEquals("foobar", jedis.lrange("lremlist", 0, -1).get(0));
        jedis.lrem("lremlist", -1, "hello");
        //the most right hello will be removed
        //now list is foobar hello world hello
        System.out.println(jedis.lrange("lremlist", 0, -1));
        Assert.assertEquals("world", jedis.lrange("lremlist", 0, -1).get(2));
        jedis.lrem("lremlist", 0, "hello");
        System.out.println(jedis.lrange("lremlist", 0, -1));
        // all hello will be removed
        Assert.assertEquals(2, jedis.lrange("lremlist", 0, -1).size());

    }

    /**
     * Available since 1.0.0.
     * Time complexity: O(N) where N is the number of elements to be removed by the operation.
     * Trim an existing list so that it will contain only the specified range of elements specified. Both start and stop are zero-based indexes, where 0 is the first element of the list (the head), 1 the next element and so on.
     * For example: LTRIM foobar 0 2 will modify the list stored at foobar so that only the first three elements of the list will remain.
     * start and end can also be negative numbers indicating offsets from the end of the list, where -1 is the last element of the list, -2 the penultimate element and so on.
     * Out of range indexes will not produce an error: if start is larger than the end of the list, or start > end, the result will be an empty list (which causes key to be removed). If end is larger than the end of the list, Redis will treat it like the last element of the list.
     * A common use of LTRIM is together with LPUSH / RPUSH. For example:
     * LPUSH mylist someelement
     * LTRIM mylist 0 99
     * This pair of commands will push a new element on the list, while making sure that the list will not grow larger than 100 elements. This is very useful when using Redis to store logs for example. It is important to note that when used in this way LTRIM is an O(1) operation because in the average case just one element is removed from the tail of the list.
     * Return value
     * Status code reply
     * Examples
     * redis> RPUSH mylist "one"
     * (integer) 1
     * redis> RPUSH mylist "two"
     * (integer) 2
     * redis> RPUSH mylist "three"
     * (integer) 3
     * redis> LTRIM mylist 1 -1
     * OK
     * redis> LRANGE mylist 0 -1
     * 1) "two"
     * 2) "three"
     * redis>
     */
    @Test
    public void list_ltrim() {
        System.out.println(jedis.lrange("lremlist", 0, -1));
        // now it is [foobar, world]
        jedis.ltrim("lremlist", 0, 0);
        //ltrim is key start end not key start length O(∩_∩)O~
        System.out.println(jedis.lrange("lremlist", 0, -1));
    }

    /**
     * Available since 1.0.0.
     * Time complexity: O(1)
     * Removes and returns the first element of the list stored at key.
     * Return value
     * Bulk reply: the value of the first element, or nil when key does not exist.
     * Examples
     * redis> RPUSH mylist "one"
     * (integer) 1
     * redis> RPUSH mylist "two"
     * (integer) 2
     * redis> RPUSH mylist "three"
     * (integer) 3
     * redis> LPOP mylist
     * "one"
     * redis> LRANGE mylist 0 -1
     * 1) "two"
     * 2) "three"
     * redis>
     */
    @Test
    public void list_lpop() {
        jedis.lpush("lpoplist", "a");
        jedis.lpush("lpoplist", "b");
        jedis.lpush("lpoplist", "c");
        jedis.lpush("lpoplist", "d");
        jedis.lpush("lpoplist", "e");
        jedis.lpush("lpoplist", "f");
        String pop = jedis.lpop("lpoplist");
        Assert.assertEquals("f", pop);
    }

    /**
     * Available since 1.0.0.
     * Time complexity: O(1)
     * Removes and returns the last element of the list stored at key.
     * Return value
     * Bulk reply: the value of the last element, or nil when key does not exist.
     * Examples
     * redis> RPUSH mylist "one"
     * (integer) 1
     * redis> RPUSH mylist "two"
     * (integer) 2
     * redis> RPUSH mylist "three"
     * (integer) 3
     * redis> RPOP mylist
     * "three"
     * redis> LRANGE mylist 0 -1
     * 1) "one"
     * 2) "two"
     * redis>
     */
    @Test
    public void list_rpop() {
        String pop = jedis.rpop("lpoplist");
        Assert.assertEquals("a", pop);
    }

    /**
     * Available since 1.2.0.
     * Time complexity: O(1)
     * Atomically returns and removes the last element (tail) of the list stored at source, and pushes the element at the first element (head) of the list stored at destination.
     * For example: consider source holding the list a,b,c, and destination holding the list x,y,z. Executing RPOPLPUSH results in source holding a,b and destination holding c,x,y,z.
     * If source does not exist, the value nil is returned and no operation is performed. If source and destination are the same, the operation is equivalent to removing the last element from the list and pushing it as first element of the list, so it can be considered as a list rotation command.
     * Return value
     * Bulk reply: the element being popped and pushed.
     * Examples
     * redis> RPUSH mylist "one"
     * (integer) 1
     * redis> RPUSH mylist "two"
     * (integer) 2
     * redis> RPUSH mylist "three"
     * (integer) 3
     * redis> RPOPLPUSH mylist myotherlist
     * "three"
     * redis> LRANGE mylist 0 -1
     * 1) "one"
     * 2) "two"
     * redis> LRANGE myotherlist 0 -1
     * 1) "three"
     * redis>
     */
    @Test
    public void rpoplpush() {
        //source list
        jedis.rpush("rl", "source1");
        jedis.rpush("rl", "source2");
        jedis.rpush("rl", "source3");
        jedis.rpush("rl", "source4");
        jedis.rpush("rl", "source5");
        //source5 source4 source3 source2 source1
        jedis.rpoplpush("rl", "dis");
        jedis.rpoplpush("rl", "dis");
        jedis.rpoplpush("rl", "dis");
        System.out.println(jedis.lrange("dis", 0, -1));
    }

    /**
     * Returns the element at index index in the list stored at key. The index is zero-based, so 0 means the first element, 1 the second element and so on. Negative indices can be used to designate elements starting at the tail of the list. Here, -1 means the last element, -2 means the penultimate and so forth.
     * When the value at key is not a list, an error is returned.
     * Return value
     * Bulk reply: the requested element, or nil when index is out of range.
     * Examples
     * redis> LPUSH mylist "World"
     * (integer) 1
     * redis> LPUSH mylist "Hello"
     * (integer) 2
     * redis> LINDEX mylist 0
     * "Hello"
     * redis> LINDEX mylist -1
     * "World"
     * redis> LINDEX mylist 3
     * (nil)
     * redis>
     * <p/>
     * it is very interesting  !!! test lindex mylist sadasd   . you can test it only under a shell
     */
    @Test
    public void lindex() {
        System.out.println(jedis.lrange("rl", 0, -1));
        System.out.println(jedis.lindex("rl", 0));
        System.out.println(jedis.lindex("rl", 1));
        System.out.println(jedis.lindex("rl", 1111111));

    }

    /**
     * Returns the length of the list stored at key. If key does not exist, it is interpreted as an empty list and 0 is returned. An error is returned when the value stored at key is not a list.
     * Return value
     * Integer reply: the length of the list at key.
     * Examples
     * redis> LPUSH mylist "World"
     * (integer) 1
     * redis> LPUSH mylist "Hello"
     * (integer) 2
     * redis> LLEN mylist
     * (integer) 2
     * redis>
     */
    @Test
    public void llen() {
        System.out.println(jedis.llen("rl"));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////         SOME BLOCK METHODS     //////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //look here http://redis.io/commands/blpop
    //http://redis.io/commands/brpop
    //http://redis.io/commands/brpoplpush
}
