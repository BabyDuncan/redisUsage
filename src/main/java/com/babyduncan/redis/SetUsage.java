package com.babyduncan.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * User: zgh
 * Date: 13-1-5
 * Time: 16:40
 */
public class SetUsage {

    Jedis jedis = new Jedis("localhost");

    /**
     * Add the specified members to the set stored at key. Specified members that are already a member of this set are ignored. If key does not exist, a new set is created before adding the specified members.
     * An error is returned when the value stored at key is not a set.
     * Return value
     * Integer reply: the number of elements that were added to the set, not including all the elements already present into the set.
     * History
     * >= 2.4: Accepts multiple member arguments. Redis versions before 2.4 are only able to add a single member per call.
     * Examples
     * redis> SADD myset "Hello"
     * (integer) 1
     * redis> SADD myset "World"
     * (integer) 1
     * redis> SADD myset "World"
     * (integer) 0
     * redis> SMEMBERS myset
     * 1) "World"
     * 2) "Hello"
     * redis>
     */
    @Test
    public void set_sadd() {
        jedis.sadd("testset", "a");
        jedis.sadd("testset", "b");
        jedis.sadd("testset", "c");
        System.out.println(jedis.smembers("testset"));  //there is no sort .
        // id a member is already exists,ignore it .
        jedis.sadd("testset", "c");
        System.out.println(jedis.smembers("testset"));  //there is no sort .

    }

    /**
     * Remove the specified members from the set stored at key. Specified members that are not a member of this set are ignored. If key does not exist, it is treated as an empty set and this command returns 0.
     * An error is returned when the value stored at key is not a set.
     * Return value
     * Integer reply: the number of members that were removed from the set, not including non existing members.
     * History
     * >= 2.4: Accepts multiple member arguments. Redis versions older than 2.4 can only remove a set member per call.
     * Examples
     * redis> SADD myset "one"
     * (integer) 1
     * redis> SADD myset "two"
     * (integer) 1
     * redis> SADD myset "three"
     * (integer) 1
     * redis> SREM myset "one"
     * (integer) 1
     * redis> SREM myset "four"
     * (integer) 0
     * redis> SMEMBERS myset
     * 1) "two"
     * 2) "three"
     * redis>
     */
    @Test
    public void set_srem() {
        jedis.srem("testset", "a");
        System.out.println(jedis.smembers("testset"));
    }

    /**
     * Removes and returns a random element from the set value stored at key.
     * This operation is similar to SRANDMEMBER, that returns a random element from a set but does not remove it.
     * Return value
     * Bulk reply: the removed element, or nil when key does not exist.
     * Examples
     * redis> SADD myset "one"
     * (integer) 1
     * redis> SADD myset "two"
     * (integer) 1
     * redis> SADD myset "three"
     * (integer) 1
     * redis> SPOP myset
     * "three"
     * redis> SMEMBERS myset
     * 1) "two"
     * 2) "one"
     * redis>
     */
    @Test
    public void set_spop() {
        jedis.sadd("popset", "a");
        jedis.sadd("popset", "b");
        jedis.sadd("popset", "c");
        jedis.sadd("popset", "d");
        jedis.sadd("popset", "e");
        jedis.sadd("popset", "f");
        System.out.println(jedis.spop("popset"));
        System.out.println(jedis.spop("popset"));
        System.out.println(jedis.spop("popset"));
        System.out.println(jedis.spop("popset"));
    }

    /**
     * Available since 1.0.0.
     * Time complexity: O(N) where N is the total number of elements in all given sets.
     * Returns the members of the set resulting from the difference between the first set and all the successive sets.
     * For example:
     * key1 = {a,b,c,d}
     * key2 = {c}
     * key3 = {a,c,e}
     * SDIFF key1 key2 key3 = {b,d}
     * Keys that do not exist are considered to be empty sets.
     * Return value
     * Multi-bulk reply: list with members of the resulting set.
     * Examples
     * redis> SADD key1 "a"
     * (integer) 1
     * redis> SADD key1 "b"
     * (integer) 1
     * redis> SADD key1 "c"
     * (integer) 1
     * redis> SADD key2 "c"
     * (integer) 1
     * redis> SADD key2 "d"
     * (integer) 1
     * redis> SADD key2 "e"
     * (integer) 1
     * redis> SDIFF key1 key2
     * 1) "b"
     * 2) "a"
     * redis>
     */
    @Test
    public void set_sdiff() {
        jedis.sadd("oneset", "hello");
        jedis.sadd("oneset", "world");
        jedis.sadd("twoset", "foo");
        jedis.sadd("twoset", "bar");
        jedis.sadd("twoset3", "hello");
        System.out.println(jedis.sdiff("oneset", "twoset", "twoset3"));
    }

    /**
     * This command is equal to SDIFF, but instead of returning the resulting set, it is stored in destination.
     * If destination already exists, it is overwritten.
     * Return value
     * Integer reply: the number of elements in the resulting set.
     * 结果存在了第一个参数里
     */
    @Test
    public void set_sdiffstore() {
        jedis.sadd("diff1", "a");
        jedis.sadd("diff1", "b");
        jedis.sadd("diff2", "a");
        jedis.sdiffstore("diffdis", "diff1", "diff2");
        System.out.println(jedis.smembers("diffdis"));
    }


    /**
     * 求交集不解释
     * Available since 1.0.0.
     * Time complexity: O(N*M) worst case where N is the cardinality of the smallest set and M is the number of sets.
     * Returns the members of the set resulting from the intersection of all the given sets.
     * For example:
     * key1 = {a,b,c,d}
     * key2 = {c}
     * key3 = {a,c,e}
     * SINTER key1 key2 key3 = {c}
     * Keys that do not exist are considered to be empty sets. With one of the keys being an empty set, the resulting set is also empty (since set intersection with an empty set always results in an empty set).
     * Return value
     * Multi-bulk reply: list with members of the resulting set.
     * Examples
     * redis> SADD key1 "a"
     * (integer) 1
     * redis> SADD key1 "b"
     * (integer) 1
     * redis> SADD key1 "c"
     * (integer) 1
     * redis> SADD key2 "c"
     * (integer) 1
     * redis> SADD key2 "d"
     * (integer) 1
     * redis> SADD key2 "e"
     * (integer) 1
     * redis> SINTER key1 key2
     * 1) "c"
     * redis>
     */
    @Test
    public void set_sinter() {
        jedis.sadd("inter1", "a");
        jedis.sadd("inter2", "b");
        jedis.sadd("inter2", "a");
        System.out.println(jedis.sinter("inter1", "inter2"));
        //sinterstore the same
    }

    /**
     * 求并集 不解释
     * <p/>
     * Available since 1.0.0.
     * Time complexity: O(N) where N is the total number of elements in all given sets.
     * Returns the members of the set resulting from the union of all the given sets.
     * For example:
     * key1 = {a,b,c,d}
     * key2 = {c}
     * key3 = {a,c,e}
     * SUNION key1 key2 key3 = {a,b,c,d,e}
     * Keys that do not exist are considered to be empty sets.
     * Return value
     * Multi-bulk reply: list with members of the resulting set.
     * Examples
     * redis> SADD key1 "a"
     * (integer) 1
     * redis> SADD key1 "b"
     * (integer) 1
     * redis> SADD key1 "c"
     * (integer) 1
     * redis> SADD key2 "c"
     * (integer) 1
     * redis> SADD key2 "d"
     * (integer) 1
     * redis> SADD key2 "e"
     * (integer) 1
     * redis> SUNION key1 key2
     * 1) "b"
     * 2) "c"
     * 3) "d"
     * 4) "a"
     * 5) "e"
     * redis>
     * <p/>
     * sunionstore  the same
     */
    @Test
    public void set_sunion() {
        System.out.println(jedis.sunion("inter1", "inter2"));
    }

    /**
     * Available since 1.0.0.
     * Time complexity: O(1)
     * Move member from the set at source to the set at destination. This operation is atomic. In every given moment the element will appear to be a member of source or destination for other clients.
     * If the source set does not exist or does not contain the specified element, no operation is performed and 0 is returned. Otherwise, the element is removed from the source set and added to the destination set. When the specified element already exists in the destination set, it is only removed from the source set.
     * An error is returned if source or destination does not hold a set value.
     * Return value
     * Integer reply, specifically:
     * 1 if the element is moved.
     * 0 if the element is not a member of source and no operation was performed.
     * Examples
     * redis> SADD myset "one"
     * (integer) 1
     * redis> SADD myset "two"
     * (integer) 1
     * redis> SADD myotherset "three"
     * (integer) 1
     * redis> SMOVE myset myotherset "two"
     * (integer) 1
     * redis> SMEMBERS myset
     * 1) "one"
     * redis> SMEMBERS myotherset
     * 1) "two"
     * 2) "three"
     * redis>
     */
    @Test
    public void set_smove() {
        jedis.sadd("source", "a");
        // three params source dis member
        jedis.smove("source", "dis", "a");
        System.out.println(jedis.smembers("dis"));

    }

    /**
     * Available since 1.0.0.
     * Time complexity: O(1)
     * Returns the set cardinality (number of elements) of the set stored at key.
     * Return value
     * Integer reply: the cardinality (number of elements) of the set, or 0 if key does not exist.
     * Examples
     * redis> SADD myset "Hello"
     * (integer) 1
     * redis> SADD myset "World"
     * (integer) 1
     * redis> SCARD myset
     * (integer) 2
     * redis>
     */
    @Test
    public void set_scard() {
        System.out.println(jedis.scard("souce"));
    }

    /**
     * Available since 1.0.0.
     * Time complexity: O(N) where N is the set cardinality.
     * Returns all the members of the set value stored at key.
     * This has the same effect as running SINTER with one argument key.
     * Return value
     * Multi-bulk reply: all elements of the set.
     * Examples
     * redis> SADD myset "Hello"
     * (integer) 1
     * redis> SADD myset "World"
     * (integer) 1
     * redis> SMEMBERS myset
     * 1) "World"
     * 2) "Hello"
     * redis>
     */
    @Test
    public void set_sismember() {
        jedis.sadd("member", "test");
        System.out.println(jedis.sismember("member", "test"));
        System.out.println(jedis.sismember("member", "test2"));
    }

    /**
     * Available since 1.0.0.
     * Time complexity: Without the count argument O(1), otherwise O(N) where N is the absolute value of the passed count.
     * When called with just the key argument, return a random element from the set value stored at key.
     * Starting from Redis version 2.6, when called with the additional count argument, return an array of count distinct elements if count is positive. If called with a negative count the behavior changes and the command is allowed to return the same element multiple times. In this case the numer of returned elements is the absolute value of the specified count.
     * When called with just the key argument, the operation is similar to SPOP, however while SPOP also removes the randomly selected element from the set, SRANDMEMBER will just return a random element without altering the original set in any way.
     * Return value
     * Bulk reply: without the additional count argument the command returns a Bulk Reply with the randomly selected element, or nil when key does not exist. Multi-bulk reply: when the additional count argument is passed the command returns an array of elements, or an empty array when key does not exist.
     * Examples
     * redis> SADD myset one two three
     * (integer) 3
     * redis> SRANDMEMBER myset
     * "three"
     * redis> SRANDMEMBER myset 2
     * 1) "one"
     * 2) "two"
     * redis> SRANDMEMBER myset -5
     * 1) "three"
     * 2) "two"
     * 3) "one"
     * 4) "three"
     * 5) "two"
     * redis>
     */
    @Test
    public void set_randommember() {
        System.out.println(jedis.srandmember("m"));
        System.out.println(jedis.srandmember("m"));
        System.out.println(jedis.srandmember("m"));
        System.out.println(jedis.srandmember("m"));
        System.out.println(jedis.srandmember("m"));
    }

}
