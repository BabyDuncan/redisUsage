package com.babyduncan.redis;

import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: zgh
 * Date: 12-12-31
 * Time: 21:27
 */
public class HashUsage {
    Jedis jedis = new Jedis("localhost", 6379);

    /**
     * HSET key field value
     * Available since 2.0.0.
     * Time complexity: O(1)
     * Sets field in the hash stored at key to value. If key does not exist, a new key holding a hash is created. If field already exists in the hash, it is overwritten.
     * Return value
     * Integer reply, specifically:
     * 1 if field is a new field in the hash and value was set.
     * 0 if field already exists in the hash and the value was updated.
     * Examples
     * redis> HSET myhash field1 "Hello"
     * (integer) 1
     * redis> HGET myhash field1
     * "Hello"
     * redis>
     */
    @Test
    public void hash_hset() {
        jedis.flushDB();
        long l = jedis.hset("key1", "field1", "value1");
        Assert.assertEquals(1l, l);
        Assert.assertEquals("value1", jedis.hget("key1", "field1"));
        long l2 = jedis.hset("key1", "field1", "value21");
        Assert.assertEquals(0l, l2);
    }

    /**
     * Available since 2.0.0.
     * Time complexity: O(1)
     * Sets field in the hash stored at key to value, only if field does not yet exist. If key does not exist, a new key holding a hash is created. If field already exists, this operation has no effect.
     * Return value
     * Integer reply, specifically:
     * 1 if field is a new field in the hash and value was set.
     * 0 if field already exists in the hash and no operation was performed.
     * Examples
     * redis> HSETNX myhash field "Hello"
     * (integer) 1
     * redis> HSETNX myhash field "World"
     * (integer) 0
     * redis> HGET myhash field
     * "Hello"
     * redis>
     * <p/>
     * nx针对的是field，而不是ey。
     */
    @Test
    public void hash_hsetnx() {
        jedis.flushDB();
        jedis.hset("foo", "f1", "bar");
        long l = jedis.hsetnx("foo", "f1", "bar2");
        long l2 = jedis.hsetnx("foo2", "f1", "bar");
        Assert.assertEquals(0l, l);
        Assert.assertEquals(1l, l2);
        Assert.assertEquals("bar", jedis.hget("foo", "f1"));
        Assert.assertEquals("bar", jedis.hget("foo2", "f1"));

    }

    /**
     * it looks like string function -> mset , and  it's really like it .
     * Available since 2.0.0.
     * Time complexity: O(N) where N is the number of fields being set.
     * Sets the specified fields to their respective values in the hash stored at key. This command overwrites any existing fields in the hash. If key does not exist, a new key holding a hash is created.
     * Return value
     * Status code reply
     * Examples
     * redis> HMSET myhash field1 "Hello" field2 "World"
     * OK
     * redis> HGET myhash field1
     * "Hello"
     * redis> HGET myhash field2
     * "World"
     * redis>
     */
    @Test
    public void hash_hmset() {
        Map<String, String> m = new HashMap<String, String>();
        m.put("foo", "bar");
        m.put("foo1", "bar1");
        m.put("foo2", "bar2");
        jedis.hmset("key", m);
        Assert.assertEquals("bar", jedis.hget("key", "foo"));
        Assert.assertEquals("bar1", jedis.hget("key", "foo1"));
        Assert.assertEquals("bar2", jedis.hget("key", "foo2"));
    }

    /**
     * Available since 2.0.0.
     * Time complexity: O(1)
     * Returns the value associated with field in the hash stored at key.
     * Return value
     * Bulk reply: the value associated with field, or nil when field is not present in the hash or key does not exist.
     * Examples
     * redis> HSET myhash field1 "foo"
     * (integer) 1
     * redis> HGET myhash field1
     * "foo"
     * redis> HGET myhash field2
     * (nil)
     * redis>
     */
    @Test
    public void hash_hget() {
        jedis.hset("aa", "dbb", "hello");
        String s = jedis.hget("aa", "dbb");
        String s1 = jedis.hget("cc", "dd");
        Assert.assertEquals("hello", s);
        Assert.assertEquals(null, s1);
    }

    /**
     * Returns the values associated with the specified fields in the hash stored at key.
     * For every field that does not exist in the hash, a nil value is returned. Because a non-existing keys are treated as empty hashes, running HMGET against a non-existing key will return a list of nil values.
     * Return value
     * Multi-bulk reply: list of values associated with the given fields, in the same order as they are requested.
     * redis> HSET myhash field1 "Hello"
     * (integer) 1
     * redis> HSET myhash field2 "World"
     * (integer) 1
     * redis> HMGET myhash field1 field2 nofield
     * 1) "Hello"
     * 2) "World"
     * 3) (nil)
     * redis>
     */
    @Test
    public void hash_hmget() {
        jedis.flushDB();
        jedis.hset("k1", "f1", "v1");
        jedis.hset("k1", "f2", "v2");
        List<String> l = jedis.hmget("k1", "f1", "f2");
        for (String s : l) {
            System.out.println(s);
        }
    }

    /**
     * Available since 2.0.0.
     * Time complexity: O(1)
     * Increments the number stored at field in the hash stored at key by increment. If key does not exist, a new key holding a hash is created. If field does not exist the value is set to 0 before the operation is performed.
     * The range of values supported by HINCRBY is limited to 64 bit signed integers.
     * Return value
     * Integer reply: the value at field after the increment operation.
     * Examples
     * Since the increment argument is signed, both increment and decrement operations can be performed:
     * redis> HSET myhash field 5
     * (integer) 1
     * redis> HINCRBY myhash field 1
     * (integer) 6
     * redis> HINCRBY myhash field -1
     * (integer) 5
     * redis> HINCRBY myhash field -10
     * (integer) -5
     * redis>
     */
    @Test
    public void hash_hincrby() {
        jedis.hset("num", "myage", "22");
        //a year passed by
        Assert.assertEquals(23l + "", "" + jedis.hincrBy("num", "myage", 1));
    }

    /**
     * Available since 2.0.0.
     * Time complexity: O(1)
     * Returns if field is an existing field in the hash stored at key.
     * Return value
     * Integer reply, specifically:
     * 1 if the hash contains field.
     * 0 if the hash does not contain field, or key does not exist.
     * Examples
     * redis> HSET myhash field1 "foo"
     * (integer) 1
     * redis> HEXISTS myhash field1
     * (integer) 1
     * redis> HEXISTS myhash field2
     * (integer) 0
     * redis>
     */
    @Test
    public void hash_hexists() {
        Assert.assertTrue(jedis.hexists("num", "myage"));
        Assert.assertFalse(jedis.hexists("sadsad", "adsadsade"));
    }

    /**
     * Available since 2.0.0.
     * Time complexity: O(1)
     * Returns the number of fields contained in the hash stored at key.
     * Return value
     * Integer reply: number of fields in the hash, or 0 when key does not exist.
     * Examples
     * redis> HSET myhash field1 "Hello"
     * (integer) 1
     * redis> HSET myhash field2 "World"
     * (integer) 1
     * redis> HLEN myhash
     * (integer) 2
     * redis>
     */
    @Test
    public void hash_hlen() {
        System.out.println(jedis.hlen("num"));
    }

    /**
     * Removes the specified fields from the hash stored at key. Specified fields that do not exist within this hash are ignored. If key does not exist, it is treated as an empty hash and this command returns 0.
     * Return value
     * Integer reply: the number of fields that were removed from the hash, not including specified but non existing fields.
     * History
     * >= 2.4: Accepts multiple field arguments. Redis versions older than 2.4 can only remove a field per call.
     * To remove multiple fields from a hash in an atomic fashion in earlier versions, use a MULTI / EXEC block.
     * Examples
     * redis> HSET myhash field1 "foo"
     * (integer) 1
     * redis> HDEL myhash field1
     * (integer) 1
     * redis> HDEL myhash field2
     * (integer) 0
     * redis>
     */
    @Test
    public void hash_hdel() {

        jedis.hset("k1", "f1", "v1");
        jedis.hset("k1", "f2", "v2");
        jedis.hset("k1", "f3", "v3");

        jedis.hdel("k1", "f1", "f2");
        Assert.assertEquals(null, jedis.hget("k1", "f2"));
        Assert.assertEquals("v3", jedis.hget("k1", "f3"));

    }

    /**
     * Available since 2.0.0.
     * Time complexity: O(N) where N is the size of the hash.
     * Returns all field names in the hash stored at key.
     * Return value
     * Multi-bulk reply: list of fields in the hash, or an empty list when key does not exist.
     * Examples
     * redis> HSET myhash field1 "Hello"
     * (integer) 1
     * redis> HSET myhash field2 "World"
     * (integer) 1
     * redis> HKEYS myhash
     * 1) "field1"
     * 2) "field2"
     * redis>
     * 我砸觉得应该叫做 hfileds呢 ？
     */
    @Test
    public void hash_hkeys() {
        System.out.println(jedis.hkeys("k1"));
    }

    /**
     * Available since 2.0.0.
     * Time complexity: O(N) where N is the size of the hash.
     * Returns all values in the hash stored at key.
     * Return value
     * Multi-bulk reply: list of values in the hash, or an empty list when key does not exist.
     * Examples
     * redis> HSET myhash field1 "Hello"
     * (integer) 1
     * redis> HSET myhash field2 "World"
     * (integer) 1
     * redis> HVALS myhash
     * 1) "Hello"
     * 2) "World"
     * redis>
     */
    @Test
    public void hash_hvals() {
        jedis.hset("k1", "adsad", "sadsadsad");
        System.out.println(jedis.hvals("k1"));
    }

    /**
     * Available since 2.0.0.
     * Time complexity: O(N) where N is the size of the hash.
     * Returns all fields and values of the hash stored at key. In the returned value, every field name is followed by its value, so the length of the reply is twice the size of the hash.
     * Return value
     * Multi-bulk reply: list of fields and their values stored in the hash, or an empty list when key does not exist.
     * Examples
     * redis> HSET myhash field1 "Hello"
     * (integer) 1
     * redis> HSET myhash field2 "World"
     * (integer) 1
     * redis> HGETALL myhash
     * 1) "field1"
     * 2) "Hello"
     * 3) "field2"
     * 4) "World"
     * redis>
     */
    @Test
    public void hash_hgetall() {
        System.out.println(jedis.hgetAll("k1"));
    }

}
