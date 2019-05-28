package tc.AUMS_V.Reso_Manang.Util;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
/** 
 * 
 * @author  author
 */
@Entity() @Table(name="AUMS_MENU_GROUPFIELD") public class GroupFieldVo {
  private String GFD;
  private String GROUPSERINO;
  private String REMARK1;
  private String REMARK3;
  private String REMARK2;
  private String GROUP_IDS;
  public GroupFieldVo(){
  }
  public void setGFD(  String GFD){
    this.GFD=GFD;
  }
  /** 
 * 菜单列ID
 */
  @Id() @Column(name="GFD",nullable=true,unique=false,insertable=true,updatable=true) public String getGFD(){
    return this.GFD;
  }
  public void setGROUPSERINO(  String GROUPSERINO){
    this.GROUPSERINO=GROUPSERINO;
  }
  /** 
 * 菜单模板列序号
 */
  @Column(name="GROUPSERINO",nullable=true,unique=false,insertable=true,updatable=true) public String getGROUPSERINO(){
    return this.GROUPSERINO;
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
  public void setGROUP_IDS(  String GROUP_IDS){
    this.GROUP_IDS=GROUP_IDS;
  }
  /** 
 * 菜单组id,以","分割
 */
  @Column(name="GROUP_IDS",nullable=true,unique=false,insertable=true,updatable=true) public String getGROUP_IDS(){
    return this.GROUP_IDS;
  }
}
