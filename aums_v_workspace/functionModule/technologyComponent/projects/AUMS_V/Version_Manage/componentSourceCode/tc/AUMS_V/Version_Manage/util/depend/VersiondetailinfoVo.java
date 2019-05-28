package tc.AUMS_V.Version_Manage.util.depend;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
/** 
 * 
 * @author author
 */
@Entity() @Table(name="T_PCVA_VERSIONDETAILINFO") public class VersiondetailinfoVo {
  private String CREATE_TIME;
  private String FILESIZE;
  private String MD5;
  private String FILENAME;
  private String PATH;
  private String VERSIONID;
  private String FILEID;
  private String TYPE;
  public VersiondetailinfoVo(){
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
  public void setFILESIZE(  String FILESIZE){
    this.FILESIZE=FILESIZE;
  }
  /** 
 * 文件大小
 */
  public String getFILESIZE(){
    return this.FILESIZE;
  }
  public void setMD5(  String MD5){
    this.MD5=MD5;
  }
  /** 
 * MD5
 */
  public String getMD5(){
    return this.MD5;
  }
  public void setFILENAME(  String FILENAME){
    this.FILENAME=FILENAME;
  }
  /** 
 * 文件名
 */
  public String getFILENAME(){
    return this.FILENAME;
  }
  public void setPATH(  String PATH){
    this.PATH=PATH;
  }
  /** 
 * 文件类型
 */
  public String getPATH(){
    return this.PATH;
  }
  public void setVERSIONID(  String VERSIONID){
    this.VERSIONID=VERSIONID;
  }
  /** 
 * 关联版本号
 */
  public String getVERSIONID(){
    return this.VERSIONID;
  }
  public void setFILEID(  String FILEID){
    this.FILEID=FILEID;
  }
  /** 
 * 文件ID
 */
  @Id() public String getFILEID(){
    return this.FILEID;
  }
  public void setTYPE(  String TYPE){
    this.TYPE=TYPE;
  }
  /** 
 * 创建时间
 */
  public String getTYPE(){
    return this.TYPE;
  }
}
