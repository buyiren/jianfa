package tc.AUMS_V.ReportForms_Manage.excel;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONObject;

import sun.misc.BASE64Encoder;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
/**
 * 将文件读取并转成base64返回
 * 
 * @author hanbin
 *
 */
@ComponentGroup(level = "平台", groupName = "导出excel组件")
public class ExcelDatabaseV_Query {

	public static String Excel(String fileName, List<JavaDict> resultSetJson,String order ,String title,String column,String starttime,String endtime) throws IOException {
		
		String[] ordersplit = order.split(",");
		List list = null;
		List <List<Object>>resultSet= new ArrayList();
		for(int i=0;i<resultSetJson.size();i++){
			list = new ArrayList();
			for(int j=0;j<ordersplit.length;j++){
					 
					list.add((resultSetJson.get(i).get(ordersplit[j])));
						
			}
			resultSet.add(list);
			
		}

		
		SimpleDateFormat sdf;
		SimpleDateFormat sdf1;
		Date starttimedate = null;
		Date endtimedate = null;
		 String starttimedate1 = null;
		 String endtimedate1 = null;
		FileOutputStream out = null ;
		FileInputStream inputFile=null;
	
		try {
			sdf = new SimpleDateFormat("yyyyMMdd");
			sdf1 = new SimpleDateFormat("yyyy'年'MM'月'dd'日'");
			
			
			starttimedate = sdf.parse(starttime);
	        endtimedate = sdf.parse(endtime);
	         starttimedate1 = sdf1.format(starttimedate);
	         endtimedate1 = sdf1.format(endtimedate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		


		//连接oracle驱动,不使用连接池
		//        Class.forName("oracle.jdbc.driver.OracleDriver");
		//        //创建连接
		//        Connection connect = DriverManager.getConnection(
		//                "jdbc:oracle:thin:@192.9.200.226:1521:pcva" ,
		//                "aums" ,
		//                "aums"
		//        );
		//读取配置文件
		//			Properties cfg=new Properties();
		//			InputStream inStream=DBUtils.class.getClassLoader().getResourceAsStream("sql.properties");
		//			cfg.load(inStream);
		//			sql=cfg.getProperty("sql");
		//Connection conn=null;
		//通过连接池获取连接
		//conn=DBUtils.getConnection();
		//Statement statement = conn.createStatement();
		//读取结果
		//创建工作簿
		XSSFWorkbook workbook = new XSSFWorkbook();
		//创建工作表  工作表的名字叫sheet
		XSSFSheet spreadsheet = workbook.createSheet("sheet");
		for (int i=0;i<=20;i++){
			spreadsheet.autoSizeColumn((short)i); //调整第i列宽度
		}
		XSSFRow row;
		XSSFCell cell;
		row = spreadsheet.createRow(0);
		cell = row.createCell(0);
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		cell = row.createCell(0);
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		spreadsheet.addMergedRegion(new CellRangeAddress(0, 0, 0, resultSet.get(0).size()-1));
		//----------------标题样式---------------------
		Font ztFont = workbook.createFont(); 
		ztFont.setFontHeightInPoints((short)18);   // 将字体大小设置为18px   
		ztFont.setFontName("宋体");             // 将“宋体”字体应用到当前单元格上  
		ztFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    //加粗
		//创建样式
		XSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		//将标题赋予样式,将样式赋予字体
		cell.setCellStyle(titleStyle);
		titleStyle.setFont(ztFont);

		cell.setCellValue(title);


		//时间样式
		XSSFCellStyle sjStyle = workbook.createCellStyle();
		row = spreadsheet.createRow(1);
		cell = row.createCell(0);
		spreadsheet.addMergedRegion(new CellRangeAddress(1, 1, 0, resultSet.get(0).size()-1));
		Font sjFont = workbook.createFont();
		sjFont.setFontHeightInPoints((short)8);
		sjStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//靠右
		sjStyle.setFont(sjFont);
		cell.setCellStyle(sjStyle);
        if (starttime.equals("")) {
        	cell.setCellValue("时间未输入,默认查询所有数据");
		}else{
			cell.setCellValue("起始时间 :"+starttimedate1+"      "+"结束时间 :"+endtimedate1);
		}
		



		//正文
		row=spreadsheet.createRow(2);
		//标题需要自己设置
		String[] a = column.split(",");
		for(int i=0;i<a.length;i++) {

			cell = row.createCell(i);
			cell.setCellValue(a[i]);
		}

		//获取列名 resultSet数据下标从1开始
		//ResultSetMetaData metaData = resultSet.getMetaData();
		//metaData有关整个数据库的信息：表名、表的索引、数据库产品的名称和版本、数据库支持的操作。 
		//获取并创建列标题放入Cell的第一列,数据库原标题展示(需要的几率低)
		//        for (int i = 0; i < metaData.getColumnCount(); i++) {
		//            int index=i+1;
		//            String columnName = metaData.getColumnName(index);
		//            System.out.println(columnName + "\t");
		//            cell=row.createCell(i);
		//            cell.getCellStyle().setWrapText(true);
		//            cell.setCellValue(columnName);
		//        }
		//从第四行开始,获取数据信息到Cell中,第二行已被列标题占用,第一行是报表主题，第三行是时间
		int i=3;
		//通过连接池获取结果集创建表格,需求变更,此方暂时不用
		//			while(resultSet.next())
		//			{
		//				row=spreadsheet.createRow(i);
		//				for (int j = 0; j< metaData.getColumnCount(); j++) {
		//					int index=j+1;
		//					cell=row.createCell(j);
		//					//表格样式
		//					cell.setCellValue(resultSet.getString(index));
		//				}
		//				i++;
		//			}
		//需求变更为通过jdbc组件获取结果集放入List中
		//定义行列起始位置,后期如果有复杂格式可以自行调整
		int startRowNum = 1;
		int startColNum = 1;
		int currentRowNum = startRowNum-1;//从第几行开始写
		int currentColNum = startColNum-1;//从第几列开始写
		for (List<Object> str: resultSet) {
			System.out.println(str);
			row = spreadsheet.createRow(i);
			for (Object str1 : str) {//遍历每一个list的Object[]数组
				if (str1==null) {
					cell = row.createCell(currentColNum++, XSSFCell.CELL_TYPE_BLANK);//如果该单元格为空，则创建一个空格子，不然数据会移位
					cell.setCellValue("");//将这个空格设置为空字符串
				}else {
					cell = row.createCell(currentColNum++, XSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(str1.toString());		 
				}
			}
			currentRowNum++;//写完第一个list，跳到下一行
			//从第一列开始
			currentColNum = startColNum-1;
			//换到下一行
			i++;
		}
		//将文件写出
		File file =null;
		try {
			//目录为/home/afa4sj/AFA4J_2.7.1_1207/hanbintemp
			//file = new File("/home/afa4sj/AFA4J_2.7.1_1207/share/excel"+fileName+".xlsx");
			file = new File(file.separator+"home"+file.separator+"afa4sj"+file.separator+"AFA4J_2.7.1_1207"+file.separator+"share"+file.separator+"excel"+file.separator+fileName+".xlsx");
			
			out = new FileOutputStream(file);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			out.close();
		}


		System.out.println("hanbintemp"+file.separator+fileName+".xlsx written successfully");

		return ("http://192.9.200.225:8023/excel/"+fileName+".xlsx");	







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
			@Param(name = "fileName", comment = "导出文件名", type = java.lang.String.class),
			@Param(name = "resultSet", comment = "结果集", type = java.util.List.class),
			@Param(name = "order", comment = "列顺序", type = java.lang.String.class),
			@Param(name = "title", comment = "列名称", type = java.lang.String.class),
			@Param(name = "column", comment = "标题", type = java.lang.String.class),
			@Param(name = "starttime", comment = "起始时间", type = java.lang.String.class),
			@Param(name = "endtime", comment = "结束时间", type = java.lang.String.class)})
	@OutParams(param = { @Param(name = "BASE64Encoder", comment = "Excel转换BASE64Encoder字符串结果", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "excel一般格式通用导出V_Query", style = "处理型", type = "同步组件", date = "2018-05-16 11:50:44")
	//这种写法不知道合不合适,但是觉得比较清晰
	public static TCResult A_ExcelPOI(String fileName1, List resultSet,String order,String title,String column,String starttime,String endtime)
			throws IOException {
		String fileName = Excel(fileName1, resultSet,order,title,column,starttime,endtime);
		return TCResult.newSuccessResult(fileName);
	}
}