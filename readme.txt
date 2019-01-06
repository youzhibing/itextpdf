推荐阿里云服务器，点我领代金券：<a href="https://promotion.aliyun.com/ntms/yunparter/invite.html?userCode=djq51vv3" target="_blank">点我领取</a>

模板文件只是用于生成pdf， 所以双击打开的话， 图片和外部css是找不到的； 如果既想满足双击打开可以找到css和图片，又能通本工程中的示例， 那么需要另外处理了；至少本工程是没有兼容两者的

1、目前只实现pdf文件的生成，与web项目pdf的导出还有些许不同， 但是已经支持中文、外部css、图片
	可直接运行com.yzb.lee.test.Test.java的main方法来测试效果
	文件路径：pdf.css在resources/css/目录下，aloner.jpg在resources/images/目录下， 模板文件是resources/template/template.html
	获取java项目路径：http://blog.csdn.net/mydreamongo/article/details/8220577

2、servlet实现pdf的导出
	工程部署后，请求地址：http://localhost:8080/itextpdf/pdfServlet
	文件路径：pdf.css在webapp/css/目录下，aloner.jpg在webapp/images/目录下， 模板文件是resources/template/webTemplate.html
	此时css文件、image文件与工程部署在同一个web容器中， 文件路径的获取与之前有所不同
	
3、css、image等文件放到单独的文件服务器上
	出于简单的演示效果，文件服务器与工程服务器还是同一个， css与image的链接方式与之前有所不同，可以看成是两个服务器
	工程部署后，请求地址：http://localhost:8080/itextpdf/pdfDownloadServlet
	文件路径：pdf.css、aloner.jpg在文件服务器上，写文件服务器的访问路径， 模板文件是resources/template/fileTemplate.html
	
4、将字体-宋体 放到工程中， 以便适应不同操作系统的中文显示问题
	工程部署后，请求地址：http://localhost:8080/itextpdf/pdfFontServlet
	文件路径：pdf.css、aloner.jpg在文件服务器上，写文件服务器的访问路径， 模板文件是resources/template/fileTemplate.html
	字体文件路径：resources/font/目录下

5、阿里巴巴code review
	阿里巴巴代码检视后，代码优化；
	个人认为有可取的，但不是全部都可取
	eclipse插件地址：https://p3c.alibaba.com/plugin/eclipse/update
