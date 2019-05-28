package tc.View.Agent.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * properties文件读取类
 * 
 */
public class PropertiesReader {

    public static JavaDict getPropertiesReader(String filePathName,JavaList keys) throws FileNotFoundException,IOException{
    	try{
    		JavaDict ret = new JavaDict();
    		InputStream in = new BufferedInputStream(new FileInputStream(filePathName));        
    		Properties p = new Properties();        
    		p.load(in);
    		for(int i=0;i<keys.size();i++){
    			ret.put((String)keys.get(i), (String)p.get(keys.get(i)));
    		}
    		return ret;
    	}catch(FileNotFoundException e){
    		e.printStackTrace();
    		throw e;
    	}catch(IOException e){
    		e.printStackTrace();
    		throw e;
    	}
    }

}