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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;





import cn.com.agree.afa.svc.javaengine.TCResult;
/**
 * 将文件读取并转成base64返回
 * 
 * @author hanbin
 *
 */
@ComponentGroup(level = "平台", groupName = "导出excel组件")
public class ExcelDatabasePayment {
	public static String Excel(String fileName, List<List<String>> resultSet,String title,String starttime,String endtime) throws IOException {
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
		XSSFWorkbook workbook = new XSSFWorkbook();
		//创建工作表  工作表的名字叫sheet
		XSSFSheet spreadsheet = workbook.createSheet("sheet");
		for (int i=0;i<=10;i++){
			spreadsheet.autoSizeColumn((short)i); //调整第i列宽度
		}
		XSSFRow row;
		XSSFCell cell;
		row = spreadsheet.createRow(0);
		
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		cell = row.createCell(1);
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		spreadsheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 11));
		//----------------标题样式---------------------
		XSSFFont ztFont = workbook.createFont(); 
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
		XSSFCell cell2;
		XSSFCellStyle sjStyle = workbook.createCellStyle();
		row = spreadsheet.createRow(1);
		cell2 = row.createCell(1);
		spreadsheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 11));
		XSSFFont sjFont = workbook.createFont();	
		sjFont.setFontHeightInPoints((short)8);
		sjStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//靠右
		sjStyle.setFont(sjFont);
		cell2.setCellStyle(sjStyle);
		if (starttime.equals("")) {
			cell2.setCellValue("时间未输入,默认查询所有数据");
		}else{
			cell2.setCellValue("起始时间 :"+starttimedate1+"      "+"结束时间 :"+endtimedate1);
		}
		//正文

		//正文样式
		XSSFCell cell3 ;
		XSSFRow row3;
		row3 = spreadsheet.createRow(2);

		XSSFFont ColumnFont = workbook.createFont();
		ColumnFont.setFontHeightInPoints((short)8);
		ColumnFont.setFontHeightInPoints((short)11);//设置字号
		ColumnFont.setFontName("宋体");//设置字体
		XSSFCellStyle ColumnStyle = workbook.createCellStyle();
		ColumnStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
		ColumnStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平  
		ColumnStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		ColumnStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		ColumnStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		ColumnStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		ColumnStyle.setFont(ColumnFont);
		cell3 = row3.createCell(1);
		spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("机构");
		cell3 = row3.createCell(2);
		spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 3));
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("存款");
		
		cell3 = row3.createCell(3);
		cell3.setCellStyle(ColumnStyle);
		
		
		
		cell3 = row3.createCell(4);
		spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 4, 5));
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("取款");
		
		cell3 = row3.createCell(5);
		cell3.setCellStyle(ColumnStyle);
		
		
		
		
		cell3 = row3.createCell(6);
		spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 7));
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("签约类");
		
		cell3 = row3.createCell(7);
		cell3.setCellStyle(ColumnStyle);
		
		
		
		
		
		cell3 = row3.createCell(8);
		spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 8, 9));
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("维护信息类");
		
		cell3 = row3.createCell(9);
		cell3.setCellStyle(ColumnStyle);
		
		
		
		
		
		cell3 = row3.createCell(10);
		spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 10, 11));
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("其他");
		
		

		cell3 = row3.createCell(11);
		cell3.setCellStyle(ColumnStyle);
		
		row3 = spreadsheet.createRow(3);  
		cell3 = row3.createCell(1);
		cell3.setCellStyle(ColumnStyle);
		
		
		
		cell3 = row3.createCell(2);
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("数量");

		cell3 = row3.createCell(3);
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("金额");

		cell3 = row3.createCell(4);
		
		

		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("数量");

		cell3 = row3.createCell(5);
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("金额");

		cell3 = row3.createCell(6);
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("数量");

		cell3 = row3.createCell(7);
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("金额");

		cell3 = row3.createCell(8);
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("数量");

		cell3 = row3.createCell(9);
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("金额");

		cell3 = row3.createCell(10);
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("数量");

		cell3 = row3.createCell(11);
		cell3.setCellStyle(ColumnStyle);
		cell3.setCellValue("金额");
		//从第五行开始,获取数据信息到Cell中,第二行已被列标题占用,第一行是报表主题，第三行是时间,第四行是
		int i=4;
		int startRowNum = 1;
		int startColNum = 1;
		int currentRowNum = startRowNum-1;//从第几行开始写
		int currentColNum = startColNum;//从第几列开始写
		XSSFCell cell4 = null;
		XSSFRow row4 = null;
		XSSFFont zwFont = workbook.createFont();
		zwFont.setFontHeightInPoints((short)8);
		zwFont.setFontHeightInPoints((short)11);//设置字号
		zwFont.setFontName("宋体");//设置字体
		XSSFCellStyle zwStyle = workbook.createCellStyle();
		zwStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
		zwStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平  
		zwStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		zwStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		zwStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		zwStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		zwStyle.setFont(zwFont);
		XSSFCell cell6;
		
		for (List<String> str: resultSet) {
			System.out.println(str);
			row4 = spreadsheet.createRow(i);
			for (int j=0;j<=10;j++) {
				//遍历每一个list的Object[]数组
				if (str.get(j)==null) {
					cell4 = row4.createCell(currentColNum++, XSSFCell.CELL_TYPE_BLANK);//如果该单元格为空，则创建一个空格子，不然数据会移位
					cell4.setCellStyle(zwStyle);
					cell4.setCellValue("");//将这个空格设置为空字符串
				}else if(j%2==0) {
					XSSFCellStyle cwStyle = workbook.createCellStyle();
					XSSFDataFormat format= workbook.createDataFormat();
					cwStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
					cwStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平  
					cwStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
					cwStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					cwStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					cwStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
					cwStyle.setDataFormat(format.getFormat("_ * #,##0.00_ ;_ * -#,##0.00_ ;_ * \"-\"??_ ;_ @_ "));
					cell6 = row4.createCell(currentColNum++, XSSFCell.CELL_TYPE_NUMERIC);
					cell6.setCellStyle(cwStyle);
					cell6.setCellValue(str.get(j));	
				}else{
					cell4 = row4.createCell(currentColNum++, XSSFCell.CELL_TYPE_NUMERIC);
					cell4.setCellStyle(zwStyle);
					cell4.setCellValue(str.get(j));		 
				}
			}
			currentRowNum++;//写完第一个list，跳到下一行
			//从第一列开始
			currentColNum = startColNum;
			//换到下一行
			i++;
		}
		//合计行
		XSSFCell cell5 = null;
		XSSFRow row5 = null;
		
		row5 = spreadsheet.createRow(i);
		cell5 = row5.createCell(1);
		cell5.setCellValue("合计");
		for (List<String> str: resultSet) {
			currentColNum=2;
			for (int j=11;j<=20;j++) {
				XSSFFont hjFont = workbook.createFont();
				hjFont.setFontHeightInPoints((short)8);
				hjFont.setFontHeightInPoints((short)11);//设置字号
				hjFont.setFontName("宋体");//设置字体
				XSSFCellStyle hjStyle = workbook.createCellStyle();
				hjStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
				hjStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平  
				hjStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
				hjStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				hjStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				hjStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
				hjStyle.setFont(hjFont);
				cell5.setCellStyle(hjStyle);
				//遍历每一个list的Object[]数组
				if (str.get(j)==null) {
					cell5 = row5.createCell(currentColNum++, XSSFCell.CELL_TYPE_BLANK);//如果该单元格为空，则创建一个空格子，不然数据会移位
					cell5.setCellValue("");//将这个空格设置为空字符串
				}else if(j%2==0){
						XSSFCellStyle cwStyle = workbook.createCellStyle();
						XSSFDataFormat format= workbook.createDataFormat();
						cwStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
						cwStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平  
						cwStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
						cwStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
						cwStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
						cwStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
						cwStyle.setDataFormat(format.getFormat("_ * #,##0.00_ ;_ * -#,##0.00_ ;_ * \"-\"??_ ;_ @_ "));
						cell6 = row5.createCell(currentColNum++, XSSFCell.CELL_TYPE_NUMERIC);
						cell6.setCellStyle(cwStyle);
						cell6.setCellValue(str.get(j));	
						
					}else{
					
					cell5 = row5.createCell(currentColNum++, XSSFCell.CELL_TYPE_NUMERIC);
					cell5.setCellValue(str.get(j));		 
				}
			}
			currentRowNum++;//写完第一个list，跳到下一行
			//从第一列开始
			currentColNum = startColNum;
			//换到下一行
			i++;
			break;
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
		return  ("http://192.9.200.225:8023/excel/"+fileName+".xlsx");	
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
			@Param(name = "title", comment = "标题", type = java.lang.String.class),
			@Param(name = "starttime", comment = "起始时间", type = java.lang.String.class),
			@Param(name = "endtime", comment = "结束时间", type = java.lang.String.class)})
	@OutParams(param = { @Param(name = "BASE64Encoder", comment = "Excel转换BASE64Encoder字符串结果", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "excel交易信息统计表导出", style = "处理型", type = "同步组件", date = "2018-05-16 11:50:44")
	//这种写法不知道合不合适,但是觉得比较清晰
	public static TCResult A_ExcelPOI(String fileName1, List resultSet,String titel,String starttime,String endtime)
			throws IOException {
		String fileName = Excel(fileName1, resultSet,titel,starttime,endtime);
		return TCResult.newSuccessResult(fileName);
	}
}