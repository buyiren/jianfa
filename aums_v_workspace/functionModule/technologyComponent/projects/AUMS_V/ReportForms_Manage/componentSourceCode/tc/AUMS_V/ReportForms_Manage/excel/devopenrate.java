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
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sun.misc.BASE64Encoder;
import cn.com.agree.afa.svc.javaengine.TCResult;
/**
 * 将文件读取并转成base64返回
 * 
 * @author hanbin
 *
 */
@ComponentGroup(level = "平台", groupName = "导出excel组件")
public class devopenrate{
	 private static FileOutputStream out ;

	//上锁为了线程安全,怕多线程扰乱计数器,同一时间导出报表的几率不高,性能不会下降太多
	public static  synchronized String devopenrate(String fileName, List<List<String>> resultSet ) throws IOException {
		
		//总台数计数
		int   sum=0;
		//行数计数
		int   i=0;
		//try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			//创建工作表  工作表的名字叫sheet
			XSSFSheet spreadsheet = workbook.createSheet("sheet");
			
			for (int i1=0;i1<=3;i1++){
				spreadsheet.autoSizeColumn((short)i1); //调整第i列宽度
			};
			XSSFRow row = spreadsheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
			// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			spreadsheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
			//----------------标题样式---------------------
			XSSFFont ztFont = workbook.createFont(); 
			//ztFont.setFontHeightInPoints((short)20);    // 将字体大小设置为18px   
			ztFont.setFontName("宋体");             // 将“宋体”字体应用到当前单元格上  
			ztFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    //加粗
			//创建样式
			XSSFCellStyle titleStyle = workbook.createCellStyle();
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//将标题赋予样式,将样式赋予字体
			cell.setCellStyle(titleStyle);
			titleStyle.setFont(ztFont);
			cell.setCellValue("总行-设备数量统计表");
			//正文
			//设置excel第二行标题title
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			XSSFRow row1 = spreadsheet.createRow(1);//第二行创建
			XSSFCell cell1  = row1.createCell(0);
			XSSFCellStyle titleStyle1 = workbook.createCellStyle();
			titleStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//靠右对齐
			XSSFFont fontsec = workbook.createFont();
			fontsec.setFontHeightInPoints((short)12);//设置字号
			fontsec.setFontName("宋体");//设置字体
			fontsec.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//正常字体不加粗
			titleStyle1.setFont(fontsec);
			cell1.setCellStyle(titleStyle1);
			spreadsheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));//起始行号，终止行号， 起始列号，终止列号【终止列号会加1】
			cell1.setCellValue("日期：" + df.format(new Date()));
			XSSFRow row2=spreadsheet.createRow(2);
			String[] headNames=new String[]{"设备品牌","设备类型","设备型号","设备数量"};
			for(int i1=0;i1<=3;i1++) {
				XSSFCell cell2  = row2.createCell(i1);
				XSSFCellStyle titleStyle12 = workbook.createCellStyle();
				titleStyle12.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
				titleStyle12.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平  
				//四周边框,thin:细线
				titleStyle12.setBorderTop(HSSFCellStyle.BORDER_THIN);
				titleStyle12.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				titleStyle12.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				titleStyle12.setBorderRight(HSSFCellStyle.BORDER_THIN);
				//列宽
                spreadsheet.setColumnWidth(i1, 20 * 256);
				XSSFFont font2 = workbook.createFont();
				font2.setFontHeightInPoints((short)11);//设置字号
				font2.setFontName("宋体");//设置字体
				cell2.setCellStyle(titleStyle12);
				cell2.setCellValue(headNames[i1]);
			}
			//从第四行开始,获取数据信息到Cell中,第二行日期,第三行已被列标题占用,第一行是报表主题
			i=3;
			//通过jdbc组件获取结果集放入List中
			int startRowNum = 1;
			int startColNum = 1;
			int currentRowNum = startRowNum-1;//从第几行开始写
			int currentColNum = startColNum-1;//从第几列开始写
			//遍历传进来的集合,存储的是jdbc组件查询后的数据
			for (List<String> str: resultSet) {
				XSSFCellStyle titleStyle13 = workbook.createCellStyle();
				titleStyle13.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
				titleStyle13.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平    
				XSSFFont font2 = workbook.createFont();
				font2.setFontHeightInPoints((short)11);//设置字号
				font2.setFontName("宋体");//设置字体
				titleStyle13.setFont(font2);
				XSSFRow row3=spreadsheet.createRow(i);
				XSSFCell cell3 = null;
                System.out.println(str);
				//从i行开始创建行
                row3 = spreadsheet.createRow(i);
                cell3 = row3.createCell(currentColNum++, XSSFCell.CELL_TYPE_NUMERIC);
                spreadsheet.addMergedRegion(new CellRangeAddress(i, Integer.parseInt(str.get(4))+i, 0, 0));
                //spreadsheet.addMergedRegion(new CellRangeAddress(i, 5, 0, 0));
				cell3.setCellStyle(titleStyle1);
				cell3.setCellValue(str.get(0));
				cell3.setCellStyle(titleStyle13);
				cell3 = row3.createCell(currentColNum++, XSSFCell.CELL_TYPE_NUMERIC);
				cell3.setCellValue(str.get(1));
				cell3.setCellStyle(titleStyle13);
				cell3 = row3.createCell(currentColNum++, XSSFCell.CELL_TYPE_NUMERIC);
				cell3.setCellValue(str.get(2));
				cell3.setCellStyle(titleStyle13);
				cell3 = row3.createCell(currentColNum++, XSSFCell.CELL_TYPE_NUMERIC);
				cell3.setCellValue(str.get(3));
				cell3.setCellStyle(titleStyle1);
				row3 = spreadsheet.createRow(i+1);
				spreadsheet.addMergedRegion(new CellRangeAddress(i+1, i+1, 1, 2));
				cell3 = row3.createCell(1, XSSFCell.CELL_TYPE_NUMERIC);
				cell3.setCellValue("合计");
				cell3.setCellStyle(titleStyle13);
				cell3 = row3.createCell(2, XSSFCell.CELL_TYPE_NUMERIC);
				cell3.setCellValue("");
				cell3 = row3.createCell(3, XSSFCell.CELL_TYPE_NUMERIC);
				cell3.setCellValue(str.get(3));		
				cell3.setCellStyle(titleStyle1);
				currentRowNum++;//写完第一个list，跳到下一行
				currentColNum = startColNum-1;
				i+=Integer.parseInt(str.get(4))+1;
				sum+=Integer.parseInt(str.get(3));
				System.out.println(sum);
				System.out.println("iiiii"+i);	
			}
			//最后合计行
			XSSFCellStyle titleStyle13 = workbook.createCellStyle();
			titleStyle13.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
			XSSFFont font3 = workbook.createFont();
			font3.setFontHeightInPoints((short)11);//设置字号
			font3.setFontName("宋体");//设置字体
			titleStyle13.setFont(font3);
			spreadsheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
			XSSFRow row4=spreadsheet.createRow(i);
			XSSFCell cell4;
			row4 = spreadsheet.createRow(i);
			cell4 = row4.createCell(0, XSSFCell.CELL_TYPE_NUMERIC);
			cell4.setCellValue("合计");
			cell4.setCellStyle(titleStyle13);
			cell4 = row4.createCell(3, XSSFCell.CELL_TYPE_NUMERIC);
			cell4.setCellValue(sum);
			//将文件写出
			//将文件写出
			File file =null;
			//目录为/home/afa4sj/AFA4J_2.7.1_1207/hanbintemp
			out=new FileOutputStream( new File(file.separator+"home"+file.separator+"afa4sj"+file.separator+"AFA4J_2.7.1_1207"+file.separator+"share"+file.separator+"excel"+file.separator+fileName+".xlsx"));
			workbook.write(out);
			out.flush();
			out.close();
			System.out.println("1.xlsx written successfully");	
			//return (file.separator+"home"+file.separator+"afa4sj"+file.separator+"AFA4J_2.7.1_1207"+file.separator+"share"+file.separator+"excel"+file.separator+fileName+".xlsx");	
			return ("http://192.9.200.225:8023/excel/"+fileName+".xlsx");	
			//} 
		//catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("导出失败");
//			return(e.printStackTrace().);
		//}
//		finally {
//			out.close();
//		}
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
				@Param(name = "resultSet", comment = "结果集", type = java.util.List.class) })
		@OutParams(param = { @Param(name = "BASE64Encoder", comment = "Excel转换BASE64Encoder字符串结果", type = java.lang.String.class) })
		@Returns(returns = { @Return(id = "1", desp = "成功") })
		@Component(label = "devopenrate", style = "处理型", type = "同步组件", date = "2018-05-16 11:50:44")
	//这种写法不知道合不合适,但是觉得比较清晰
	public static TCResult A_ExcelPOI(String fileName1, List resultSet)
			throws IOException {
		String fileName = devopenrate(fileName1, resultSet);
		return TCResult.newSuccessResult(fileName);
	}
}