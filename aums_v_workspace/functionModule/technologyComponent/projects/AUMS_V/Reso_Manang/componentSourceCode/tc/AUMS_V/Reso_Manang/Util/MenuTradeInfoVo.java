package tc.AUMS_V.Reso_Manang.Util;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
/** 
 * 
 * @author  author
 */
@Entity() @Table(name="AUMS_MENU_TRADEINFO") public class MenuTradeInfoVo {
  private String BG;
  private String TADPATH;
  private String THEMECOLOR;
  private String CHILDRENARR;
  private String LAYOUT;
  private String REMARK1;
  private String MENU_ID;
  private String REMARK3;
  private String TRADENAME;
  private String REMARK2;
  private String ACTIVECLASS;
  private String TRADECODE;
  private String ISHASCHILD;
  private String NAVIGATIONMODE;
  private String MENUSIZE;
  private String ISENABLED;
  private String ICON;
  private String CATEGORYNAME;
  public MenuTradeInfoVo(){
  }
  public void setBG(  String BG){
    this.BG=BG;
  }
  /** 
 * 菜单项背景图片
 */
  @Column(name="BG",nullable=true,unique=false,insertable=true,updatable=true) public String getBG(){
    return this.BG;
  }
  public void setTADPATH(  String TADPATH){
    this.TADPATH=TADPATH;
  }
  /** 
 * 菜点击菜单项进入的交易的tad路径,若有二级菜单此值为空字符串或无此字段
 */
  @Column(name="TADPATH",nullable=true,unique=false,insertable=true,updatable=true) public String getTADPATH(){
    return this.TADPATH;
  }
  public void setTHEMECOLOR(  String THEMECOLOR){
    this.THEMECOLOR=THEMECOLOR;
  }
  /** 
 * 二级菜单header背景颜色
 */
  @Column(name="THEMECOLOR",nullable=true,unique=false,insertable=true,updatable=true) public String getTHEMECOLOR(){
    return this.THEMECOLOR;
  }
  public void setCHILDRENARR(  String CHILDRENARR){
    this.CHILDRENARR=CHILDRENARR;
  }
  /** 
 * 二级菜单包含的菜单id,以","分割,二级菜单选择多个菜单项通过下拉列表多选的方式
 */
  @Column(name="CHILDRENARR",nullable=true,unique=false,insertable=true,updatable=true) public String getCHILDRENARR(){
    return this.CHILDRENARR;
  }
  public void setLAYOUT(  String LAYOUT){
    this.LAYOUT=LAYOUT;
  }
  /** 
 * 二级菜单布局格式
 */
  @Column(name="LAYOUT",nullable=true,unique=false,insertable=true,updatable=true) public String getLAYOUT(){
    return this.LAYOUT;
  }
  public void setREMARK1(  String REMARK1){
    this.REMARK1=REMARK1;
  }
  /** 
 * 备注1
 */
  @Column(name="REMARK1",nullable=true,unique=false,insertable=true,updatable=true) public String getREMARK1(){
    return this.REMARK1;
  }
  public void setMENU_ID(  String MENU_ID){
    this.MENU_ID=MENU_ID;
  }
  /** 
 * 菜单id
 */
  @Id() @Column(name="MENU_ID",nullable=true,unique=false,insertable=true,updatable=true) public String getMENU_ID(){
    return this.MENU_ID;
  }
  public void setREMARK3(  String REMARK3){
    this.REMARK3=REMARK3;
  }
  /** 
 * 备注3
 */
  @Column(name="REMARK3",nullable=true,unique=false,insertable=true,updatable=true) public String getREMARK3(){
    return this.REMARK3;
  }
  public void setTRADENAME(  String TRADENAME){
    this.TRADENAME=TRADENAME;
  }
  /** 
 * 菜单项标题
 */
  @Column(name="TRADENAME",nullable=true,unique=false,insertable=true,updatable=true) public String getTRADENAME(){
    return this.TRADENAME;
  }
  public void setREMARK2(  String REMARK2){
    this.REMARK2=REMARK2;
  }
  /** 
 * 备注2
 */
  @Column(name="REMARK2",nullable=true,unique=false,insertable=true,updatable=true) public String getREMARK2(){
    return this.REMARK2;
  }
  public void setACTIVECLASS(  String ACTIVECLASS){
    this.ACTIVECLASS=ACTIVECLASS;
  }
  /** 
 * 实现自定义菜单项点击效果的类名
 */
  @Column(name="ACTIVECLASS",nullable=true,unique=false,insertable=true,updatable=true) public String getACTIVECLASS(){
    return this.ACTIVECLASS;
  }
  public void setTRADECODE(  String TRADECODE){
    this.TRADECODE=TRADECODE;
  }
  /** 
 * 菜单项交易码
 */
  @Column(name="TRADECODE",nullable=true,unique=false,insertable=true,updatable=true) public String getTRADECODE(){
    return this.TRADECODE;
  }
  public void setISHASCHILD(  String ISHASCHILD){
    this.ISHASCHILD=ISHASCHILD;
  }
  /** 
 * 是否包含二级菜单,0：包含、1：不包含,不包含的话就没有下面对应的数据了
 */
  @Column(name="ISHASCHILD",nullable=true,unique=false,insertable=true,updatable=true) public String getISHASCHILD(){
    return this.ISHASCHILD;
  }
  public void setNAVIGATIONMODE(  String NAVIGATIONMODE){
    this.NAVIGATIONMODE=NAVIGATIONMODE;
  }
  /** 
 * 点击菜单项进入的交易页是否带有步骤条，step、non-step，若有二级菜单此值为空字符串或无此字段
 */
  @Column(name="NAVIGATIONMODE",nullable=true,unique=false,insertable=true,updatable=true) public String getNAVIGATIONMODE(){
    return this.NAVIGATIONMODE;
  }
  public void setMENUSIZE(  String MENUSIZE){
    this.MENUSIZE=MENUSIZE;
  }
  /** 
 * 菜单项大小
 */
  @Column(name="MENUSIZE",nullable=true,unique=false,insertable=true,updatable=true) public String getMENUSIZE(){
    return this.MENUSIZE;
  }
  public void setISENABLED(  String ISENABLED){
    this.ISENABLED=ISENABLED;
  }
  /** 
 * 菜单项是否可点击进入
 */
  @Column(name="ISENABLED",nullable=true,unique=false,insertable=true,updatable=true) public String getISENABLED(){
    return this.ISENABLED;
  }
  public void setICON(  String ICON){
    this.ICON=ICON;
  }
  /** 
 * 菜单项图标
 */
  @Column(name="ICON",nullable=true,unique=false,insertable=true,updatable=true) public String getICON(){
    return this.ICON;
  }
  public void setCATEGORYNAME(  String CATEGORYNAME){
    this.CATEGORYNAME=CATEGORYNAME;
  }
  /** 
 * 二级菜单header内标题
 */
  @Column(name="CATEGORYNAME",nullable=true,unique=false,insertable=true,updatable=true) public String getCATEGORYNAME(){
    return this.CATEGORYNAME;
  }
}
