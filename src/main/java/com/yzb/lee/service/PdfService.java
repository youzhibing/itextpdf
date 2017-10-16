package com.yzb.lee.service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.yzb.lee.util.TemplateUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class PdfService {

	public String getContent() throws IOException {

		Map<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put("xy_no", "11");
		valueMap.put("jklist", "11");

		valueMap.put("jf_loginName", "12");
		valueMap.put("jf_name", "11");
		valueMap.put("jf_card", "11");
		valueMap.put("yf_loginName", "11");

		valueMap.put("yf_card", "11");
		valueMap.put("yf_realName", "11");
		valueMap.put("sf", "11");
		valueMap.put("bf_companyName", "11");
		valueMap.put("bf_address", "11");

		valueMap.put("yq_rate", "11");
		valueMap.put("wyj_rate", "11");

		valueMap.put("jk_jkyt", "11");
		valueMap.put("jk_money_xx", "11");
		valueMap.put("jk_money_dx", "11");

		valueMap.put("jk_zmoney", "11");
		valueMap.put("jk_zmoney_dx", "11");

		valueMap.put("bdxq_type", "11");

		valueMap.put("jk_rate", "11");

		valueMap.put("jk_jkqx", "11");
		valueMap.put("jk_dqr", "11");
		valueMap.put("jk_hkqs", "11");
		valueMap.put("jk_hkr", "11");
		valueMap.put("jk_ksr", "11");

		valueMap.put("hklist", "11");
		valueMap.put("site_name", "11");
		valueMap.put("site_domain", "11");
		valueMap.put("jxts", "11");

		Configuration cfg = new Configuration();
		Template template = new Template("四方借款协议", TemplateUtil.getTemplate(), cfg);
		
		String htmlContent = "";
		try {
			Writer writer = new StringWriter();  
            //数据填充模板  
            template.process(valueMap, writer);  
            //设置输出文件内容及路径  
            htmlContent = writer.toString();  
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return htmlContent;
	}

	public String getName() {
		return "四方借款协议";
	}
	
}
