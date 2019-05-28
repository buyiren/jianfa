package tc.AUMS_V.Reso_Manang.Util;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
/** 
 * 
 * @author author
 */
@Entity() @Table(name="AUMS_MENU_MENUMODELINFO") public class MenuModelInfoVo {
  private String remark1;
  private String model_desc;
  private String remark2;
  private String remark3;
  private String create_time;
  private String menu_style;
  private String update_time;
  private String model_name;
  private String isupload;
  private String filepath;
  private String menu_model_id;
  private String create_user;
  private String update_user;
  private String filedesc;
  private String groupfields;
  public MenuModelInfoVo(){
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
  public void setModel_desc(  String model_desc){
    this.model_desc=model_desc;
  }
  /** 
 * 
 */
  public String getModel_desc(){
    return this.model_desc;
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
  public void setCreate_time(  String create_time){
    this.create_time=create_time;
  }
  /** 
 * 
 */
  public String getCreate_time(){
    return this.create_time;
  }
  public void setMenu_style(  String menu_style){
    this.menu_style=menu_style;
  }
  /** 
 * 
 */
  public String getMenu_style(){
    return this.menu_style;
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
  public void setModel_name(  String model_name){
    this.model_name=model_name;
  }
  /** 
 * 
 */
  public String getModel_name(){
    return this.model_name;
  }
  public void setIsupload(  String isupload){
    this.isupload=isupload;
  }
  /** 
 * 是否上传菜单文件，1是，0否
 */
  public String getIsupload(){
    return this.isupload;
  }
  public void setFilepath(  String filepath){
    this.filepath=filepath;
  }
  /** 
 * 文件路径，包含文件名
 */
  public String getFilepath(){
    return this.filepath;
  }
  public void setMenu_model_id(  String menu_model_id){
    this.menu_model_id=menu_model_id;
  }
  /** 
 * 
 */
  @Id() public String getMenu_model_id(){
    return this.menu_model_id;
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
  public void setUpdate_user(  String update_user){
    this.update_user=update_user;
  }
  /** 
 * 
 */
  public String getUpdate_user(){
    return this.update_user;
  }
  public void setFiledesc(  String filedesc){
    this.filedesc=filedesc;
  }
  /** 
 * 文件描述
 */
  public String getFiledesc(){
    return this.filedesc;
  }
  public void setGroupfields(  String groupfields){
    this.groupfields=groupfields;
  }
  /** 
 * 组栏位，存放分组栏位信息，仅平铺式用到,以","分割
 */
  public String getGroupfields(){
    return this.groupfields;
  }
}
