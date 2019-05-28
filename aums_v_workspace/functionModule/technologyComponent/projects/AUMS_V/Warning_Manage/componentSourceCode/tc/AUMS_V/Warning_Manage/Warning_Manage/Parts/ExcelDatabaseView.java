package tc.AUMS_V.Warning_Manage.Warning_Manage.Parts;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.svc.javaengine.TCResult;
/**
 * 将文件读取并转成base64返回
 * 
 * @author hanbin
 *
 */
@ComponentGroup(level = "平台", groupName = "Parts")
public class ExcelDatabaseView {
	public static List Excel(List<List<String>> resultSet1,List<String> list){
		List<Object> list1 = new ArrayList<Object>();
		JSONObject json1;
		//Map <String,Object> map=null;
		for(int i=0;i<resultSet1.size();i++){
			//map = new ConcurrentHashMap<String, Object>();
			json1 = new JSONObject();
			for(int  j=0;j<list.size();j++){
				json1.put(list.get(j), resultSet1.get(i).get(j));
			}	
			System.out.println("1");
			list1.add(json1);
		}
		System.out.println(list1);
		return list1;
	}


	/**
	 * @throws IOException
	 * @category excel导出
	 * @param fileName
	 *            入参|导出文件名|{@link java.lang.String}
	 * @param resultSet
	 *            入参|结果集|{@link java.util.List}
	 * @param BASE64Encoder
	 *            出参|Excel转换BASE64Encoder字符串结果|{@link java.lang.String}
	 * @return 1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "resultSet", comment = "结果集", type = java.util.List.class),
			@Param(name = "jsonKey", comment = "jsonKey", type = java.util.List.class)})


	@OutParams(param = { @Param(name = "BASE64Encoder", comment = "Excel转换BASE64Encoder字符串结果", type = java.util.List.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "一般格式通用展示", style = "处理型", type = "同步组件", date = "2018-05-25 11:50:44")
	//这种写法不知道合不合适,但是觉得比较清晰
	public static TCResult A_ExcelPOI(List<List<String>> resultSet1,List<String> list)
	{
		List resultSet = Excel( resultSet1,list);

		return TCResult.newSuccessResult(resultSet);
	}
}