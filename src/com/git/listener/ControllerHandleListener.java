package com.git.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * 服务器启动，执行ControllerHandlerListener,扫描Contrtoller所在包,<br/>
 * 生成MethodHandlerMap并放入ServletContext中
 * @author 92432
 *
 */
public class ControllerHandleListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("git:Servlet Context destoryed!");
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		System.out.println("git:Servlet Context created!");
		String contextConfigLocation = servletContextEvent.getServletContext().getInitParameter("contextConfigLocation");
		try {
			DocumentBuilderFactory domfac=DocumentBuilderFactory.newInstance();
			DocumentBuilder dombuilder = domfac.newDocumentBuilder();
			String basePath = servletContextEvent.getServletContext().getRealPath("\\");
			InputStream is=new FileInputStream(new File(servletContextEvent.getServletContext().getRealPath("\\") + "WEB-INF\\" + contextConfigLocation));
			Document doc=dombuilder.parse(is);
			Element root=doc.getDocumentElement();
			NodeList dbinfo=root.getChildNodes();
			if(dbinfo!=null){
				for(int i=0;i<dbinfo.getLength();i++){
					Node db=dbinfo.item(i);
					for(Node node=db.getFirstChild();node!=null;node=node.getNextSibling()){
						if(node.getNodeType()==Node.ELEMENT_NODE){
							System.out.println(node.getNodeName());
						}
					}
				}
			}
			         
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		servletContextEvent.getServletContext().setAttribute("methodHandlerMap", ControllerHandler.handle(controllerPackageName));
	}

}
