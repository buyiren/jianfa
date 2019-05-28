package tc.platform;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 支持FTP,SFTP协议
 * 
 * @date 2015-12-15 10:12:18
 */
@ComponentGroup(level = "平台", groupName = "FTP客户端组件类")
public class P_FTP {

	private static final int P_FTP = 0;
	private static final int P_SFTP = 1;

	/**
	 * @param host
	 *            入参|主机ip地址|{@link java.lang.String}
	 * @param port
	 *            入参|端口号(若为0则使用对应协议的默认端口)|int
	 * @param username
	 *            入参|用户名|{@link java.lang.String}
	 * @param passwd
	 *            入参|密码|{@link java.lang.String}
	 * @param protocol
	 *            入参|协议类型(0-FTP, 1-SFTP)|int
	 * @param localFilePath
	 *            入参|本地文件路径|{@link java.lang.String}
	 * @param remotePath
	 *            入参|远程上传目录|{@link java.lang.String}
	 * @param remoteFileName
	 *            入参|远程文件命名|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "host", comment = "主机ip地址", type = java.lang.String.class),
			@Param(name = "port", comment = "端口号(若为0则使用对应协议的默认端口)", type = int.class),
			@Param(name = "username", comment = "用户名", type = java.lang.String.class),
			@Param(name = "passwd", comment = "密码", type = java.lang.String.class),
			@Param(name = "protocol", comment = "协议类型(0-FTP, 1-SFTP)", type = int.class),
			@Param(name = "localFilePath", comment = "本地文件路径", type = java.lang.String.class),
			@Param(name = "remotePath", comment = "远程上传目录", type = java.lang.String.class),
			@Param(name = "remoteFileName", comment = "远程文件命名", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "上传文件", style = "判断型", type = "同步组件", comment = "通过指定协议上传文件", date = "2015-12-15 10:18:01")
	public static TCResult put(String host, int port, String username,
			String passwd, int protocol, String localFilePath,
			String remotePath, String remoteFileName) {
		if (protocol == P_FTP) {
			return ftpPut(host, port, username, passwd, localFilePath,
					remotePath, remoteFileName);
		} else if (protocol == P_SFTP) {
			return sftpPut(host, port, username, passwd, localFilePath,
					remotePath, remoteFileName);
		} else {
			return TCResult.newFailureResult(ErrorCode.AGR,
					"Unsupported protocol type of:" + protocol
							+ " (0 for ftp, 1 for sftp)");
		}

	}

	/**
	 * @param host
	 *            入参|主机ip地址|{@link java.lang.String}
	 * @param port
	 *            入参|端口号(若为0则使用对应协议的默认端口)|int
	 * @param username
	 *            入参|用户名|{@link java.lang.String}
	 * @param passwd
	 *            入参|密码|{@link java.lang.String}
	 * @param protocol
	 *            入参|协议(0-FTP, 1-SFTP)|int
	 * @param localSavedPath
	 *            入参|本地保存目录|{@link java.lang.String}
	 * @param localSavedFileName
	 *            入参|本地保存文件命名|{@link java.lang.String}
	 * @param remoteFilePath
	 *            入参|远程文件所在目录|{@link java.lang.String}
	 * @param remoteFileName
	 *            入参|远程文件名|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "host", comment = "主机ip地址", type = java.lang.String.class),
			@Param(name = "port", comment = "端口号(若为0则使用对应协议的默认端口)", type = int.class),
			@Param(name = "username", comment = "用户名", type = java.lang.String.class),
			@Param(name = "passwd", comment = "密码", type = java.lang.String.class),
			@Param(name = "protocol", comment = "协议(0-FTP, 1-SFTP)", type = int.class),
			@Param(name = "localSavedPath", comment = "本地保存目录", type = java.lang.String.class),
			@Param(name = "localSavedFileName", comment = "本地保存文件命名", type = java.lang.String.class),
			@Param(name = "remoteFilePath", comment = "远程文件所在目录", type = java.lang.String.class),
			@Param(name = "remoteFileName", comment = "远程文件名", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "下载文件", style = "判断型", type = "同步组件", comment = "通过指定协议下载文件", date = "2015-12-15 10:27:33")
	public static TCResult get(String host, int port, String username,
			String passwd, int protocol, String localSavedPath,
			String localSavedFileName, String remoteFilePath,
			String remoteFileName) {
		if (protocol == P_FTP) {
			return ftpGet(host, port, username, passwd, localSavedPath,
					localSavedFileName, remoteFilePath, remoteFileName);
		} else if (protocol == P_SFTP) {
			return sftpGet(host, port, username, passwd, localSavedPath,
					localSavedFileName, remoteFilePath, remoteFileName);
		} else {
			return TCResult.newFailureResult(ErrorCode.AGR,
					"Unsupported protocol type of:" + protocol
							+ " (0 for ftp, 1 for sftp)");
		}
	}

	private static TCResult ftpPut(String host, int port, String username,
			String passwd, String localFilePath, String remotePath,
			String remoteFileName) {

		File file = new File(localFilePath);
		if (!file.exists() || file.isDirectory()) {
			return TCResult.newFailureResult(ErrorCode.FILECTL, "Local file:"
					+ localFilePath + " doesn't exist or is not a file");
		}

		FTPClient ftp = null;
		BufferedInputStream bis = null;

		try {
			ftp = new FTPClient();

			if (port <= 0) {
				ftp.connect(host);
			} else {
				ftp.connect(host, port);
			}

			if (!ftp.login(username, passwd)) {
				return TCResult.newFailureResult(ErrorCode.COMM,
						"Incorrect username or password");
			}

			if (!ftp.changeWorkingDirectory(remotePath)) {
				return TCResult.newFailureResult(
						ErrorCode.REMOTE,
						"Directory "
								+ remotePath
								+ " doesn't exist or current user doesn't have enough permission to access the directory");
			}

			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.setControlEncoding("UTF-8");
			ftp.setBufferSize(8192);

			bis = new BufferedInputStream(new FileInputStream(file));

			if (!ftp.storeFile(remoteFileName, bis)) {
				return TCResult.newFailureResult(ErrorCode.HANDLING,
						"Fail to upload file, caused by:" + ftp.getReplyString());
			}
		} catch (IOException e) {
			AppLogger.error(e);
			return TCResult.newFailureResult(ErrorCode.IOCTL, e);
		} finally {
			if (ftp != null) {
				try {
					ftp.disconnect();
				} catch (IOException e) {
					AppLogger.error(e);
				}
			}

			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					AppLogger.error(e);
				}
			}
		}

		return TCResult.newSuccessResult();
	}

	private static TCResult sftpPut(String host, int port, String username,
			String passwd, String localFilePath, String remotePath,
			String remoteFileName) {

		File localFile = new File(localFilePath);
		if (!localFile.exists()) {
			return TCResult.newFailureResult(ErrorCode.FILECTL, "Local file:"
					+ localFilePath + " does not exists");
		}

		Session session = null;
		Channel channel = null;
		JSch jsch = new JSch();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			if (port <= 0) {
				session = jsch.getSession(username, host);
			} else {
				session = jsch.getSession(username, host, port);
			}

			if (session == null) {
				return TCResult.newFailureResult(ErrorCode.CONN,
						"Can not connect to " + host + ":" + port);
			}

			session.setPassword(passwd);
			// hints for first-time logging, options are:(ask | yes | no)
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(5000);

			channel = (Channel) session.openChannel("sftp");
			channel.connect(3000);
			ChannelSftp sftp = (ChannelSftp) channel;

			sftp.cd(remotePath);

			bos = new BufferedOutputStream(sftp.put(remoteFileName));
			bis = new BufferedInputStream(new FileInputStream(localFile));

			byte[] buf = new byte[8192];
			int n;
			while ((n = bis.read(buf)) != -1) {
				bos.write(buf, 0, n);
			}

			bos.flush();

		} catch (Exception e) {
			AppLogger.error(e);
			return TCResult.newFailureResult(ErrorCode.HANDLING, e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					AppLogger.error(e);
				}
			}

			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					AppLogger.error(e);
				}
			}

			if (session != null) {
				session.disconnect();
			}

			if (channel != null) {
				channel.disconnect();
			}
		}

		return TCResult.newSuccessResult();
	}

	private static TCResult ftpGet(String host, int port, String username,
			String passwd, String localSavedPath, String localSavedFileName,
			String remotePath, String remoteFileName) {
		File localPath = new File(localSavedPath);
		if (!localPath.exists()) {
			localPath.mkdirs();
		}

		FTPClient ftp = null;
		BufferedOutputStream bos = null;

		try {
			ftp = new FTPClient();
			if (port <= 0) {
				ftp.connect(host);
			} else {
				ftp.connect(host, port);
			}

			if (!ftp.login(username, passwd)) {
				return TCResult.newFailureResult(ErrorCode.COMM,
						"Incorrect username or password");
			}

			if (!ftp.changeWorkingDirectory(remotePath)) {
				return TCResult
						.newFailureResult(
								ErrorCode.REMOTE,
								"Directory "
										+ remotePath
										+ " doesn't exist or current user doesn't have enough permission to access the directory");
			}

			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.setControlEncoding("UTF-8");
			ftp.setBufferSize(8192);

			bos = new BufferedOutputStream(new FileOutputStream(new File(
					localPath, localSavedFileName)));
			if (!ftp.retrieveFile(remoteFileName, bos)) {
				return TCResult.newFailureResult(ErrorCode.HANDLING, "Fail to download file:"
						+ remoteFileName + " cause by:" + ftp.getReplyString());
			} else {
				return TCResult.newSuccessResult();
			}

		} catch (IOException e) {
			AppLogger.error(e);
			return TCResult.newFailureResult(ErrorCode.HANDLING, e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					AppLogger.error(e);
				}
			}

			if (ftp != null) {
				try {
					ftp.disconnect();
				} catch (IOException e) {
					AppLogger.error(e);
				}
			}
		}
	}

	private static TCResult sftpGet(String host, int port, String username,
			String passwd, String localSavedPath, String localSavedFileName,
			String remoteFilePath, String remoteFileName) {
		File localPath = new File(localSavedPath);
		if (!localPath.exists()) {
			localPath.mkdirs();
		}

		Session session = null;
		Channel channel = null;
		JSch jsch = new JSch();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			if (port <= 0) {
				session = jsch.getSession(username, host);
			} else {
				session = jsch.getSession(username, host, port);
			}

			if (session == null) {
				return TCResult.newFailureResult(ErrorCode.CONN, "Can not connect to " + host + ":"
						+ port);
			}

			session.setPassword(passwd);
			// hints for first-time logging, options are:(ask | yes | no)
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(5000);

			channel = (Channel) session.openChannel("sftp");
			channel.connect(3000);
			ChannelSftp sftp = (ChannelSftp) channel;

			if (!remoteFilePath.endsWith("/")) {
				remoteFilePath = remoteFilePath.concat("/");
			}

			bis = new BufferedInputStream(sftp.get(remoteFilePath
					+ remoteFileName));
			bos = new BufferedOutputStream(new FileOutputStream(new File(
					localPath, localSavedFileName)));

			byte[] buf = new byte[8192];
			int n;
			while ((n = bis.read(buf)) != -1) {
				bos.write(buf, 0, n);
			}

			bos.flush();

			return TCResult.newSuccessResult();
		} catch (Exception e) {
			AppLogger.error(e);
			return TCResult.newFailureResult(ErrorCode.HANDLING, e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					AppLogger.error(e);
				}
			}

			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					AppLogger.error(e);
				}
			}

			if (session != null) {
				session.disconnect();
			}

			if (channel != null) {
				channel.disconnect();
			}
		}
	}

}
