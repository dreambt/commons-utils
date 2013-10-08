package cn.im47.commons.utils.merpressor;

import com.googlecode.htmlcompressor.compressor.YuiJavaScriptCompressor;
import com.yahoo.platform.yui.compressor.SiqiCssCompressor;
import com.yahoo.platform.yui.compressor.SiqiJavaScriptCompressor;
import org.apache.commons.io.FileUtils;
import org.mozilla.javascript.ErrorReporter;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import javax.management.timer.Timer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Servlet implementation class Compressor
 */
public class Merpressor extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Merpressor() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// expires, default expires: 1 hour
		int expires = 3600 * 24 * 7;
		String expiresStr = request.getParameter("expires");
		if (expiresStr != null && !expiresStr.equalsIgnoreCase("")) {
			expires = Integer.parseInt(request.getParameter("expires")) * 60;
		}

		// calculate expires header
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, (int) (expires * Timer.ONE_SECOND));
		Date expTime = cal.getTime();
		Locale local = Locale.US;
		DateFormat fmt = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", new DateFormatSymbols(local));
		fmt.setTimeZone(TimeZone.getTimeZone("GMT"));

		// file type and mime-type
		String encoding = "utf-8";
		String type = request.getParameter("t");
		String mimeType = "application/x-javascript";
		if ("css".equalsIgnoreCase(type)) {
			mimeType = "text/css";
		}

		// set header
		response.setHeader("Cache-Control", "max-age=" + Integer.toString(expires, 10));
		//response.setHeader("Content-Encoding", "gzip");
		response.setHeader("Content-Type", mimeType + "; charset=" + encoding);
		response.setHeader("Expires", fmt.format(expTime));
		response.setHeader("Vary", "Accept-Encoding");

		// web root
		String root = request.getSession(true).getServletContext().getRealPath("/static");
		String fs = System.getProperties().getProperty("file.separator");
		String filesStr = request.getParameter("f");
		String key = Encodes.encodeHex(Digests.sha1(filesStr.getBytes()));

		PrintWriter writer = response.getWriter();

		File file = new File(root + fs + key);
		if (file.exists()) {
			// 如果目标文件存在，直接返回文件
			writer.print(FileUtils.readFileToString(file, "UTF-8"));
			writer.flush();
			writer.close();
			return;
		}

		// javascript or css file list
		String[] filesArray = filesStr.split(",");
		Vector<String> files = new Vector<String>();
		for (String fileStr : filesArray) {
			if ("\\".equals(fs)) {
				fileStr = fileStr.replace("/", "\\");
			}
			files.add(root + fileStr);
			//response.setHeader("File-Path", file);
		}

		// get streams
		InputStreamReader inReader = null;
		try {
			inReader = new InputStreamReader(new SequenceInputStream(new InputStreamEnumerator(files)), encoding);
		} catch (FileListException e) {
			response.setHeader("File-Not-Found", e.path);
			response.setStatus(404);
			return;
		}

		// merge and compress
		int linebreakpos = -1;
		boolean verbose = false;
		String compressStr = null;
		if (type.equalsIgnoreCase("js")) {
			ErrorReporter logger = new YuiJavaScriptCompressor.DefaultErrorReporter();
			try {
				SiqiJavaScriptCompressor compressor = new SiqiJavaScriptCompressor(inReader, logger);
				// Close the input stream first, and then open the output stream,
				// in case the output file should override the input file.
				boolean munge = true;
				boolean preserveAllSemiColons = false;
				boolean disableOptimizations = false;
				compressStr = compressor.compress(linebreakpos, munge, verbose, preserveAllSemiColons, disableOptimizations);
			} catch (Exception e) {
				// e.printStackTrace();
				// Return a special error code used specifically by the web front-end.
				// System.exit(2);
				//response.setStatus(500);
				//return;
				int in;
				while ((in = inReader.read()) != -1) {
					writer.print((char) in);
				}
				e.printStackTrace();
			}
		} else if (type.equalsIgnoreCase("css")) {
			try {
				SiqiCssCompressor compressor = new SiqiCssCompressor(inReader);
				// Close the input stream first, and then open the output stream,
				// in case the output file should override the input file.
				compressStr = compressor.compress(linebreakpos);
				compressStr = compressStr.replace("../", "static/");
			} catch (Exception e) {
				//response.setStatus(500);
				//return;
				int in;
				while ((in = inReader.read()) != -1) {
					writer.print((char) in);
				}
				e.printStackTrace();
			}
		}
		writer.print(compressStr);
		inReader.close();
		inReader = null;
		writer.flush();
		writer.close();

		FileUtils.writeStringToFile(file, compressStr, encoding);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
