package tc.AUMS_V.Version_Manage.util.depend;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
/** 
 * 
 * @author author
 */
@Entity() @Table(name="T_PCVA_VERSIONINFO") public class VersioninfoVo {
  private String VERSIONCODE;
  private String DESCRIPTION;
  private String CREATE_TIME;
  private String VERSIONID;
  private String OPTIONS_ID;
  public VersioninfoVo(){
  }
  public void setVERSIONCODE(  String VERSIONCODE){
    this.VERSIONCODE=VERSIONCODE;
  }
  /** 
 * 版本号
 */
  public String getVERSIONCODE(){
    return this.VERSIONCODE;
  }
  public void setDESCRIPTION(  String DESCRIPTION){
    this.DESCRIPTION=DESCRIPTION;
  }
  /** 
 * 版本描述
 */
  public String getDESCRIPTION(){
    return this.DESCRIPTION;
  }
  public void setCREATE_TIME(  String CREATE_TIME){
    this.CREATE_TIME=CREATE_TIME;
  }
  /** 
 * 创建时间
 */
  public String getCREATE_TIME(){
    return this.CREATE_TIME;
  }
  public void setVERSIONID(  String VERSIONID){
    this.VERSIONID=VERSIONID;
  }
  /** 
 * 版本ID
 */
  @Id() public String getVERSIONID(){
    return this.VERSIONID;
  }
  public void setOPTIONS_ID(  String OPTIONS_ID){
    this.OPTIONS_ID=OPTIONS_ID;
  }
  /** 
 * 策略ID
 */
  public String getOPTIONS_ID(){
    return this.OPTIONS_ID;
  }
}
