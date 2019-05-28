package tc.AUMS_V.Reso_Manang.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExcelParseTool {
	 //private String mFilePath;
	 //private InputStream is;

	  /*public  void setInputStream(File f) {
	    	try {
				is = new InputStream(f);
			} catch (FileNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
	    }*/
	    private static final String SUFFIX_2003 = ".xls";
	    private static final String SUFFIX_2007 = ".xlsx";

	    public Workbook initWorkBook(InputStream is,String pathname) throws IOException {

	        Workbook workbook=null;
	        if (pathname.endsWith(SUFFIX_2003)) {
	            workbook = new HSSFWorkbook(is);
	        } else if (pathname.endsWith(SUFFIX_2007)) {
	            workbook = new XSSFWorkbook(is);
	        }

	        return workbook;
	    }	    
	    public List<List<String>> getExcelData(Workbook workbook) {
	    	List<List<String>> dataLst = new ArrayList<List<String>>();
	        int numOfSheet = workbook.getNumberOfSheets();
	        for (int i = 0; i < numOfSheet; ++i) {
	            Sheet sheet = workbook.getSheetAt(i);
	            //获取当前sheet的开始行数数
	            int firstRows = sheet.getFirstRowNum();
	            //获取当前sheet的结束行数数
	            int endRows = sheet.getLastRowNum();
	            //循环遍历行
	            for(int r=firstRows+1;r<endRows+1;r++){	           
	            	Row row = sheet.getRow(r);
	            	if(row==null){
	            		continue;
	            	}
	   			//获取当前行的列数 
	   			List<String> rowLst = new ArrayList<String>();
	   			//获取当前sheet的开始列数
	            int firstCells = row.getFirstCellNum();
	            //获取当前sheet的结束列数
	            int endCells = row.getPhysicalNumberOfCells();
	   			//循环遍历列
	   			 for(int c = firstCells;c<endCells;c++){
	   				Cell cell = row.getCell(c);
	   				//DevInfoVo devinfo = new DevInfoVo();
	   				//devinfo.setDEVID(cell.getStringCellValue());
	   				String cellValue = "";
/*	   				System.out.println((HSSFCell.CELL_TYPE_BLANK));
	   				if((cell.getCellType()==HSSFCell.CELL_TYPE_BLANK)){
	   					cellValue="";
	   				}*/
	   				if(cell==null){
	   					continue;
	   				}
	   				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
   					cellValue = cell.getStringCellValue();
	   				rowLst.add(cellValue);
	   			 }
	   			dataLst.add(rowLst);
	   		 }
	            
	        }
	        return dataLst;
	    }

}
