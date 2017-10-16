模板文件只是用于生成pdf， 所以双击打开的话， 图片和外部css是找不到的； 如果既想满足双击打开可以找到css和图片，又能通本工程中的示例， 那么需要另外处理了；至少本工程是没有兼容两者的

1、目前只实现pdf文件的生成，与web项目pdf的导出还有些许不同， 但是已经支持中文、外部css、图片
	可直接运行com.yzb.lee.test.Test.java的main方法来测试效果
	文件路径：pdf.css在resources/css/目录下，aloner.jpg在resources/images/目录下， 模板文件是resources/template/template.html
	获取java项目路径：http://blog.csdn.net/mydreamongo/article/details/8220577

2、servlet实现pdf的导出
	工程部署后，请求地址：http://localhost:8080/itextpdf/pdfServlet
	文件路径：pdf.css在webapp/css/目录下，aloner.jpg在webapp/images/目录下， 模板文件是resources/template/webTemplate.html
	此时css文件、image文件与工程部署在同一个web容器中， 文件路径的获取与之前有所不同