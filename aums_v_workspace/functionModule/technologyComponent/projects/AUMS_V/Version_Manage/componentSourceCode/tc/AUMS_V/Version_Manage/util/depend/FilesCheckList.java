package tc.AUMS_V.Version_Manage.util.depend;
import java.io.Serializable;
import java.util.List;

public class FilesCheckList implements Serializable{
	/**
	 * 修改类使其实现Serializable接口
	 */
	private static final long serialVersionUID = 1187905296246574151L;
	private boolean success=false;
	private String message;
	private List<FilesDetails> files;

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<FilesDetails> getFiles() {
		return files;
	}

	public void setFiles(List<FilesDetails> files) {
		this.files = files;
	}

}
