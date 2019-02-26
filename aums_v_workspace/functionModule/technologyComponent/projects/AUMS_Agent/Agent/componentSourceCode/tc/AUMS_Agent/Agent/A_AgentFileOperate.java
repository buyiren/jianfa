package tc.AUMS_Agent.Agent;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import tc.AUMS_Agent.Agent.utils.AgentFileUtil;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;

import com.alibaba.fastjson.JSONObject;

/**
 * 包括文件浏览、文件抓取、文件上传或覆盖
 * 
 * @date 2018-05-02 14:13:4
 */
@ComponentGroup(level = "应用", groupName = "代理端文件操作", projectName = "AUMS_Agent", appName = "Agent")
public class A_AgentFileOperate {

	/**
	 * @category 列出文件及文件夹
	 * @param basePath
	 *            入参|基础路径（绝对）|{@link java.lang.String}
	 * @param recursion
	 *            入参|是否递归。0-不递归，1-递归|{@link java.lang.String}
	 * @param jsonStr
	 *            出参|文件清单的json字符串|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "basePath", comment = "基础路径（绝对）", type = java.lang.String.class),
			@Param(name = "recursion", comment = "是否递归。0-不递归，1-递归", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "jsonStr", comment = "文件清单的json字符串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "列出文件及文件夹", style = "判断型", type = "同步组件", comment = "如果入参的路径为空，则列出所有的可见盘符", author = "唐韶东", date = "2018-05-02 11:29:00")
	public static TCResult A_listDir(String basePath, String recursion) {
		JSONObject obj = new JSONObject();
		if (basePath == null || basePath.equals("")) {
			File[] roots = File.listRoots();
			for (int i = 0; i < roots.length; i++) {
				System.out.println(roots[i].getPath());
				JSONObject tmpFile = new JSONObject();
				tmpFile.put("fileName", roots[i].getPath());
				tmpFile.put("isDirectory", true);
				tmpFile.put("lastModified", roots[i].lastModified());
				tmpFile.put("relativePath", roots[i].getAbsolutePath());
				tmpFile.put("size", roots[i].length());
				obj.put(tmpFile.getString("fileName"), tmpFile);
			}
		} else {
			obj = AgentFileUtil.listAllDir(basePath, "", recursion);// 初次调用时，path为“./”，ret为{}
		}
		return TCResult.newSuccessResult(obj.toJSONString());

	}

	/**
	 * @category 读取arouter_agent.conf信息
	 * @param ar_conf
	 *            出参|配置信息|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@OutParams(param = { @Param(name = "ar_conf", comment = "配置信息", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "读取arouter_agent.conf信息", style = "判断型", type = "同步组件", author = "唐韶东", date = "2018-05-05 10:56:32")
	public static TCResult A_getAR_config() {
		try {
			JavaDict ret = new JavaDict();
			InputStream in = new BufferedInputStream(new FileInputStream(
					System.getProperty("afa.home") + "/conf/arouter_agent.conf"));
			Properties p = new Properties();
			p.load(in);
			Iterator keySet = p.keySet().iterator();
			String key=null;
			while(keySet.hasNext()){
				key=(String)keySet.next();
				ret.put((String) key, (String) p.get(key));
			}
			return TCResult.newSuccessResult(ret);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("AUMS010", e);
		} catch (IOException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("AUMS010", e);
		}
	}

}
