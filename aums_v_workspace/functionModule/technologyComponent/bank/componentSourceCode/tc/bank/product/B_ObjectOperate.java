package tc.bank.product;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;


import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 对象操作类
 * 
 * @date 2018-04-24 13:45:16
 */
@ComponentGroup(level = "银行", groupName = "对象操作类")
public class B_ObjectOperate {


	@InParams(param = {
			@Param(name = "oriList", comment = "需要排序树list", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "keyList", comment = "原字典keylist,第一位必须是序号，第二位必须是父亲序号，父亲序号可为\"\"", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "newKeyList", comment = "生成后的字典key列表，最后一个是child节点名", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "treeList", comment = "树形list", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "创建树list", style = "判断型", type = "同步组件", comment = "创建树list", date = "Thu Jul 02 11:48:56 CST 2015")
	public static TCResult B_TreeList_Create(JavaList oriList,
			JavaList keyList, JavaList newKeyList) {
    
		    JavaDict rootDict = createnode(keyList,(JavaDict)oriList.get(0),newKeyList);
		    AppLogger.info("oriList----:" + oriList.toString());
		    createtree(rootDict,keyList,oriList,newKeyList);
		    JavaList treeList = new JavaList();
		    treeList.add(rootDict);
		    AppLogger.info("树形结构----:" + treeList.toString());
		return TCResult.newSuccessResult(treeList);
	}

	
	
	private static void createtree(JavaDict rootDict,JavaList keyList, JavaList oriList,
			JavaList newKeyList) {
		    String rootid=(String)rootDict.get(newKeyList.get(0));
            for(int i =0;i<oriList.size();i++)
            {

            	JavaDict childldict=(JavaDict)oriList.get(i);
            	String childid=(String)(childldict.get(keyList.get(1)));
            	//AppLogger.info("if----i:"+rootid+"-----"+childid);
            	if(childid!=null&&childid.equals(rootid)){
                	JavaDict childnode=createnode(keyList,(JavaDict)oriList.get(i),newKeyList);
            		JavaList rootchild = new JavaList();
                	if(rootDict.hasKey(newKeyList.get(newKeyList.size() - 1))){
                		rootchild=(JavaList)rootDict.get(newKeyList.get(newKeyList.size() - 1));
                		
                	}
                	rootchild.add(childnode);
            		rootDict.setItem(newKeyList.get(newKeyList.size() - 1), rootchild);
            		createtree(childnode,keyList,oriList,
            				newKeyList);
                	
            	}

            }

	}


	private static JavaDict createnode(JavaList keyList, JavaDict oriDict,
			JavaList newKeyList) {
		JavaDict nodedict = new JavaDict();
		for (int i = 0; i < newKeyList.size() - 1; i++) {
			nodedict.setItem(newKeyList.get(i),
					oriDict.get(keyList.get(i)));
			AppLogger.info("ori:"+newKeyList.get(i)+"   new-----"+oriDict.get(keyList.get(i)));
		}
		return nodedict;

	}
	
	

	/**
	 * @category SHA256加密
	 * @param origString
	 *            入参|字符串|{@link java.lang.String}
	 * @param SHA256String
	 *            出参|字节数组|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "origString", comment = "字符串", type = java.lang.String.class)
			 })
	@OutParams(param = { @Param(name = "SHA256String", comment = "加密后字符串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "SHA256加密", style = "判断型", type = "同步组件", comment = "SHA256加密", author = "Anonymous", date = "2015-12-11 07:13:02")
	public static TCResult getSHA256StrJava(String origString) {
		MessageDigest messageDigest;
		String encodeStr = "";
		try {
		messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(origString.getBytes("UTF-8"));
		encodeStr = byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			return TCResult.newFailureResult(ErrorCode.AGR,
					"加密错误");
		} catch (UnsupportedEncodingException e) {
			return TCResult.newFailureResult(ErrorCode.AGR,
					"加密错误");
		}
		return TCResult.newSuccessResult(encodeStr);
	}
	
	
	private static String byte2Hex(byte[] bytes){
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i=0;i<bytes.length;i++){
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length()==1){
					//1得到一位的进行补0操作
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}
}
