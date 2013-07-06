package com.babyduncan.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * User: zgh
 * Date: 13-1-5
 * Time: 22:43
 */
public class SortedsetUsage {

    //首先测试一下java的sortedset
    @Test
    public void testSortedset() {
        SortedSet<String> set = new TreeSet<String>();
        set.add("hello");
        set.add("world");
        System.out.println(set.first());
        System.out.println(set.last());
        System.out.println(Math.E);
    }

    Jedis jedis = new Jedis("localhost");

    /**
     * Available since 1.2.0.
     * Time complexity: O(log(N)) where N is the number of elements in the sorted set.
     * Adds all the specified members with the specified scores to the sorted set stored at key. It is possible to specify multiple score/member pairs. If a specified member is already a member of the sorted set, the score is updated and the element reinserted at the right position to ensure the correct ordering. If key does not exist, a new sorted set with the specified members as sole members is created, like if the sorted set was empty. If the key exists but does not hold a sorted set, an error is returned.
     * The score values should be the string representation of a numeric value, and accepts double precision floating point numbers.
     * For an introduction to sorted sets, see the data types page on sorted sets.
     * Return value
     * Integer reply, specifically:
     * The number of elements added to the sorted sets, not including elements already existing for which the score was updated.
     * History
     * >= 2.4: Accepts multiple elements. In Redis versions older than 2.4 it was possible to add or update a single member per call.
     * Examples
     * redis 127.0.0.1:6379[1]> zadd z 1 aaa
     * (integer) 1
     * redis 127.0.0.1:6379[1]> zadd z 1 bbb
     * (integer) 1
     * redis 127.0.0.1:6379[1]> zadd z 2 ccc
     * (integer) 1
     * redis 127.0.0.1:6379[1]> zadd z 3 ddd
     * (integer) 1
     * redis 127.0.0.1:6379[1]> zrange z 0 -1 withscores
     * 1) "aaa"
     * 2) "1"
     * 3) "bbb"
     * 4) "1"
     * 5) "ccc"
     * 6) "2"
     * 7) "ddd"
     * 8) "3"
     */
    @Test
    public void zadd() {
        jedis.zadd("myzset", 1, "aaa");
        jedis.zadd("myzset", 1.5, "bbb");
        jedis.zadd("myzset", 2, "ccc");
        System.out.println(jedis.zrange("myzset", 0, -1));
    }

    /**
     * Removes the specified members from the sorted set stored at key. Non existing members are ignored.
     * An error is returned when key exists and does not hold a sorted set.
     * Return value
     * Integer reply, specifically:
     * The number of members removed from the sorted set, not including non existing members.
     * History
     * >= 2.4: Accepts multiple elements. In Redis versions older than 2.4 it was possible to remove a single member per call.
     * Examples
     * redis> ZADD myzset 1 "one"
     * (integer) 1
     * redis> ZADD myzset 2 "two"
     * (integer) 1
     * redis> ZADD myzset 3 "three"
     * (integer) 1
     * redis> ZREM myzset "two"
     * (integer) 1
     * redis> ZRANGE myzset 0 -1 WITHSCORES
     * 1) "one"
     * 2) "1"
     * 3) "three"
     * 4) "3"
     * redis>
     */
    @Test
    public void zrem() {
        jedis.zrem("myzset", "aaa");
        System.out.println(jedis.zrange("myzset", 0, -1));

    }

    /**
     * Increments the score of member in the sorted set stored at key by increment. If member does not exist in the sorted set, it is added with increment as its score (as if its previous score was 0.0). If key does not exist, a new sorted set with the specified member as its sole member is created.
     * An error is returned when key exists but does not hold a sorted set.
     * The score value should be the string representation of a numeric value, and accepts double precision floating point numbers. It is possible to provide a negative value to decrement the score.
     * Return value
     * Bulk reply: the new score of member (a double precision floating point number), represented as string.
     * Examples
     * redis> ZADD myzset 1 "one"
     * (integer) 1
     * redis> ZADD myzset 2 "two"
     * (integer) 1
     * redis> ZINCRBY myzset 2 "one"
     * "3"
     * redis> ZRANGE myzset 0 -1 WITHSCORES
     * 1) "two"
     * 2) "2"
     * 3) "one"
     * 4) "3"
     * redis>
     */
    @Test
    public void zincrby() {
        System.out.println(jedis.zrange("myzset", 0, -1));
        jedis.zincrby("myzset", 10, "bbb");
        jedis.zincrby("myzset", 10.1, "asdsad");   //it is add really
        System.out.println(jedis.zrange("myzset", 0, -1));
    }

    /**
     * Available since 2.0.0.
     * Time complexity: O(log(N))
     * Returns the rank of member in the sorted set stored at key, with the scores ordered from low to high. The rank (or index) is 0-based, which means that the member with the lowest score has rank 0.
     * Use ZREVRANK to get the rank of an element with the scores ordered from high to low.
     * Return value
     * If member exists in the sorted set, Integer reply: the rank of member.
     * If member does not exist in the sorted set or key does not exist, Bulk reply: nil.
     * Examples
     * redis> ZADD myzset 1 "one"
     * (integer) 1
     * redis> ZADD myzset 2 "two"
     * (integer) 1
     * redis> ZADD myzset 3 "three"
     * (integer) 1
     * redis> ZRANK myzset "three"
     * (integer) 2
     * redis> ZRANK myzset "four"
     * (nil)
     * redis>
     * zrevrank  is zrank‘s reverse .you can know it  .
     * and zrevrange as zrange ,you will kown it .
     */
    @Test
    public void zrank() {
        System.out.println(jedis.zrange("myzset", 0, -1));
        System.out.println(jedis.zrank("myzset", "asdsad"));
    }

    /**
     * Returns all the elements in the sorted set at key with a score between min and max (including elements with score equal to min or max). The elements are considered to be ordered from low to high scores.
     * The elements having the same score are returned in lexicographical order (this follows from a property of the sorted set implementation in Redis and does not involve further computation).
     * The optional LIMIT argument can be used to only get a range of the matching elements (similar to SELECT LIMIT offset, count in SQL). Keep in mind that if offset is large, the sorted set needs to be traversed for offset elements before getting to the elements to return, which can add up to O(N) time complexity.
     * The optional WITHSCORES argument makes the command return both the element and its score, instead of the element alone. This option is available since Redis 2.0.
     * Exclusive intervals and infinity
     * min and max can be -inf and +inf, so that you are not required to know the highest or lowest score in the sorted set to get all elements from or up to a certain score.
     * By default, the interval specified by min and max is closed (inclusive). It is possible to specify an open interval (exclusive) by prefixing the score with the character (. For example:
     * ZRANGEBYSCORE zset (1 5
     * Will return all elements with 1 < score <= 5 while:
     * ZRANGEBYSCORE zset (5 (10
     * Will return all the elements with 5 < score < 10 (5 and 10 excluded).
     * Return value
     * Multi-bulk reply: list of elements in the specified score range (optionally with their scores).
     * Examples
     * redis> ZADD myzset 1 "one"
     * (integer) 1
     * redis> ZADD myzset 2 "two"
     * (integer) 1
     * redis> ZADD myzset 3 "three"
     * (integer) 1
     * redis> ZRANGEBYSCORE myzset -inf +inf
     * 1) "one"
     * 2) "two"
     * 3) "three"
     * redis> ZRANGEBYSCORE myzset 1 2
     * 1) "one"
     * 2) "two"
     * redis> ZRANGEBYSCORE myzset (1 2
     * 1) "two"
     * redis> ZRANGEBYSCORE myzset (1 (2
     * (empty list or set)
     * redis>
     */
    @Test
    public void zrangebyscore() {

    }

    /**
     * Available since 2.0.0.
     * Time complexity: O(log(N)+M) with N being the number of elements in the sorted set and M being the number of elements between min and max.
     * Returns the number of elements in the sorted set at key with a score between min and max.
     * The min and max arguments have the same semantic as described for ZRANGEBYSCORE.
     * Return value
     * Integer reply: the number of elements in the specified score range.
     * Examples
     * redis> ZADD myzset 1 "one"
     * (integer) 1
     * redis> ZADD myzset 2 "two"
     * (integer) 1
     * redis> ZADD myzset 3 "three"
     * (integer) 1
     * redis> ZCOUNT myzset -inf +inf
     * (integer) 3
     * redis> ZCOUNT myzset (1 3
     * (integer) 2
     * redis>
     */
    @Test
    public void zcount() {

    }

    /**
     * Returns the sorted set cardinality (number of elements) of the sorted set stored at key.
     * Return value
     * Integer reply: the cardinality (number of elements) of the sorted set, or 0 if key does not exist.
     * Examples
     * redis> ZADD myzset 1 "one"
     * (integer) 1
     * redis> ZADD myzset 2 "two"
     * (integer) 1
     * redis> ZCARD myzset
     * (integer) 2
     * redis>
     */
    @Test
    public void zcard() {

    }

    /**
     * Returns the score of member in the sorted set at key.
     * If member does not exist in the sorted set, or key does not exist, nil is returned.
     * Return value
     * Bulk reply: the score of member (a double precision floating point number), represented as string.
     * Examples
     * redis> ZADD myzset 1 "one"
     * (integer) 1
     * redis> ZSCORE myzset "one"
     * "1"
     * redis>
     */
    @Test
    public void zscore() {

    }

}
