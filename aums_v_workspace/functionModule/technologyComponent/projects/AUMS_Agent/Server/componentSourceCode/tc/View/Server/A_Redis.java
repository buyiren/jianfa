package tc.View.Server;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.agree.afa.jcomponent.CacheOperationImpl;
import cn.com.agree.afa.jcomponent.CacheOperationImpl.CacheOperationException;
import cn.com.agree.afa.svc.javaengine.AppLogger;
//import cn.com.agree.afa.jcomponent.RedisServiceClient;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/**
 * Redis客户端组件
 * 
 * @date 2015-10-29 17:49:56
 */
@ComponentGroup(level = "应用", groupName = "Redis客户端组件", projectName = "AAAA", appName = "server")
public class A_Redis {

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param value
	 *            入参|值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "value", comment = "值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "set", style = "判断型", type = "同步组件", comment = "存储数据到缓存中，若key已存在则覆盖 。value的长度不能超过1073741824 bytes (1 GB)", date = "2015-11-03 04:05:14")
	public static TCResult A_set(String serviceIdentifier, String cacheName,
			String key, String value) {
		// return RedisServiceClient.request(serviceIdentifier, "set", key,
		// value);
		try {
			CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName,
					key, value);
		} catch (CacheOperationException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param value
	 *            出参|值|{@link int}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "value", comment = "值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "get", style = "判断型", type = "同步组件", comment = "从缓存中根据key取得其String类型的值，如果key不存在则返回null，如果key存在但value不是string类型的，则返回一个error。这个方法只能从缓存中取得value为string类型的值。", date = "2015-11-03 04:05:26")
	public static TCResult A_get(String serviceIdentifier, String cacheName,
			String key) {
		// return RedisServiceClient.request(serviceIdentifier, "get", key);
		String ret = (String) CacheOperationImpl.get(serviceIdentifier,
				cacheName, key);
		if (ret == null) {
			return TCResult.newFailureResult("BBBBBBB", "key[" + key + "]不存在");
		}
		AppLogger.info("key:[" + key + "],get 返回:[" + ret + "]");
		return TCResult.newSuccessResult(ret);
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "exists", style = "判断型", type = "同步组件", comment = "检查某个key是否在缓存中存在，如果存在返回true，否则返回false；需要注意的是，即使该key所对应的value是一个空字符串，也依然会返回true", date = "2015-11-03 04:05:39")
	public static TCResult A_exists(String serviceIdentifier, String cacheName,
			String key) {
		// return RedisServiceClient.request(serviceIdentifier, "exists", key);
		boolean ret = CacheOperationImpl.exists(serviceIdentifier, cacheName,
				key);
		if (ret) {
			return TCResult.newSuccessResult();
		} else {
			return TCResult.newFailureResult("BBBBBBB", "key[" + key + "]不存在");
		}

	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "persist(暂未实现)", style = "判断型", type = "同步组件", comment = "如果一个key设置了过期时间，则取消其过期时间，使其永久存在", date = "2015-11-03 04:05:51")
	public static TCResult A_persist(String serviceIdentifier, String key) {
		// return RedisServiceClient.request(serviceIdentifier, "persist", key);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "type(暂未实现)", style = "判断型", type = "同步组件", comment = "返回某个key所存储的数据类型，返回的数据类型有可能是\"none\", \"string\", \"list\", \"set\", \"zset\",\"hash\". \"none\"代表key不存在。", date = "2015-11-03 04:06:02")
	public static TCResult A_type(String serviceIdentifier, String key) {
		// return RedisServiceClient.request(serviceIdentifier, "type", key);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param seconds
	 *            入参|过期时间|{@link int}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "seconds", comment = "过期时间", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "expire", style = "判断型", type = "同步组件", comment = "为key设置一个特定的过期时间，单位为秒。过期时间一到，redis将会从缓存中删除掉该key。在一个有设置过期时间的key上重复设置过期时间将会覆盖原先设置的过期时间。", date = "2015-11-03 04:06:14")
	public static TCResult A_expire(String serviceIdentifier, String cacheName,
			String key, int seconds) {
		// return RedisServiceClient.request(serviceIdentifier, "expire", key,
		// seconds);
		try {
			CacheOperationImpl.expire(serviceIdentifier, cacheName, key,
					seconds);
		} catch (CacheOperationException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB",
					"设置超时时间失败" + e.getMessage());
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "ttl(暂未实现)", style = "判断型", type = "同步组件", comment = "返回一个key还能活多久，单位为秒", date = "2015-11-03 04:06:23")
	public static TCResult A_ttl(String serviceIdentifier, String key) {
		// return RedisServiceClient.request(serviceIdentifier, "ttl", key);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param offset
	 *            入参|起始位置|{@link long}
	 * @param value
	 *            入参|要覆盖的值|{@link java.lang.String}
	 * @param length
	 *            出参|该命令修改后的字符串长度|{@link int}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "offset", comment = "起始位置", type = long.class),
			@Param(name = "value", comment = "要覆盖的值", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "length", comment = "该命令修改后的字符串长度", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "setrange(暂未实现)", style = "判断型", type = "同步组件", comment = "这个命令的作用是覆盖key对应的string的一部分，从指定的offset处开始，覆盖value的长度", date = "2015-11-03 04:06:32")
	public static TCResult A_setrange(String serviceIdentifier, String key,
			long offset, String value) {
		// return RedisServiceClient.request(serviceIdentifier, "setrange", key,
		// offset, value);
		return TCResult.newSuccessResult(0);
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param startOffset
	 *            入参|开始位置|{@link long}
	 * @param endOffset
	 *            入参|结束位置|{@link java.lang.String}
	 * @param subString
	 *            出参|子字符串|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "startOffset", comment = "开始位置", type = long.class),
			@Param(name = "endOffset", comment = "结束位置", type = long.class) })
	@OutParams(param = { @Param(name = "subString", comment = "子字符串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "getrange(暂未实现)", style = "判断型", type = "同步组件", comment = "获得start - end之间的子字符串，若偏移量为负数，代表从末尾开始计算，例如-1代表倒数第一个，-2代表倒数第二个", date = "2015-11-03 04:06:46")
	public static TCResult A_getrange(String serviceIdentifier, String key,
			long startOffset, long endOffset) {
		// return RedisServiceClient.request(serviceIdentifier, "getrange", key,
		// startOffset, endOffset);
		return TCResult.newSuccessResult("");
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param value
	 *            入参|值|{@link java.lang.String}
	 * @param oldValue
	 *            出参|原值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "value", comment = "值", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "oldValue", comment = "原值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "getSet", style = "判断型", type = "同步组件", comment = "自动将key对应到value并且返回原来key对应的value。如果key存在但是对应的value不是字符串，就返回错误", date = "2015-11-03 04:06:58")
	public static TCResult A_getSet(String serviceIdentifier, String cacheName,
			String key, String value) {
		Object oldValue = CacheOperationImpl.get(serviceIdentifier, cacheName,
				key);
		try {
			CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName,
					key, value);
		} catch (CacheOperationException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		}
		if (oldValue == null)
			return TCResult.newSuccessResult("");
		return TCResult.newSuccessResult(oldValue);
		// return RedisServiceClient.request(serviceIdentifier, "getSet", key,
		// value);
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param value
	 *            入参|值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "value", comment = "值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "setnx", style = "判断型", type = "同步组件", comment = "设置key对应的值为string类型的value，如果key已经存在，返回0，nx是not exist的意思", date = "2015-11-03 04:07:09")
	public static TCResult A_setnx(String serviceIdentifier, String cacheName,
			String key, String value) {
		// return RedisServiceClient.request(serviceIdentifier, "setnx", key,
		// value);
		if (CacheOperationImpl.exists(serviceIdentifier, cacheName, key)) {
			return TCResult.newFailureResult("BBBBBBB", "key[" + key + "]已经存在");
		}
		try {
			CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName,
					key, value);
		} catch (CacheOperationException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param seconds
	 *            入参|时间|int
	 * @param value
	 *            入参|值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "seconds", comment = "时间", type = int.class),
			@Param(name = "value", comment = "值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "setex(暂未实现)", style = "判断型", type = "同步组件", comment = "设置key对应的值为string类型的value，并指定此键值对应的有效期，单位秒", author = "Anonymous", date = "2015-11-03 04:04:04")
	public static TCResult A_setex(String serviceIdentifier, String key,
			int seconds, String value) {
		// return RedisServiceClient.request(serviceIdentifier, "setex", key,
		// seconds, value);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param value
	 *            入参|值|{@link int}
	 * @param newValue
	 *            出参|减少后的新值|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "value", comment = "值", type = long.class) })
	@OutParams(param = { @Param(name = "newValue", comment = "减少后的新值", type = long.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "decrBy", style = "判断型", type = "同步组件", comment = "将指定key的值减少某个值", date = "2015-11-03 04:20:05")
	public static TCResult A_decrBy(String serviceIdentifier, String cacheName,
			String key, long value) {
		// return RedisServiceClient.request(serviceIdentifier, "decrBy", key,
		// value);
		Object oldVal = CacheOperationImpl.get(serviceIdentifier, cacheName,
				key);
		if (oldVal == null) {
			return TCResult.newFailureResult("BBBBBBB", "key[" + key + "]不存在");
		}
		long newVal = 0;
		try {
			newVal = Long.parseLong((String) oldVal) - value;
		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", "数值转换失败,oldVal:"
					+ oldVal);
		}
		try {
			CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName,
					key, newVal);
		} catch (CacheOperationException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", "设置新值失败。newVal["
					+ newVal + "],key[" + key + "]");
		}
		return TCResult.newSuccessResult(newVal);

	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "newValue", comment = "减少后的新值", type = long.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "decr", style = "判断型", type = "同步组件", comment = "将指定Key的值减少1", author = "Anonymous", date = "2015-11-09 10:56:14")
	public static TCResult A_decr(String serviceIdentifier, String cacheName,
			String key) {
		// return RedisServiceClient.request(serviceIdentifier, "decr", key);
		Object oldVal = CacheOperationImpl.get(serviceIdentifier, cacheName,
				key);
		if (oldVal == null) {
			return TCResult.newFailureResult("BBBBBBB", "key[" + key + "]不存在");
		}
		long newVal = 0;
		try {
			newVal = Long.parseLong((String) oldVal) - 1;
		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", "数值转换失败,oldVal:"
					+ oldVal);
		}
		try {
			CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName,
					key, newVal);
		} catch (CacheOperationException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", "设置新值失败。newVal["
					+ newVal + "],key[" + key + "]");
		}
		return TCResult.newSuccessResult(newVal);
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param value
	 *            入参|值|int
	 * @param newValue
	 *            出参|增加后的新值|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "value", comment = "值", type = long.class) })
	@OutParams(param = { @Param(name = "newValue", comment = "增加后的新值", type = long.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "incrBy", style = "判断型", type = "同步组件", comment = "将指定的key的值增加指定的值", author = "Anonymous", date = "2015-11-03 04:25:57")
	public static TCResult A_incrBy(String serviceIdentifier, String cacheName,
			String key, long value) {
		// return RedisServiceClient.request(serviceIdentifier, "incrBy", key,
		// value);
		Object oldVal = CacheOperationImpl.get(serviceIdentifier, cacheName,
				key);
		if (oldVal == null) {
			return TCResult.newFailureResult("BBBBBBB", "key[" + key + "]不存在");
		}
		long newVal = 0;
		try {
			newVal = Long.parseLong((String) oldVal) + value;
		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", "数值转换失败,oldVal:"
					+ oldVal);
		}
		try {
			CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName,
					key, newVal);
		} catch (CacheOperationException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", "设置新值失败。newVal["
					+ newVal + "],key[" + key + "]");
		}
		return TCResult.newSuccessResult(newVal);
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param newValue
	 *            出参|增加1后的新值|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "newValue", comment = "增加1后的新值", type = long.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "incr", style = "判断型", type = "同步组件", comment = "将指定的key的值增加1", author = "Anonymous", date = "2015-11-03 04:30:05")
	public static TCResult A_incr(String serviceIdentifier, String cacheName,
			String key) {
		// return RedisServiceClient.request(serviceIdentifier, "incr", key);
		Object oldVal = CacheOperationImpl.get(serviceIdentifier, cacheName,
				key);
		if (oldVal == null) {
			return TCResult.newFailureResult("BBBBBBB", "key[" + key + "]不存在");
		}
		long newVal = 0;
		try {
			newVal = Long.parseLong((String) oldVal) + 1;
		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", "数值转换失败,oldVal:"
					+ oldVal);
		}
		try {
			CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName,
					key, newVal);
		} catch (CacheOperationException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", "设置新值失败。newVal["
					+ newVal + "],key[" + key + "]");
		}
		return TCResult.newSuccessResult(newVal);

	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param value
	 *            入参|值|{@link java.lang.String}
	 * @param newVal
	 *            出参|字符串的总长度|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "value", comment = "值", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "newVal", comment = "字符串的总长度", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "append", style = "判断型", type = "同步组件", comment = "若key存在，将value追加到原有字符串的末尾。若key不存在，则创建一个新的空字符串。", author = "Anonymous", date = "2015-11-03 04:36:37")
	public static TCResult A_append(String serviceIdentifier, String cacheName,
			String key, String value) {
		// return RedisServiceClient.request(serviceIdentifier, "append", key,
		// value);
		Object oldVal = CacheOperationImpl.get(serviceIdentifier, cacheName,
				key);
		if (oldVal == null)
			oldVal = "";
		String newVal = oldVal + value;
		try {
			CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName,
					key, newVal);
		} catch (CacheOperationException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", "设置新值失败。newVal["
					+ newVal + "],key[" + key + "]");
		}
		return TCResult.newSuccessResult(newVal);
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param start
	 *            入参|开始位置|int
	 * @param end
	 *            入参|结束位置|int
	 * @param subStr
	 *            出参|子字符串|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "start", comment = "开始位置", type = int.class),
			@Param(name = "end", comment = "结束位置", type = int.class) })
	@OutParams(param = { @Param(name = "subStr", comment = "子字符串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "substr", style = "判断型", type = "同步组件", comment = "返回start - end 之间的子字符串(start 和 end处的字符也包括在内)", author = "Anonymous", date = "2015-11-03 04:40:59")
	public static TCResult A_substr(String serviceIdentifier, String cacheName,
			String key, int start, int end) {
		// return RedisServiceClient.request(serviceIdentifier, "substr", key,
		// start, end);
		Object oldVal = CacheOperationImpl.get(serviceIdentifier, cacheName,
				key);
		if (oldVal == null) {
			return TCResult.newFailureResult("BBBBBBB", "key[" + key + "]不存在");
		}
		String newVal = ((String) oldVal).substring(start, end);
		return TCResult.newSuccessResult(newVal);
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param field
	 *            入参|字段|{@link java.lang.String}
	 * @param value
	 *            入参|值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "field", comment = "字段", type = java.lang.String.class),
			@Param(name = "value", comment = "值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "hset(暂未实现)", style = "判断型", type = "同步组件", comment = "设置hash表里field字段的值为value。如果key不存在，则创建一个新的hash表", author = "Anonymous", date = "2015-11-03 04:47:24")
	public static TCResult A_hset(String serviceIdentifier, String key,
			String field, String value) {
		// return RedisServiceClient.request(serviceIdentifier, "hset", key,
		// field, value);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param field
	 *            入参|字段|{@link java.lang.String}
	 * @param value
	 *            出参|field的值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "field", comment = "字段", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "value", comment = "field的值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "hget(暂未实现)", style = "判断型", type = "同步组件", comment = "如果该key对应的值是一个Hash表，则返回对应字段的值。 如果不存在该字段，或者key不存在，则返回一个\"nil\"值。", author = "Anonymous", date = "2015-11-03 04:51:53")
	public static TCResult A_hget(String serviceIdentifier, String key,
			String field) {
		// return RedisServiceClient
		// .request(serviceIdentifier, "hget", key, field);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param field
	 *            入参|字段|{@link java.lang.String}
	 * @param value
	 *            入参|值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "field", comment = "字段", type = java.lang.String.class),
			@Param(name = "value", comment = "值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "hsetnx(暂未实现)", style = "判断型", type = "同步组件", comment = "当hash表字段不存在时，才进行set", author = "Anonymous", date = "2015-11-03 04:55:45")
	public static TCResult A_hsetnx(String serviceIdentifier, String key,
			String field, String value) {
		// return RedisServiceClient.request(serviceIdentifier, "hsetnx", key,
		// field, value);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param field
	 *            入参|字段|{@link java.lang.String}
	 * @param value
	 *            入参|值|int
	 * @param newValue
	 *            出参|增加后的新值|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "field", comment = "字段", type = java.lang.String.class),
			@Param(name = "value", comment = "值", type = long.class) })
	@OutParams(param = { @Param(name = "newValue", comment = "增加后的新值", type = long.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "hincrBy(暂未实现)", style = "判断型", type = "同步组件", comment = "对hash中指定字段的值增加指定的值", author = "Anonymous", date = "2015-11-03 05:20:40")
	public static TCResult A_hincrBy(String serviceIdentifier, String key,
			String field, long value) {
		// return RedisServiceClient.request(serviceIdentifier, "hincrBy", key,
		// field, value);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param field
	 *            入参|字段|{@link java.lang.String}
	 * @param exist
	 *            出参|字段是否存在|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "field", comment = "字段", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "exist", comment = "字段是否存在", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "hexists(暂未实现)", style = "判断型", type = "同步组件", comment = "判断hash中指定字段是否存在", author = "Anonymous", date = "2015-11-03 05:25:03")
	public static TCResult A_hexists(String serviceIdentifier, String key,
			String field) {
		// return RedisServiceClient.request(serviceIdentifier, "hexists", key,
		// field);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param length
	 *            出参|哈希集中字段的数量|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "length", comment = "哈希集中字段的数量", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "hlen(暂未实现)", style = "判断型", type = "同步组件", comment = "返回 key 指定的哈希集包含的字段的数量", author = "Anonymous", date = "2015-11-03 05:32:04")
	public static TCResult A_hlen(String serviceIdentifier, String key) {
		// return RedisServiceClient.request(serviceIdentifier, "hlen", key);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param keys
	 *            出参|哈希集中的字段列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "keys", comment = "哈希集中的字段列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "hkeys(暂未实现)", style = "判断型", type = "同步组件", comment = "返回 key 指定的哈希集中所有字段的名字", author = "Anonymous", date = "2015-11-03 05:36:17")
	public static TCResult A_hkeys(String serviceIdentifier, String key) {
		// return RedisServiceClient.request(serviceIdentifier, "hkeys", key);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param keys
	 *            出参|哈希集中的字段列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "values", comment = "哈希集中的字段值列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "hvals(暂未实现)", style = "判断型", type = "同步组件", comment = "哈希集中的值的列表，当 key 指定的哈希集不存在时返回空列表", author = "Anonymous", date = "2015-11-03 05:36:17")
	public static TCResult A_hvals(String serviceIdentifier, String key) {
		// return RedisServiceClient.request(serviceIdentifier, "hvals", key);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param fields
	 *            出参|key 指定的哈希集中所有的字段和值|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "fields", comment = "key 指定的哈希集中所有的字段和值", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "hgetAll(暂未实现)", style = "判断型", type = "同步组件", comment = "返回 key 指定的哈希集中所有的字段和值", author = "Anonymous", date = "2015-11-03 05:44:38")
	public static TCResult A_hgetAll(String serviceIdentifier, String key) {
		// return RedisServiceClient.request(serviceIdentifier, "hgetAll", key);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param length
	 *            出参|key对应的list的长度|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "length", comment = "key对应的list的长度", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "llen(暂未实现)", style = "判断型", type = "同步组件", comment = "返回存储在 key 里的list的长度", author = "Anonymous", date = "2015-11-03 05:56:08")
	public static TCResult A_llen(String serviceIdentifier, String key) {
		// return RedisServiceClient.request(serviceIdentifier, "llen", key);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param start
	 *            入参|开始位置|long
	 * @param end
	 *            入参|结束位置|long
	 * @param subList
	 *            出参|指定范围里的列表元素|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "start", comment = "开始位置", type = long.class),
			@Param(name = "end", comment = "结束位置", type = long.class) })
	@OutParams(param = { @Param(name = "subList", comment = "指定范围里的列表元素", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "lrange(暂未实现)", style = "判断型", type = "同步组件", comment = "返回存储在 key 的列表里指定范围内的元素。", author = "Anonymous", date = "2015-11-03 06:01:45")
	public static TCResult A_lrange(String serviceIdentifier, String key,
			long start, long end) {
		// return RedisServiceClient.request(serviceIdentifier, "lrange", key,
		// start, end);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param start
	 *            入参|开始位置|long
	 * @param end
	 *            入参|结束位置|long
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "start", comment = "开始位置", type = long.class),
			@Param(name = "end", comment = "结束位置", type = long.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "ltrim(暂未实现)", style = "判断型", type = "同步组件", comment = "修剪(trim)一个已存在的 list，这样 list 就会只包含指定范围的指定元素。start 和 stop 都是由0开始计数的， 这里的 0 是列表里的第一个元素（表头），1 是第二个元素，以此类推", author = "Anonymous", date = "2015-11-04 09:52:01")
	public static TCResult A_ltrim(String serviceIdentifier, String key,
			long start, long end) {
		// return RedisServiceClient.request(serviceIdentifier, "ltrim", key,
		// start, end);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param index
	 *            入参|索引下标|long
	 * @param element
	 *            出参|请求的对应元素|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "index", comment = "索引下标", type = long.class) })
	@OutParams(param = { @Param(name = "element", comment = "请求的对应元素", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "lindex(暂未实现)", style = "判断型", type = "同步组件", comment = "返回列表里的元素的索引", author = "Anonymous", date = "2015-11-04 09:58:49")
	public static TCResult A_lindex(String serviceIdentifier, String key,
			long index) {
		// return RedisServiceClient.request(serviceIdentifier, "lindex", key,
		// index);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param index
	 *            入参|下标|long
	 * @param value
	 *            入参|值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "index", comment = "下标", type = long.class),
			@Param(name = "value", comment = "值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "lset(暂未实现)", style = "判断型", type = "同步组件", comment = "设置 index 位置的list元素的值为 value", author = "Anonymous", date = "2015-11-04 10:04:55")
	public static TCResult A_lset(String serviceIdentifier, String key,
			long index, String value) {
		// return RedisServiceClient.request(serviceIdentifier, "lset", key,
		// index, value);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param count
	 *            入参|个数|long
	 * @param value
	 *            入参|值|{@link java.lang.String}
	 * @param number
	 *            出参|返回删除的个数|long
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "count", comment = "个数", type = long.class),
			@Param(name = "value", comment = "值", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "number", comment = "返回删除的个数", type = long.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "lrem(暂未实现)", style = "判断型", type = "同步组件", comment = "从存于 key 的列表里移除前 count 次出现的值为 value 的元素。", author = "Anonymous", date = "2015-11-04 10:12:01")
	public static TCResult A_lrem(String serviceIdentifier, String key,
			long count, String value) {
		// return RedisServiceClient.request(serviceIdentifier, "lrem", key,
		// count, value);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param value
	 *            出参|list的第一个元素|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "value", comment = "list的第一个元素", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "lpop(暂未实现)", style = "判断型", type = "同步组件", comment = "移除并且返回 key 对应的 list 的第一个元素", author = "Anonymous", date = "2015-11-04 10:16:13")
	public static TCResult A_lpop(String serviceIdentifier, String key) {
		// return RedisServiceClient.request(serviceIdentifier, "lpop", key);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param value
	 *            出参|list的第一个元素|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "value", comment = "list的最后一个元素", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "rpop(暂未实现)", style = "判断型", type = "同步组件", comment = "移除并且返回 key 对应的 list 的最后一个元素", author = "Anonymous", date = "2015-11-04 10:16:13")
	public static TCResult A_rpop(String serviceIdentifier, String key) {
		// return RedisServiceClient.request(serviceIdentifier, "rpop", key);
		return TCResult.newSuccessResult();
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param members
	 *            出参|集合中的所有元素|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "members", comment = "集合中的所有元素", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "smembers", style = "判断型", type = "同步组件", comment = "返回key集合所有的元素", author = "Anonymous", date = "2015-11-04 10:34:48")
	public static TCResult A_smembers(String serviceIdentifier,
			String cacheName, String key) {
		// return RedisServiceClient.request(serviceIdentifier, "smembers",
		// key);
		Object val = CacheOperationImpl.get(serviceIdentifier, cacheName, key);
		if (!(val instanceof cn.com.agree.afa.svc.javaengine.context.JavaList)) {
			return TCResult.newFailureResult("BBBBBBB", "key[" + key
					+ "]的类型不是JavaList");
		}
		return TCResult.newSuccessResult((JavaList) val);

	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param member
	 *            出参|被移除的元素|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "member", comment = "被移除的元素", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "spop(暂不实现)", style = "判断型", type = "同步组件", comment = "返回一个随机元素并移除", author = "Anonymous", date = "2015-11-04 10:46:41")
	public static TCResult A_spop(String serviceIdentifier, String cacheName,
			String key) {
		// return RedisServiceClient.request(serviceIdentifier, "spop", key);
		return TCResult.newSuccessResult("");

	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param member
	 *            入参|成员|{@link java.lang.String}
	 * @param length
	 *            出参|添加后的列表长度|long
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "member", comment = "成员", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "length", comment = "添加后的列表长度", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "sadd", style = "判断型", type = "同步组件", comment = "向集合添加成员", author = "Anonymous", date = "2015-11-04 02:25:26")
	public static TCResult A_sadd(String serviceIdentifier, String cacheName,
			String key, String member) {
		// return RedisServiceClient.request(serviceIdentifier, "sadd", key,
		// member);
		AppLogger.info("A_sadd begins......");
		String[] tmp = { member };
		
		long ret = CacheOperationImpl.lpush(serviceIdentifier, cacheName, key, tmp);
		
		return TCResult.newSuccessResult(ret);
		// CacheOperationImpl.lpush(key, tmp);//.lpush(serviceIdentifier,
		// cacheName, key, tmp);
		// try{
		// AppLogger.info("A_sadd begins......2");
		// // CacheOperationImpl.exists(serviceIdentifier, cacheName, key);
		// CacheOperationImpl.get(serviceIdentifier, cacheName, key);
		//
		// }catch(Exception e1){
		// AppLogger.info("A_sadd begins......3");
		// JavaList newVal=new JavaList();
		// newVal.add(member);
		// try{
		// AppLogger.info("putAndReplicate:"+serviceIdentifier+","+cacheName+","+key+","+newVal.toString());
		// CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName, key,
		// newVal);
		// }catch(CacheOperationException e){
		// AppLogger.info("error:"+e.getMessage());
		// e.printStackTrace();
		// return TCResult.newFailureResult("BBBBBBB",
		// "缓存操作失败。"+e.getMessage());
		// }
		// return TCResult.newSuccessResult(0);
		// }
		// AppLogger.info("A_sadd begins......2."+serviceIdentifier+","+cacheName+","+key);
		// Object val=CacheOperationImpl.get(serviceIdentifier, cacheName, key);
		// if(val==null){
		// JavaList newVal=(JavaList)val;
		// newVal.add(member);
		// try{
		// AppLogger.info("A_sadd begins......3");
		// CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName, key,
		// newVal);
		// AppLogger.info("A_sadd begins......4");
		// return TCResult.newSuccessResult(0);
		// }catch(CacheOperationException e){
		// e.printStackTrace();
		// return TCResult.newFailureResult("BBBBBBB",
		// "缓存操作失败。"+e.getMessage());
		// }
		// }
		// if(!(val instanceof
		// cn.com.agree.afa.svc.javaengine.context.JavaList)){
		// return TCResult.newFailureResult("BBBBBBB",
		// "key["+key+"]的类型不是JavaList");
		// }
		// JavaList newVal=(JavaList)val;
		// newVal.add(member);
		// try{
		// CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName, key,
		// newVal);
		// }catch(CacheOperationException e){
		// e.printStackTrace();
		// return TCResult.newFailureResult("BBBBBBB",
		// "缓存操作失败。"+e.getMessage());
		// }
		// return TCResult.newSuccessResult(1);
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param number
	 *            出参|元素的数量|long
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "number", comment = "元素的数量", type = long.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "scard", style = "判断型", type = "同步组件", comment = "返回集合存储的key的基数 (集合元素的数量).如果key不存在，则返回0", author = "Anonymous", date = "2015-11-04 02:20:46")
	public static TCResult A_scard(String serviceIdentifier, String cacheName,
			String key) {
		// return RedisServiceClient.request(serviceIdentifier, "scard", key);
		if (!CacheOperationImpl.exists(serviceIdentifier, cacheName, key)) {
			return TCResult.newSuccessResult(0);
		}
		Object val = CacheOperationImpl.get(serviceIdentifier, cacheName, key);
		if (!(val instanceof cn.com.agree.afa.svc.javaengine.context.JavaList)) {
			return TCResult.newFailureResult("BBBBBBB", "key[" + key
					+ "]的类型不是JavaList");
		}
		return TCResult.newSuccessResult(((JavaList) val).size());
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param member
	 *            入参|成员|{@link java.lang.String}
	 * @param contain
	 *            出参|是否包含|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "member", comment = "成员", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "contain", comment = "是否包含", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "sismember", style = "判断型", type = "同步组件", comment = "返回成员 member 是否是存储的集合 key的成员。0-不是，1-是", author = "Anonymous", date = "2015-11-04 02:25:26")
	public static TCResult A_sismember(String serviceIdentifier,
			String cacheName, String key, String member) {
		// return RedisServiceClient.request(serviceIdentifier, "sismember",
		// key,
		// member);
		if (!CacheOperationImpl.exists(serviceIdentifier, cacheName, key)) {
			return TCResult.newSuccessResult(0);
		}
		Object val = CacheOperationImpl.get(serviceIdentifier, cacheName, key);
		if (!(val instanceof cn.com.agree.afa.svc.javaengine.context.JavaList)) {
			return TCResult.newFailureResult("BBBBBBB", "key[" + key
					+ "]的类型不是JavaList");
		}
		JavaList newVal = (JavaList) val;
		if (newVal.contains(member)) {
			return TCResult.newSuccessResult(1);
		} else {
			return TCResult.newSuccessResult(0);
		}

	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param member
	 *            出参|返回随机的元素|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "member", comment = "返回随机的元素", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "srandmember(暂未实现)", style = "判断型", type = "同步组件", comment = "仅提供key参数,那么随机返回key集合中的一个元素", author = "Anonymous", date = "2015-11-04 02:29:05")
	public static TCResult A_srandmember(String serviceIdentifier, String key) {
		// return RedisServiceClient
		// .request(serviceIdentifier, "srandmember", key);
		return TCResult.newSuccessResult("");
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param score
	 *            入参|分数|float
	 * @param member
	 *            入参|值|{@link java.lang.String}
	 * @param number
	 *            出参|回添加到有序集合中元素的个数|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "score", comment = "分数", type = double.class),
			@Param(name = "member", comment = "值", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "number", comment = "回添加到有序集合中元素的个数", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "zadd(暂未实现)", style = "判断型", type = "同步组件", comment = "添加指定的成员到key对应的有序集合中，每个成员都有一个分数", author = "Anonymous", date = "2015-11-04 02:38:59")
	public static TCResult A_zadd(String serviceIdentifier, String cacheName,
			String key, double score, String member) {
		// return RedisServiceClient.request(serviceIdentifier, "zadd", key,
		// score, member);
		return TCResult.newSuccessResult(0);

	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param start
	 *            入参|开始位置|long
	 * @param end
	 *            入参|结束位置|long
	 * @param members
	 *            出参|指定范围的元素列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "start", comment = "开始位置", type = long.class),
			@Param(name = "end", comment = "结束位置", type = long.class) })
	@OutParams(param = { @Param(name = "members", comment = "指定范围的元素列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "zrange(暂未实现)", style = "判断型", type = "同步组件", comment = "返回有序集key中，指定区间内的成员", author = "Anonymous", date = "2015-11-04 02:49:24")
	public static TCResult A_zrange(String serviceIdentifier, String key,
			long start, long end) {
		// return RedisServiceClient.request(serviceIdentifier, "zrange", key,
		// start, end);
		return TCResult.newSuccessResult(0);
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param score
	 *            入参|分数|float
	 * @param member
	 *            入参|成员|{@link java.lang.String}
	 * @param newScore
	 *            出参|member成员的新score值|long
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "score", comment = "分数", type = double.class),
			@Param(name = "member", comment = "成员", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "newScore", comment = "member成员的新score值", type = long.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "zincrby(暂未实现)", style = "判断型", type = "同步组件", comment = "有序集key的成员member的score值加上增量increment", author = "Anonymous", date = "2015-11-04 02:57:52")
	public static TCResult A_zincrby(String serviceIdentifier, String key,
			double score, String member) {
		// return RedisServiceClient.request(serviceIdentifier, "zincrby", key,
		// score, member);
		return TCResult.newSuccessResult(0);
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param member
	 *            入参|成员|{@link java.lang.String}
	 * @param rank
	 *            出参|返回member的排名的整数|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "member", comment = "成员", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "rank", comment = "返回member的排名的整数", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "zrank(暂未实现)", style = "判断型", type = "同步组件", comment = "返回有序集key中成员member的排名", author = "Anonymous", date = "2015-11-04 03:03:27")
	public static TCResult A_zrank(String serviceIdentifier, String key,
			String member) {
		// return RedisServiceClient.request(serviceIdentifier, "zrank", key,
		// member);
		return TCResult.newSuccessResult(0);
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param member
	 *            入参|成员|{@link java.lang.String}
	 * @param rank
	 *            出参|返回member的排名|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "member", comment = "成员", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "rank", comment = "返回member的排名", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "zrevrank(暂未实现)", style = "判断型", type = "同步组件", comment = "返回有序集key中成员member的排名，其中有序集成员按score值从大到小排列", author = "Anonymous", date = "2015-11-04 03:06:38")
	public static TCResult A_zrevrank(String serviceIdentifier, String key,
			String member) {
		// return RedisServiceClient.request(serviceIdentifier, "zrevrank", key,
		// member);
		return TCResult.newSuccessResult(0);
	}

	/**
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param key
	 *            入参|键|{@link java.lang.String}
	 * @param start
	 *            入参|开始位置|{@link long}
	 * @param end
	 *            入参|结束位置|{@link long}
	 * @param members
	 *            出参|指定范围的元素列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "key", comment = "键", type = java.lang.String.class),
			@Param(name = "start", comment = "开始位置", type = long.class),
			@Param(name = "end", comment = "结束位置", type = long.class) })
	@OutParams(param = { @Param(name = "members", comment = "指定范围的元素列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "zrevrange(暂未实现)", style = "判断型", type = "同步组件", comment = "返回有序集key中，指定区间内的成员,其中成员的位置按score值递减", date = "2015-11-04 03:15:10")
	public static TCResult A_zrevrange(String serviceIdentifier, String key,
			long start, long end) {
		// return RedisServiceClient.request(serviceIdentifier, "zrevrange",
		// key,
		// start, end);
		return TCResult.newSuccessResult(0);
	}

	/**
	 * @category keys
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param pattern
	 *            入参|匹配表达式|{@link java.lang.String}
	 * @param keyList
	 *            出参|key列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "pattern", comment = "匹配表达式", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "keyList", comment = "key列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "keys", style = "判断型", type = "同步组件", comment = "查看当前缓存中有哪些key", author = "Anonymous", date = "2017-05-26 08:50:33")
	public static TCResult A_keys(String serviceIdentifier, String cacheName,
			String pattern) {
		List<String> keyList = CacheOperationImpl.keys(serviceIdentifier,
				cacheName, pattern);
		if (keyList == null)
			return TCResult.newSuccessResult(new JavaList());
		return TCResult.newSuccessResult(new JavaList(keyList));
	}

	/**
	 * @category hmget
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|主键key|{@link java.lang.String}
	 * @param nameList
	 *            入参|属性名list|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param valueList
	 *            出参|属性值list|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "主键key", type = java.lang.String.class),
			@Param(name = "nameList", comment = "属性名list", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "valueList", comment = "属性值list", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "hmget", style = "判断型", type = "同步组件", comment = "调用HMGET命令获取散列哈希值。需要指定hash对象的key名及属性名", author = "Anonymous", date = "2017-06-01 11:19:31")
	public static TCResult A_hmget(String serviceIdentifier, String cacheName,
			String key, JavaList nameList) {
		String[] nameList1 = new String[nameList.size()];
		for (int i = 0; i < nameList.size(); i++) {
			nameList1[i] = (String) nameList.get(i);
		}
		List<String> valueList = CacheOperationImpl.hmgetAsString(
				serviceIdentifier, cacheName, key, nameList1);
		if (valueList == null)
			return TCResult.newSuccessResult(new JavaList());
		return TCResult.newSuccessResult(new JavaList(valueList));
	}

	/**
	 * @category hmset
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|主键key|{@link java.lang.String}
	 * @param nameList
	 *            入参|属性名list|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param valueList
	 *            入参|属性值list|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "主键key", type = java.lang.String.class),
			@Param(name = "nameList", comment = "属性名list", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "valueList", comment = "属性值list", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "hmset", style = "判断型", type = "同步组件", comment = "设置散列哈希值。因为底层服务没有实现hmset接口，故使用多个hset组合实现此功能", author = "Anonymous", date = "2017-06-01 11:35:08")
	public static TCResult A_hmset(String serviceIdentifier, String cacheName,
			String key, JavaList nameList, JavaList valueList) {
		try {
			for (int i = 0; i < nameList.size(); i++) {
				CacheOperationImpl.hSet(serviceIdentifier, cacheName, key,
						(String) nameList.get(i), (String) valueList.get(i));
			}
		} catch (CacheOperationException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category hdel
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param key
	 *            入参|删除的key列表|{@link java.lang.String}
	 * @param nameList
	 *            入参|删除的属性列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "key", comment = "主键key", type = java.lang.String.class),
			@Param(name = "nameList", comment = "删除的属性列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "hdel", style = "判断型", type = "同步组件", comment = "调用hdel命令删除散列哈希的属性", author = "Anonymous", date = "2017-06-01 11:40:32")
	public static TCResult A_hdel(String serviceIdentifier, String cacheName,
			String key, JavaList nameList) {
		String[] names = new String[nameList.size()];
		for (int i = 0; i < nameList.size(); i++) {
			names[i] = nameList.getStringItem(i);
		}
		try {
			CacheOperationImpl.hDel(serviceIdentifier, cacheName, key, names);
		} catch (CacheOperationException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category del
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param pattern
	 *            入参|删除的key的pattern表达式|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "pattern", comment = "删除的key的pattern表达式", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "del", style = "判断型", type = "同步组件", comment = "调用del命令删除某主键", author = "Anonymous", date = "2017-06-01 11:40:32")
	public static TCResult A_del(String serviceIdentifier, String cacheName,
			String pattern) {
		CacheOperationImpl.remove(serviceIdentifier, cacheName, pattern);
		return TCResult.newSuccessResult();
	}
	
	/**
	 * @category mdel
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param pattern
	 *            入参|删除的key的pattern表达式|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "pattern", comment = "删除的key的pattern表达式", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "mdel", style = "判断型", type = "同步组件", comment = "调用del命令删除某些主键,可包含*号", author = "Anonymous", date = "2017-06-01 11:40:32")
	public static TCResult A_mdel(String serviceIdentifier, String cacheName,
			String pattern) {
		List<String> keyList = CacheOperationImpl.keys(serviceIdentifier,
				cacheName, pattern);
		if (keyList.size()==0){
			return TCResult.newSuccessResult();
		}
		for (int i = 0; i < keyList.size(); i++) {
			String param = keyList.get(i);
			CacheOperationImpl.remove(serviceIdentifier, cacheName, param);
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category MD5_MD5信息写入缓存
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param md5Dict
	 *            入参|文件信息字典|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param keyName
	 *            入参|缓存主键|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "md5Dict", comment = "文件信息字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "keyName", comment = "缓存主键", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "MD5_MD5信息写入缓存", style = "判断型", type = "同步组件", comment = "将当前基准版本的文件MD5写入redis缓存", author = "Anonymous", date = "2017-06-13 09:44:12")
	public static TCResult A_writeMD5ToRedis(String serviceIdentifier,
			String cacheName, JavaDict md5Dict, String keyName) {
		StringBuffer value = new StringBuffer();
		for (Object key : md5Dict.keySet().toArray()) {
			value.append((String) key);
			value.append(":");
			value.append((String) md5Dict.get(key));
			value.append("|");
		}
		try {
			CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName,
					keyName, value.toString());
//			CacheOperationImpl.expire(serviceIdentifier, cacheName, keyName,
//					3660);//有效期为1小时零一分钟
			
		} catch (CacheOperationException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category MD5_从缓存读出MD5信息
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param keyName
	 *            入参|缓存主键|{@link java.lang.String}
	 * @param md5Dict
	 *            出参|文件信息字典|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "keyName", comment = "缓存主键", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "md5Dict", comment = "文件信息字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "MD5_从缓存读出MD5信息", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-06-13 10:07:19")
	public static TCResult A_readMD5FromRedis(String serviceIdentifier,
			String cacheName, String keyName) {
		String value = (String)CacheOperationImpl.get(serviceIdentifier, cacheName, keyName);
		if (value == null) {
			return TCResult.newFailureResult("BBBBBBB", "key[" + keyName + "]不存在");
		}
		JavaDict ret = new JavaDict();
		String[] pairs = value.split("\\|");
		AppLogger.info("pairs size:"+pairs.length+",content:"+pairs.toString());
		for(int i=0;i<pairs.length;i++){
			String[] tmp = pairs[i].split(":");
			ret.put(tmp[0], tmp[1]);
		}
		return TCResult.newSuccessResult(ret);
	}
	/**
	 * @category 查询当前心跳信息
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param agentDict
	 *            出参|心跳信息列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "agentDict", comment = "心跳信息列表", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "查询当前心跳信息", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-06-13 10:07:19")
	public static TCResult A_readHeartBeatInfo(String serviceIdentifier,
			String cacheName) {
		JavaDict ret = new JavaDict();
		List<String> keys = CacheOperationImpl.keys(serviceIdentifier, cacheName, "abc_heartbeating_*_");
		String tmpKey = null;
		Pattern p=Pattern.compile("abc_heartbeating_\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}_");
		Matcher m=null;
		Pattern p_ip=Pattern.compile("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}");
		Matcher m_ip=null;
		int k=0;
		JSONObject jsonObj;
		JavaDict jsonDict;
		for(int i=0;i<keys.size();i++){
			tmpKey = keys.get(i);
			m=p.matcher(tmpKey);
			if(m.matches()){
				try{
					m_ip=p_ip.matcher(tmpKey);
					if(!m_ip.find()){
						AppLogger.info("未匹配到ip地址["+tmpKey+"]");
						continue;
					}
					jsonObj = (JSONObject) JSON.parseObject((String)CacheOperationImpl.get(serviceIdentifier, cacheName, tmpKey));
					jsonDict = getJavaDict(jsonObj);
					ret.put(m_ip.group(0), jsonDict);
				}catch(Exception e){
					e.printStackTrace();
					AppLogger.info("读取该心跳数据出错了["+tmpKey+"]:"+e.getMessage());
				}
			}
		}
		return TCResult.newSuccessResult(ret);
	}
	private static JavaDict getJavaDict(JSONObject jsonObj) {
		JavaDict dict = new JavaDict();

		for (@SuppressWarnings("rawtypes")
		Map.Entry entry : jsonObj.entrySet()) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (value == null) {
				dict.setItem(key, null);
				continue;
			}
			if (value.getClass() == JSONArray.class) {
				dict.setItem(key, getJavaList((JSONArray) value));
			} else if (value.getClass() == JSONObject.class) {
				dict.setItem(key, getJavaDict((JSONObject) value));
			} else {
				dict.setItem(key, value);
			}
		}
		return dict;
	}
	private static JavaList getJavaList(JSONArray seq) {
		JavaList list = new JavaList();
		for (int i = 0; i < seq.size(); i++) {
			Object value = seq.get(i);
			if (value == null) {
				list.add(null);
				continue;
			}
			if (value.getClass() == JSONArray.class) {
				list.add(getJavaList((JSONArray) value));
			} else if (value.getClass() == JSONObject.class) {
				list.add(getJavaDict((JSONObject) value));
			} else {
				list.add(value);
			}
		}
		return list;
	}

}
