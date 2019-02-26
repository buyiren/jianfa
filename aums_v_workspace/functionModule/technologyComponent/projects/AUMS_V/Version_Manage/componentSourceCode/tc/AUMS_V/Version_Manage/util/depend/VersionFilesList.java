package tc.AUMS_V.Version_Manage.util.depend;
import java.io.Serializable;
import java.util.List;

public class VersionFilesList implements Serializable{
	/**
	 * 修改类使其实现Serializable接口
	 */
	private static final long serialVersionUID = 1187905296246574151L;
	private String id;
	private String name;
	private String description;
	private A_OptionsVo options;
	private List<FilesDetails> files;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	
	public A_OptionsVo getOptions() {
		return options;
	}

	public void setOptions(A_OptionsVo options) {
		this.options = options;
	}

	public List<FilesDetails> getFiles() {
		return files;
	}

	public void setFiles(List<FilesDetails> files) {
		this.files = files;
	}

}
