package tc.AUMS_V.Reso_Manang.Util;
import java.io.Serializable;
@SuppressWarnings("serial")
public class DevModelMappingVoIdClass implements Serializable {
  private String modelid;
  private String devid;
  public void setModelid(  String modelid){
    this.modelid=modelid;
  }
  public void setDevid(  String devid){
    this.devid=devid;
  }
  public String getModelid(){
    return this.modelid;
  }
  public String getDevid(){
    return this.devid;
  }
}
