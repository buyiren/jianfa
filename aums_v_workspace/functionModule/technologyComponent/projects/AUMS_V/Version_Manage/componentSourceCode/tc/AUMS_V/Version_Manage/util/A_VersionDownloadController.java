package tc.AUMS_V.Version_Manage.util;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import redis.clients.jedis.Jedis;
import tc.AUMS_V.Version_Manage.util.depend.RedisConnectionPool;
import tc.platform.P_Jdbc;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import com.alibaba.fastjson.JSONObject;

/**
 * 版本文件下载
 * 
 * @date 2017-09-26 10:42:53
 */

@ComponentGroup(level = "应用", groupName = "版本文件下载")
public class A_VersionDownloadController {

	/**
	 * @category 版本更新下载文件
	 * @param versionID
	 *            入参|设备对应的版本号(技术key)|{@link java.lang.String}
	 * @param filePath
	 *            入参|文件相对路径信息|{@link java.lang.String}
	 * @return
	 */
	@InParams(param = { @Param(name = "versionID", comment = "设备对应的版本号(技术key)", type = java.lang.String.class),
			            @Param(name = "filePath", comment = "文件相对路径信息", type = java.lang.String.class),})
	@OutParams(param = { @Param(name = "result", comment = "返回C端报文", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "版本更新下载文件", comment = "版本更新下载文件", style = "判断型", type = "同步组件", author = "alpha", date = "2018-03-15 02:26:38")
	
	
	public static TCResult getOneDirectoyFile(String versionID, String filePath)
			throws Exception

	{
		AppLogger.info("版本文件下载,获取C端请求数据：filepath：【" + filePath + "】,versionid:【" + versionID + "】");
		// 先从redis取下载路径
		Jedis jedis = RedisConnectionPool.getJedits();
		String verFileDownLoadRootPath = null;
		if (!(jedis == null)) {
			// 20180606 修改：由于在是在afa中存入的redies，所以取出需要substring(1)
			try{
				verFileDownLoadRootPath = jedis.get("verFileDownLoadRootPath").substring(1);
			}catch(Exception e){
				AppLogger.info("Redis获取key内容异常："+e.getLocalizedMessage());
			}
			AppLogger.info("从redis读取成功verFileDownLoadRootPath:" + verFileDownLoadRootPath);
			if (!(jedis == null)) {
				jedis.close();
			}
		}
		JSONObject object = new JSONObject();
		// 如果从redis读取失败，则查询数据库配置的地址
		if (verFileDownLoadRootPath == null) {
			AppLogger.info("从redis读取失败，开始从数据库读取");
			String selectUpackPath = "select PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE='Version_Manage' AND tpi.TRANSCODE='verFileDownLoad' AND tpi.PARAMKEYNAME='rootPath'";
			
			TCResult downLoadPathResutlt = P_Jdbc.dmlSelect(null, selectUpackPath, -1);
			
			if(downLoadPathResutlt==null){
				object.put("result", "");
				object.put("success", false);
				JSONObject obj = new JSONObject();
				obj.put("code", "09");
				obj.put("message", "查询nginx根目录信息异常");
				obj.put("details", "查询nginx根目录信息异常");
				object.put("error", obj);
				return TCResult.newSuccessResult(object.toJSONString());
			}
			
			if(downLoadPathResutlt.getStatus()==2){
				object.put("result", "");
				object.put("success", false);
				JSONObject obj = new JSONObject();
				obj.put("code", "09");
				obj.put("message", "查询nginx根目录信息内容为空");
				obj.put("details", "查询nginx根目录信息内容为空");
				object.put("error", obj);
				return TCResult.newSuccessResult(object.toJSONString());
			}
			JavaList initmsgmapVoUnpackList = (JavaList) downLoadPathResutlt.getOutputParams().get(1);
			verFileDownLoadRootPath = (String) initmsgmapVoUnpackList.getListItem(0).get(0).toString();
		}
		// 下载路径拼接【相对路径头不能包含:/】
		//String realPath = verFileDownLoadRootPath + filePath;
		String realPath = verFileDownLoadRootPath;
		AppLogger.info("下载路径：" + realPath);
		// 返回文件接口信息给C端
		JSONObject result = new JSONObject();
		result.put("fileDownUrl", realPath);
		object.put("result", result);
		object.put("success", true);
		object.put("error", "");
		return TCResult.newSuccessResult(object.toJSONString());

	}
}
