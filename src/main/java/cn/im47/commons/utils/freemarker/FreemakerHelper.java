package cn.im47.commons.utils.freemarker;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.*;
import java.util.Map;

/**
 * Freemaker Helper
 * <p/>
 * User: baitao.jibt@gmail.com
 * Date: 12-8-27
 * Time: 上午11:31
 */
public class FreemakerHelper {

	private static final String DEFAULT_ENCODING = "utf-8";

	private static Logger logger = LoggerFactory.getLogger(FreemakerHelper.class);

	private Configuration freemarkerConfiguration;

	@Value("${path.upload.base}")
	private String UPLOAD_PATH;

	@Value("${website.domain}")
	private String WEB_URL;

	private Template template;
	private static HtmlCompressor compressor = new HtmlCompressor();

	static {
		compressor.setRemoveComments(true);
		compressor.setRemoveIntertagSpaces(true);
		compressor.setRemoveQuotes(true);
		compressor.setRemoveMultiSpaces(true);
		compressor.setCompressJavaScript(true);
		compressor.setCompressCss(true);
	}

	/**
	 * 使用Freemarker生成html格式内容.
	 */
	public String generateContent(Map<String, Object> context, String templateFile) throws Exception {
		try {
			template = freemarkerConfiguration.getTemplate(templateFile, DEFAULT_ENCODING);

			// 处理模版
			context.put("ctx", WEB_URL);
			String htmlStr = FreeMarkerTemplateUtils.processTemplateIntoString(template, context);

			// html 压缩
			return compressor.compress(htmlStr);
		} catch (IOException e) {
			logger.error("FreeMarker 处理失败. 模板不存在！", e);
			throw new Exception("FreeMarker 处理失败. 模板不存在！", e);
		} catch (TemplateException e) {
			logger.error("FreeMarker 处理失败. 模板处理异常！", e);
			throw new Exception("FreeMarker 处理失败. 模板处理异常！", e);
		} catch (NullPointerException e) {
			logger.error("FreeMarker 处理失败. 空指针异常！", e);
			throw new Exception("FreeMarker 处理失败. 空指针异常！", e);
		}
	}

	/**
	 * 使用Freemarker生成html格式内容并保存到指定文件.
	 */
	public void generateContent(Map<String, Object> context, String templateFile, String targetFile) throws Exception {
		try {
			template = freemarkerConfiguration.getTemplate(templateFile, DEFAULT_ENCODING);

			File htmlFile = new File(UPLOAD_PATH + targetFile);
			htmlFile.createNewFile();
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), DEFAULT_ENCODING));//设置Encoding

			// 处理模版
			context.put("ctx", WEB_URL);
			String htmlStr = FreeMarkerTemplateUtils.processTemplateIntoString(template, context);

			// html 压缩
			htmlStr = compressor.compress(htmlStr);
			out.write(htmlStr);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("FreeMarker 处理失败. 模板不存在！", e);
			throw new Exception("FreeMarker 处理失败. 模板不存在！", e);
		} catch (TemplateException e) {
			logger.error("FreeMarker 处理失败. 模板处理异常！", e);
			throw new Exception("FreeMarker 处理失败. 模板处理异常！", e);
		} catch (NullPointerException e) {
			logger.error("FreeMarker 处理失败. 空指针异常！", e);
			throw new Exception("FreeMarker 处理失败. 空指针异常！", e);
		}
	}

	public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
		this.freemarkerConfiguration = freemarkerConfiguration;
	}

}
