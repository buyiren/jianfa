package tc.bank.utils;

import java.util.ArrayList;
import java.util.Random;

public class LongSockCliUtil {
	/**
	 * 将服务端ar实例的配置信息（格式[ip:port,ip:port]）转换成ArrayList列表,并且打乱顺序
	 * */
	public static ArrayList<ArrayList<String>> arCfg2List(String ar_ip_port_cfg) {
		if (ar_ip_port_cfg.length() <= 0)
			return null;
		if (!ar_ip_port_cfg.endsWith(","))
			ar_ip_port_cfg += ",";
		ArrayList<ArrayList<String>> ret1 = new ArrayList<ArrayList<String>>();
		int idx = 0;
		while (true) {
			int tmpIdx = ar_ip_port_cfg.indexOf(",", idx);
			if (tmpIdx < 0)
				break;
			String tmpArCfgStr = ar_ip_port_cfg.substring(idx, tmpIdx);
			ArrayList<String> tmpArCfg = new ArrayList<String>();
			tmpArCfg.add(tmpArCfgStr.substring(0, tmpArCfgStr.indexOf(":")));
			tmpArCfg.add(tmpArCfgStr.substring(tmpArCfgStr.indexOf(":") + 1,tmpArCfgStr.indexOf(":",tmpArCfgStr.indexOf(":")+1)));
			ret1.add(tmpArCfg);
			idx = tmpIdx + 1;
		}
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		Random r = new Random();
		while (ret1.size() > 0) {
			ret.add(ret1.remove(r.nextInt(ret1.size())));
		}
		return ret;
	}
}
