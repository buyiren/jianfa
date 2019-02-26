package tc.View.Agent;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tc.platform.P_Logger;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * 获取机具唯一标识
 * 
 * @date 2018-08-31 14:33:37
 */
@ComponentGroup(level = "应用", groupName = "getSn", projectName = "AUMS_Agent", appName = "Agent")
public class A_getAllSn {
	public static String getUnit(String vbs) {

			String result = "";
			try {
				File file = File.createTempFile("realhowto", ".vbs");
				file.deleteOnExit();
				FileWriter fw = new java.io.FileWriter(file);
				fw.write(vbs);
				fw.close();
				String path = file.getPath().replace("%20", " ");
				Process p = Runtime.getRuntime().exec(
						"cscript //NoLogo " + path);
				BufferedReader input = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				String line;
				int i=0;
				while ((line = input.readLine()) != null) {
					if(i!=0) {
						result+=",";
					}
					i++;
					result += line;
				}
				input.close();
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result.trim().replace(" ", "");
		}
		public static String getbaseBoardIds() {
			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
					+ "Set colItems = objWMIService.ExecQuery _ \n"
					+ "   (\"Select * from Win32_BaseBoard\") \n"
					+ "For Each objItem in colItems \n"
					+ "    Wscript.Echo objItem.SerialNumber \n"
					+ "Next \n";
			return getUnit(vbs);
		}
		public static String getbiosIds() {
			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
					+ "Set colItems = objWMIService.ExecQuery _ \n"
					+ "   (\"Select * from Win32_BIOS\") \n"
					+ "For Each objItem in colItems \n"
					+ "    Wscript.Echo objItem.SerialNumber \n"
					+ "Next \n";
			return getUnit(vbs);
		}
		public static String getdiskDriveIds() {
			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
					+ "Set colItems = objWMIService.ExecQuery(\"Select * from Win32_DiskDrive\") \n"
					+ "For Each objItem in colItems \n"
					+ "if objItem.MediaType = \"Fixed hard disk media\" then \n"
					+ "Wscript.Echo objItem.SerialNumber \n"
					+ "end if \n"
					+ "Next \n";
			return getUnit(vbs);
		}
		public static String getCPUSerial() {
			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
					+ "Set colItems = objWMIService.ExecQuery _ \n"
					+ "   (\"Select * from Win32_Processor\") \n"
					+ "For Each objItem in colItems \n"
					+ "    Wscript.Echo objItem.ProcessorId \n"
					+ "Next \n";
			return getUnit(vbs);
		}
		public static String getAllNetworkMacAddress() {
			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
					+ "Set colItems = objWMIService.ExecQuery(\"Select * from Win32_NetworkAdapter\") \n"
					+ "For Each objItem in colItems \n"
					+ "if objItem.PhysicalAdapter then \n"
					+ "Wscript.Echo objItem.MACAddress \n"
					+ "end if \n"
					+ "Next \n";
			return getUnit(vbs);
		}
		public static String getMac() {
			String mac="";
			ArrayList<String> regList=getRealNetname();
			String name="";
			for(int i=0;i<regList.size();i++) {
				name=(String) regList.get(i);
				if(i!=0) {
					mac=mac+",";
				}
				mac=mac+getmacaddress(name);
			}
			return mac;
		}
		public static ArrayList<String> getRealNetname() {
			ArrayList<String> regList=new ArrayList<String>();
			try {
				Process ps = null;
				ps = Runtime.getRuntime().exec("reg query HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Control\\Network\\{4D36E972-E325-11CE-BFC1-08002BE10318}");
				ps.getOutputStream().close();
				InputStreamReader i = new InputStreamReader(ps.getInputStream(),"GBK");
				String line;
				BufferedReader ir = new BufferedReader(i);
				while ((line = ir.readLine()) != null) {
					Process pstemp = null;
					pstemp = Runtime.getRuntime().exec("reg query "+line+"\\Connection");
					pstemp.getOutputStream().close();
					InputStreamReader itemp = new InputStreamReader(pstemp.getInputStream(),"GBK");
					String linetemp;
					BufferedReader irtemp = new BufferedReader(itemp);
					Map<String,String> regMap = new HashMap<String,String>();
					while ((linetemp = irtemp.readLine()) != null) {
						String linereplacespace=linetemp.replace(" ", "");
						if(linereplacespace.contains("Name"))
						{
							regMap.put("Name", linereplacespace.substring(10, linereplacespace.length()));
						}
						if(linereplacespace.toLowerCase().contains("pnpinstanceid"))
						{
							regMap.put("pnpinstanceid", linereplacespace.substring(19, linereplacespace.length()));
						}

					}
					if(regMap.containsKey("pnpinstanceid")&&regMap.get("pnpinstanceid").contains("PCI"))
					{
						regList.add(regMap.get("Name"));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return regList;
		}
		public static String getmacaddress(String name){
			String macaddress="";

			try {
				Process process = Runtime.getRuntime().exec("cmd.exe /c ipconfig /all");
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));  

				String line;
				int i=-1;
				while ((line = br.readLine()) != null) {
					if(line.indexOf(name+":")>=0){
						i=1;
						continue;
					}
					if (i==1&&((line.indexOf("物理地址")>=0)||(line.indexOf("Physical Address")>=0))) {  
						int index = line.indexOf(":");
						macaddress = line.substring(index+1).trim().replace("-", "");
						break;  
					}  
				}  
				br.close();  
				process.destroy ();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return macaddress;
		}
		public static String md5(String s) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] bytes = md.digest(s.getBytes("utf-8"));
				return toHex(bytes).toLowerCase();
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		public static String toHex(byte[] bytes) {

			final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
			StringBuilder ret = new StringBuilder(bytes.length * 2);
			for (int i=0; i<bytes.length; i++) {
				ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
				ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
			}
			return ret.toString();
		}
	/**
	 * @category 获取机具唯一标识
	 * @param idSummaryMd5
	 *            出参|机具唯一标识| {@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@OutParams(param = { @Param(name = "idSummaryMd5", comment = "机具唯一标识", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取机具唯一标识", style = "判断型", type = "同步组件", author = "hanbin", date = "2018-08-31 02:05:25")
	public static TCResult A_GetSn() {
		String diskDriveIds = A_getAllSn.getdiskDriveIds();
		String baseBoardIds = A_getAllSn.getbaseBoardIds();
		String biosIds = A_getAllSn.getbiosIds();
		String processorIds = A_getAllSn.getCPUSerial();
		String networkAdapterIds = A_getAllSn.getAllNetworkMacAddress();
		String networkAdapterIds2 = A_getAllSn.getMac();
		String idSummary =diskDriveIds+","+baseBoardIds+","+biosIds+","+processorIds+","+networkAdapterIds;
		String idSummary2 =diskDriveIds+","+baseBoardIds+","+biosIds+","+processorIds+","+networkAdapterIds2;
		String idSummaryMd5=md5(idSummary);
		String idSummaryMd52=md5(idSummary2);
		List list = new ArrayList();
		list.add(idSummaryMd5);
		list.add(idSummaryMd52);
		return TCResult.newSuccessResult(list);
	}
}
