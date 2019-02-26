package tc.AUMS_V.Reso_Manang.Util;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
/** 
 * 
 * @author  author
 */
@Entity() @Table(name="AUMS_MENU_MENUTRADEREL") public class MenuTraderelVo {
  private String MENUARR;
  private String REMARK1;
  private String REMARK3;
  private String REMARK2;
  private String MENUTRADERELID;
  private String GROUP_ID;
  public MenuTraderelVo(){
  }
  public void setMENUARR(  String MENUARR){
    this.MENUARR=MENUARR;
  }
  /** 
 * 菜单组所包含的菜单ID，以","分割
 */
  @Column(name="MENUARR",nullable=true,unique=false,insertable=true,updatable=true) public String getMENUARR(){
    return this.MENUARR;
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
  public void setREMARK3(  String REMARK3){
    this.REMARK3=REMARK3;
  }
  /** 
 * 备注3
 */
  @Column(name="REMARK3",nullable=true,unique=false,insertable=true,updatable=true) public String getREMARK3(){
    return this.REMARK3;
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
  public void setMENUTRADERELID(  String MENUTRADERELID){
    this.MENUTRADERELID=MENUTRADERELID;
  }
  /** 
 * 关系ID
 */
  @Id() @Column(name="MENUTRADERELID",nullable=true,unique=false,insertable=true,updatable=true) public String getMENUTRADERELID(){
    return this.MENUTRADERELID;
  }
  public void setGROUP_ID(  String GROUP_ID){
    this.GROUP_ID=GROUP_ID;
  }
  /** 
 * 组ID,对应表t_pcva_client_menugroupinfo的组id
 */
  @Column(name="GROUP_ID",nullable=true,unique=false,insertable=true,updatable=true) public String getGROUP_ID(){
    return this.GROUP_ID;
  }
}
