package com.yzb.lee.generator;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.Map;

import com.yzb.lee.freemarker.FreemarkerConfiguration;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class HtmlGenerator {

	/**
	 * Generate html string.
	 * 
	 * @param template
	 *            the name of freemarker teamlate.
	 * @param variables
	 *            the data of teamlate.
	 * @return htmlStr
	 * @throws Exception
	 */
	public static String generate(String template, Map<String, Object> variables)
			throws Exception {
		Configuration config = FreemarkerConfiguration.getConfiguation();
		Template tp = config.getTemplate(template);
		StringWriter stringWriter = new StringWriter();
		BufferedWriter writer = new BufferedWriter(stringWriter);
		tp.setEncoding("UTF-8");
		tp.process(variables, writer);
		String htmlStr = stringWriter.toString();
		writer.flush();
		writer.close();
		return htmlStr;
	}
}
