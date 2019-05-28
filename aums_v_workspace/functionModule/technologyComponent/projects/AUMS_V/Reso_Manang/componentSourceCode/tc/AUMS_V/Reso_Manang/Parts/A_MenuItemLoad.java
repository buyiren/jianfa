package tc.AUMS_V.Reso_Manang.Parts;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import tc.AUMS_V.Reso_Manang.Util.ExcelExcelParseTool;
import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * 导入交易模板
 * 
 * @date 2018-07-18 17:31:29
 */
@ComponentGroup(level = "应用", groupName = "MenuItemLoad", projectName = "AUMS_V", appName = "Reso_Manang")
public class A_MenuItemLoad {
	
	//static String poolName = "qdzh_oracle";
	static boolean commitFlg = true;

	/**
	 * @category 导入交易Excel模板
	 * @param fileName
	 *            入参|文件名|{@link java.lang.String}
	 * @param fileUrl
	 *            入参|文件路径|{@link java.lang.String}
	 * @return 1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "fileName", comment = "文件名", type = java.lang.String.class),
			@Param(name = "fileUrl", comment = "文件路径", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "导入交易Excel模板", style = "处理型", type = "同步组件", author = "bj", date = "2018-07-18 05:33:47")
	public static TCResult A_tradeExcelLoad(String fileName, String fileUrl) {
		if (fileName == null || "".equals(fileName)) {
			return new TCResult(0, ErrorCode.REMOTE, "请选择您要导入的交易模板表！");
		} else if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			return new TCResult(0, ErrorCode.REMOTE, "请选择正确格式的交易模板表！");
		}
		ExcelExcelParseTool excelParseTool = new ExcelExcelParseTool();
		Workbook workbook = null;
		P_Logger.info("************************* fileName :  " + fileName);
		P_Logger.info("************************* fileUrl :  " + fileUrl);
		try {
			InputStream in = new FileInputStream(fileUrl+fileName);
			workbook = excelParseTool.initWorkBook(in,fileUrl+fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return new TCResult(0, ErrorCode.REMOTE, "导入的交易模板文件不存在！");
		} catch (IOException e) {
			e.printStackTrace();
			return new TCResult(0, ErrorCode.REMOTE, "导入的交易模板异常！");
		}
		List<List<String>> list = null;
		try{
			list = excelParseTool.getExcelData(workbook);
		}catch(Exception e){
			AppLogger.error("获取模板信息异常，【"+e.getMessage()+"】");
		}
		String tradeName = null;
		String tradeCode = null;
		String tradeType = null;
		String icon = null;
		String navigationMode = null;
		String tadPath = null;
		String remark1 = "交易模板导入";
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				List<String> cellList = list.get(i);
				for (int j = 0; j < cellList.size(); j++) {
					if (j == 0) {
						tradeName = cellList.get(j);
						j++;
					}
					if (j == 1) {
						tradeCode = cellList.get(j);
						j++;
					}
					if (j == 2) {
						tradeType = cellList.get(j);
						j++;
					}
					if (j == 3) {
						icon = cellList.get(j);
						j++;
					}
					if (j == 4) {
						navigationMode = cellList.get(j);
						j++;
					}
					if (j == 5) {
						tadPath = cellList.get(j);
						j++;
					}
					String sql = "insert into AUMS_MENU_TRADE (TRADENAME, TRADECODE, TRADETYPE, ICON, NAVIGATIONMODE, TADPATH, ISENABLED, REMARK1) values("
							+ "'"+tradeName+"',"
							+ "'"+tradeCode+"',"
							+ "'"+tradeType+"',"
							+ "'"+icon+"',"
							+ "'"+navigationMode+"',"
							+ "'"+tadPath+"',"
							+ "'0',"
							+ "'"+remark1+"')";
					P_Jdbc.executeSQL(null, sql, commitFlg);
				}
			}
		}
		return TCResult.newSuccessResult();
	}

}
