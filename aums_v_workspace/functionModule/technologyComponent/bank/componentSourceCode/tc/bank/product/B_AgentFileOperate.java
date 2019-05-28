package tc.bank.product;

import java.io.File;

import tc.bank.utils.AgentFileUtil;

import com.alibaba.fastjson.JSONObject;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * 包括文件浏览，文件抓取，文件上传或覆盖
 * 
 * @date 2018-05-02 11:24:42
 */
@ComponentGroup(level = "银行", groupName = "代理端文件操作")
public class B_AgentFileOperate {

	/**
	 * @category 列出文件及文件夹
	 * @param basePath
	 *            入参|基础路径（绝对）|{@link java.lang.String}
	 * @param path
	 *            入参|相对路径|{@link java.lang.String}
	 * @param recursion
	 *            入参|是否递归。0-不递归，1-递归|{@link java.lang.String}
	 * @param jsonStr
	 *            出参|文件清单的json字符串|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "basePath", comment = "基础路径（绝对）", type = java.lang.String.class),
			@Param(name = "path", comment = "相对路径", type = java.lang.String.class),
			@Param(name = "recursion", comment = "是否递归。0-不递归，1-递归", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "jsonStr", comment = "文件清单的json字符串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "列出文件及文件夹", style = "判断型", type = "同步组件", comment = "如果入参的路径为空，则列出所有的可见盘符", author = "唐韶东", date = "2018-05-02 11:29:00")
	public static TCResult B_listDir(String basePath,String path, String recursion) {
		JSONObject obj = new JSONObject();
	    if(basePath==null||basePath.equals("")){
			File[] roots=File.listRoots();
			for(int i=0;i<roots.length;i++){
		      System.out.println(roots[i].getPath());
		      JSONObject tmpFile = new JSONObject();
		      tmpFile.put("name", roots[i].getPath());
		      tmpFile.put("isDirectory", true);
		      tmpFile.put("lastModified", roots[i].lastModified());
		      tmpFile.put("relativePath", roots[i].getAbsolutePath());
		      tmpFile.put("size", roots[i].length());
		      obj.put(tmpFile.getString("name"), tmpFile);
			}
		}else{
			obj = AgentFileUtil.listAllDir(basePath,path, recursion);// 初次调用时，path为“./”，ret为{}
		}
		return TCResult.newSuccessResult(obj.toJSONString());
		
	}

}
