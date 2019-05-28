package tc.AUMS_V.Branch_Manage.BranchManage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;

import tc.bank.constant.BusException;
import tc.platform.P_Jdbc;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

/**
 * 导入机构信息
 * 
 * @date 2018-07-04 11:22:5
 */
@ComponentGroup(level = "应用", groupName = "导入机构", projectName = "AUMS_V", appName = "Branch_Manage")
public class A_ImpBranchInfo {
	public static String branchCol[]={"branchNo","branchFatherNo","branchName","branchPhone","branchAddress"};
	/**
	 * @category 导入机构
	 * @param filename
	 *            入参|文件名| {@link String}
	 * @throws BusException
	 */
	@InParams(param = {
			@Param(name = "filename", comment = "文件名", type = java.lang.String.class)})
	@Returns(returns = { @Return(id = "0", desp = "不可用"),
			@Return(id = "1", desp = "可用") })
	@Component(label = "导入机构", style = "判断型", type = "同步组件", comment = "导入", author = "gaoxin", date = "2018-05-07 07:13:02")
	
	public static TCResult ImportBranchInfo(String filename)
			throws BusException {
        JavaList branchList=readBranchFile(filename);
        JavaList branchGetIdList=branchgetId(branchList);
        boolean importFlag=true;
        String deletesql="delete from aums_branchinfo";
        P_Jdbc.executeSQL(null, deletesql, false);
        for(int i=0;i<branchGetIdList.size();i++)
        {
        	JavaDict branchItem=(JavaDict)branchGetIdList.get(i);
			String sql = "INSERT INTO AUMS_BRANCHINFO (BRANCHID, BRANCHNO, BRANCHNAME, FATHERBRANCHID, FATHERFLAG, BRANCHADRESS, BRANCHPHONE) VALUES ('"
					+ branchItem.get("branchId").toString()
					+ "','"
					+ branchItem.get("branchNo").toString()
					+ "','"
					+ branchItem.get("branchName").toString()
					+ "','"
					+ branchItem.get("branchFatherId").toString()
					+ "','"
					+ ""//是否为父节点，暂时为空
					+ "','"
					+ branchItem.get("branchAddress").toString()
					+ "','"
					+ branchItem.get("branchPhone").toString()
					+ "')";
			
			int resultFlag = P_Jdbc.executeSQL(null, sql, false).getStatus();
			AppLogger.info("机构导入：【" + sql + "】 状态为："+resultFlag);
			if(resultFlag!=1)
			{
				P_Jdbc.rollBack(null);
				importFlag=false;
				break;

			}
			
        }
        if(!importFlag)
        {
        	return TCResult.newFailureResult("IMP098","导入失败");

        }
        AppLogger.info("导入成功");
		P_Jdbc.commit(null);
		return new TCResult(1, "000000", "导入成功");
        


	}
	
	//读取机构文件
	public static JavaList readBranchFile(String filename){
		//System.out.print(branchCol);
		File file = new File(filename);
		JavaList branchList=new JavaList();
        if (!file.exists())  
            System.out.println("文件不存在");  
        try {  
            //1.读取Excel的对象  
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));  
            //2.Excel工作薄对象  
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);  
            //3.Excel工作表对象  
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);  
            //总行数  
            int rowLength = hssfSheet.getLastRowNum()+1;  
            //4.得到Excel工作表的行  
            HSSFRow hssfRow = hssfSheet.getRow(0);  
            //总列数  
            int colLength = branchCol.length;
            //得到Excel指定单元格中的内容  
            HSSFCell hssfCell = hssfRow.getCell(0);  
            //得到单元格样式  
            CellStyle cellStyle = hssfCell.getCellStyle();  
  
            for (int i = 1; i < rowLength; i++) {  
                //获取Excel工作表的行  
            	JavaDict branchItem=new JavaDict();
                HSSFRow hssfRow1 = hssfSheet.getRow(i);  
                for (int j = 0; j < colLength; j++) {  
                    //获取指定单元格  
                    HSSFCell hssfCell1 = hssfRow1.getCell(j);  
  
                    //Excel数据Cell有不同的类型，当我们试图从一个数字类型的Cell读取出一个字符串时就有可能报异常：  
                    //Cannot get a STRING value from a NUMERIC cell  
                    //将所有的需要读的Cell表格设置为String格式  
                    //获取每一列中的值  
                    
                    if(hssfCell1!=null)
                    {
                    	hssfCell1.setCellType(1);
                    	branchItem.setItem(branchCol[j], hssfCell1.getStringCellValue());
        				//AppLogger.info("添加======"+branchCol[j]+"===="+hssfCell1.getStringCellValue());
                    }
                    else
                    {
                    	branchItem.setItem(branchCol[j],"");
                    }
                    
                    
                } 
    			branchItem.setItem("branchFatherId","");
                branchList.add(branchItem);
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }

		return branchList;
	}
	
	
	public static JavaList branchgetId(JavaList branchList){
		JavaList branchGetIdList=new JavaList();
		for(int i=0;i<branchList.size();i++)
		{   
			JavaDict branchItem=(JavaDict)branchList.get(i);
			String sql_getId = "select BRANCHID from AUMS_BRANCHINFO where branchno = '" + branchItem.get("branchNo") +"'";
			AppLogger.info("查询机构IDSql:" + sql_getId.toString());
			TCResult dmlSelectBranch = P_Jdbc.dmlSelect(null, sql_getId, -1);
			int dmlSelectResultFlag = dmlSelectBranch.getStatus();
			JavaList getBranchId=(JavaList) dmlSelectBranch.getOutputParams();
			if(dmlSelectResultFlag==1)
			{
				JavaList branchIdDb = (JavaList) getBranchId.get(1);
				branchItem.setItem("branchId", ((JavaList)(branchIdDb.get(0))).get(0));
			}
			else
			{
				branchItem.setItem("branchId", UUID.randomUUID().toString());
			}
			branchGetIdList.add(branchItem);
		}
		for(int i=0;i<branchGetIdList.size();i++)
		{   
			JavaDict branchItem=(JavaDict)branchGetIdList.get(i);
			for(int j=0;j<branchGetIdList.size();j++)
			{   
				if(((JavaDict)branchGetIdList.get(j)).get("branchNo").equals(branchItem.get("branchFatherNo"))){
					branchItem.setItem("branchFatherId",((JavaDict)branchGetIdList.get(j)).get("branchId"));
					branchGetIdList.set(i, branchItem);
					break;
				}
			}
		}
		return branchGetIdList;
	}
}
