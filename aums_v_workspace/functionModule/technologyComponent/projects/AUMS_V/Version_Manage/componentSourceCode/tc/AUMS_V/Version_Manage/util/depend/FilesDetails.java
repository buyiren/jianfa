package tc.AUMS_V.Version_Manage.util.depend;

import java.io.Serializable;

public class FilesDetails implements Serializable{

	/**
	 * 修改类使其实现Serializable接口
	 */
	private static final long serialVersionUID = -3334163730143510703L;
	private String id;
	private String md5;
	private String vpath;
	private String path;
	private String versionid;
	private String size;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getVPath() {
		return vpath;
	}

	public void setVPath(String vpath) {
		this.vpath = vpath;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getVersionid() {
		return versionid;
	}

	public void setVersionid(String versionid) {
		this.versionid = versionid;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
