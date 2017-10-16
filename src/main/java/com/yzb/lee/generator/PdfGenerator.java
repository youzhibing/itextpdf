package com.yzb.lee.generator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.ReadingProcessor;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;
import com.itextpdf.tool.xml.pipeline.html.NoImageProviderException;

public class PdfGenerator {

	/**
	 * Output a pdf to the specified outputstream
	 * 
	 * @param htmlStr
	 *            the htmlstr
	 * @param out
	 *            the specified outputstream
	 * @throws Exception
	 */
	public static void generate(String htmlStr, OutputStream out)
			throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream(htmlStr
				.getBytes()));
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(doc, null);
		renderer.layout();
		renderer.createPDF(out);
		out.close();
	}
	
	public static void generate1(String htmlStr, OutputStream out) throws IOException, DocumentException {
		final String charsetName = "UTF-8";
		
		Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        document.setMargins(30, 30, 30, 30);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        
        // html内容解析
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(
                new CssAppliersImpl(new XMLWorkerFontProvider() {
                    @Override
                    public Font getFont(String fontname, String encoding,
                                        float size, final int style) {
                        if (fontname == null) {
                            fontname = "SimSun";        // 操作系统需要有该字体, 没有则需要安装; 当然也可以将字体放到项目中， 再从项目中读取
                        }
                        return super.getFont(fontname, encoding, size,
                                style);
                    }
                })) {
            @Override
            public HtmlPipelineContext clone()
                    throws CloneNotSupportedException {
                HtmlPipelineContext context = super.clone();
                try {
                    ImageProvider imageProvider = this.getImageProvider();
                    context.setImageProvider(imageProvider);
                } catch (NoImageProviderException e) {
                }
                return context;
            }
        };
        
        // 图片解析
        htmlContext.setImageProvider(new AbstractImageProvider() {
        	
        	String rootPath = PdfGenerator.class.getResource("/").getPath();
        	
            @Override
            public String getImageRootPath() {
                //return getServletContext().getRealPath("/");
            	return rootPath;
            }

            @Override
            public Image retrieve(String src) {
                if (StringUtils.isEmpty(src)) {
                    return null;
                }
                try {
                    Image image = Image.getInstance(new File(rootPath, src).toURI().toString());
                    image.setAbsolutePosition(400, 400);		//  图片位置
                    if (image != null) {
                        store(src, image);
                        return image;
                    }
                } catch (Throwable e) {
                	e.printStackTrace();
                }
                return super.retrieve(src);
            }
        });
        htmlContext.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
        
        // css解析
        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        cssResolver.setFileRetrieve(new FileRetrieve() {
            @Override
            public void processFromStream(InputStream in,
                                          ReadingProcessor processor) throws IOException {
                try (
                        InputStreamReader reader = new InputStreamReader(in, charsetName)) {
                    int i = -1;
                    while (-1 != (i = reader.read())) {
                        processor.process(i);
                    }
                } catch (Throwable e) {
                }
            }

            // 解析href
            @Override
            public void processFromHref(String href, ReadingProcessor processor) throws IOException {
            	InputStream is = PdfGenerator.class.getResourceAsStream("/" + href);
                try (InputStreamReader reader = new InputStreamReader(is,charsetName)) {
                    int i = -1;
                    while (-1 != (i = reader.read())) {
                        processor.process(i);
                    }
                } catch (Throwable e) {
                	e.printStackTrace();
                }
            }
        });

        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, writer));
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        XMLWorker worker = null;
        worker = new XMLWorker(pipeline, true);
        XMLParser parser = new XMLParser(true, worker, Charset.forName(charsetName));
        try (InputStream inputStream = new ByteArrayInputStream(htmlStr.getBytes())) {
            parser.parse(inputStream, Charset.forName(charsetName));
        }
        document.close();
	}
}
