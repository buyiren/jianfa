package tc.AUMS_V.Reso_Manang.Util;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.IdClass;
import javax.persistence.Entity;
/** 
 * 
 * @author author
 */
@Entity() @Table(name="AUMS_MENU_DEVMODELMAPPING") @IdClass(DevModelMappingVoIdClass.class) public class DevModelMappingVo {
  private String remark1;
  private String modeltype;
  private String brno;
  private String remark2;
  private String remark3;
  private String modelid;
  private String create_time;
  private String create_user;
  private String devid;
  private String devip;
  private String update_time;
  private String update_user;
  private String devtype;
  public DevModelMappingVo(){
  }
  public void setRemark1(  String remark1){
    this.remark1=remark1;
  }
  /** 
 * 
 */
  public String getRemark1(){
    return this.remark1;
  }
  public void setModeltype(  String modeltype){
    this.modeltype=modeltype;
  }
  /** 
 * 模版类型：00菜单模版，01广告推送
 */
  public String getModeltype(){
    return this.modeltype;
  }
  public void setBrno(  String brno){
    this.brno=brno;
  }
  /** 
 * 
 */
  public String getBrno(){
    return this.brno;
  }
  public void setRemark2(  String remark2){
    this.remark2=remark2;
  }
  /** 
 * 
 */
  public String getRemark2(){
    return this.remark2;
  }
  public void setRemark3(  String remark3){
    this.remark3=remark3;
  }
  /** 
 * 
 */
  public String getRemark3(){
    return this.remark3;
  }
  public void setModelid(  String modelid){
    this.modelid=modelid;
  }
  /** 
 * 
 */
  @Id() public String getModelid(){
    return this.modelid;
  }
  public void setCreate_time(  String create_time){
    this.create_time=create_time;
  }
  /** 
 * 
 */
  public String getCreate_time(){
    return this.create_time;
  }
  public void setCreate_user(  String create_user){
    this.create_user=create_user;
  }
  /** 
 * 
 */
  public String getCreate_user(){
    return this.create_user;
  }
  public void setDevid(  String devid){
    this.devid=devid;
  }
  /** 
 * 
 */
  @Id() public String getDevid(){
    return this.devid;
  }
  public void setDevip(  String devip){
    this.devip=devip;
  }
  /** 
 * 设备IP地址
 */
  public String getDevip(){
    return this.devip;
  }
  public void setUpdate_time(  String update_time){
    this.update_time=update_time;
  }
  /** 
 * 
 */
  public String getUpdate_time(){
    return this.update_time;
  }
  public void setUpdate_user(  String update_user){
    this.update_user=update_user;
  }
  /** 
 * 
 */
  public String getUpdate_user(){
    return this.update_user;
  }
  public void setDevtype(  String devtype){
    this.devtype=devtype;
  }
  /** 
 * 
 */
  public String getDevtype(){
    return this.devtype;
  }
}
