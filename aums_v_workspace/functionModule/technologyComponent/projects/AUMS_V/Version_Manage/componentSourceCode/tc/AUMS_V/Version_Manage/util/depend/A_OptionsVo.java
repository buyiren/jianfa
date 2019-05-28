package tc.AUMS_V.Version_Manage.util.depend;

import java.io.Serializable;
import java.util.Date;

public class A_OptionsVo implements Serializable {
	/**
	 * 修改类使其实现Serializable接口
	 */
	private static final long serialVersionUID = 8869134792167562304L;
	private String matchPatterns;
	  private String excludedFiles;
	  private String excludedDirs;
	  private Date createTime;
	  private String onceUpdateDirs;
	  private String optionId;
	  private String onceUpdateFiles;
	  public A_OptionsVo(){
	  }
	public String getMatchPatterns() {
		return matchPatterns;
	}
	public void setMatchPatterns(String matchPatterns) {
		this.matchPatterns = matchPatterns;
	}
	public String getExcludedFiles() {
		return excludedFiles;
	}
	public void setExcludedFiles(String excludedFiles) {
		this.excludedFiles = excludedFiles;
	}
	public String getExcludedDirs() {
		return excludedDirs;
	}
	public void setExcludedDirs(String excludedDirs) {
		this.excludedDirs = excludedDirs;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getOnceUpdateDirs() {
		return onceUpdateDirs;
	}
	public void setOnceUpdateDirs(String onceUpdateDirs) {
		this.onceUpdateDirs = onceUpdateDirs;
	}
	public String getOptionId() {
		return optionId;
	}
	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}
	public String getOnceUpdateFiles() {
		return onceUpdateFiles;
	}
	public void setOnceUpdateFiles(String onceUpdateFiles) {
		this.onceUpdateFiles = onceUpdateFiles;
	}

}
