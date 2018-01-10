package com.git.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.git.annotation.Controller;
import com.git.annotation.Autowired;
import com.git.annotation.RequestMapping;
import com.git.annotation.Service;
import com.git.exception.SelfInjectionException;

public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = -7200153214315312177L;
	
	List<String> packageNames = new ArrayList<String>();
    // 所有类的实例，key是注解的value,value是所有类的实例
	//instanceMap包括<controller的value,controller的实例>和<service的名字（包名+类名）,service的实例>
    Map<String, Object> instanceMap = new HashMap<String, Object>();
    //handerMap包括<controller的value+method的value,method的实例>
    Map<String, Object> handerMap = new HashMap<String, Object>();
    
    public DispatcherServlet() {
        super();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        String context = req.getContextPath();
        String path = url.replace(context, "");
        //根据path获得Method
        Method method = (Method) handerMap.get(path);
        //获取该Method所属的Controller类名
        String className = method.getDeclaringClass().getName();
        //获取具体的Controller实例
        Object controller = instanceMap.get(className);
        try {
        	//调用具体Controller里的方法
            method.invoke(controller, new Object[] { req, resp, null });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	//获取spring-mvc.xml的位置
    	String contextConfigLocation = getInitParameter("contextConfigLocation");
        System.out.println("contextConfigLocation:"+contextConfigLocation);
        //包扫描,获取包中的文件
        scanPackage("com.git");
        try {
        	//过滤Controller和Service，并将其实例化
            filterAndInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //建立映射关系
        handerMap();
        //实现注入
        ioc();
    }
    
    /**
     * 将Controller和Service中用到Autowired标签的地方，注入相应实例
     */
    private void ioc() {
    	if (instanceMap.isEmpty())
            return;
        for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
        	
            //拿到里面的所有属性
            Field fields[] = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);//可访问私有属性
                if (field.isAnnotationPresent((Class<? extends Annotation>) Autowired.class)){
//                	Autowired autowired = field.getAnnotation(Autowired.class);
//                	String value = autowired.value();
                	String value = field.getType().getName();
	                field.setAccessible(true);
	                if(entry.getKey().equals(value)){
                		throw new SelfInjectionException(value + "里出现自我调用");
                	}
	                try {
	                	field.set(entry.getValue(), instanceMap.get(value));
	                } catch (IllegalArgumentException e) {
	                	e.printStackTrace();
	                } catch (IllegalAccessException e) {
	                	e.printStackTrace();
	                }
	            }
            }
        }
	}

	/**
     * 建立映射关系
     */
    private void handerMap() {
    	if (instanceMap.size() <= 0)
            return;
        for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
            if (entry.getValue().getClass().isAnnotationPresent(Controller.class)) {
//                Controller controller = (Controller) entry.getValue().getClass().getAnnotation(Controller.class);
                RequestMapping ctRm = (RequestMapping) entry.getValue().getClass().getAnnotation(RequestMapping.class);
                String ctRmvalue = ctRm.url();
                //如果首个字符不是“/”，则加上
                ctRmvalue = dealWith(ctRmvalue);
                Method[] methods = entry.getValue().getClass().getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent((Class<? extends Annotation>) RequestMapping.class)) {
                        RequestMapping rm = (RequestMapping) method.getAnnotation(RequestMapping.class);
                        String rmvalue = rm.url();
                        rmvalue = dealWith(rmvalue);
                        handerMap.put(ctRmvalue + rmvalue, method);
                    } else {
                        continue;
                    }
                }
            } else {
                continue;
            }
        }
	}
    
    /**
     * 如果首个字符不是“/”，则加上
     * @param ctRmvalue
     * @return
     */
	private String dealWith(String ctRmvalue) {
		ctRmvalue = ctRmvalue.trim();
		if(!ctRmvalue.substring(0, 1).equals("/")){
			ctRmvalue = "/" + ctRmvalue;
		}
		return ctRmvalue;
	}

	/**
     * 过滤Controller和Service并且实例化
     * @throws Exception
     */
    private void filterAndInstance() throws Exception {
    	if (packageNames.size() <= 0) {
            return;
        }
        for (String className : packageNames) {
        	//获得该Class
            Class<?> cName = Class.forName(className.replace(".class", "").trim());
            if (cName.isAnnotationPresent((Class<? extends Annotation>) Controller.class)) {	//Controller
                Object instance = cName.newInstance();
                Controller controller = (Controller) cName.getAnnotation(Controller.class);
//                String key = controller.value();
//                instanceMap.put(key, instance);
                //将该class名（包括包名）作为key
                String key = cName.getName();
                instanceMap.put(key, instance);
            } else if (cName.isAnnotationPresent((Class<? extends Annotation>) Service.class)) {	//Service
                Object instance = cName.newInstance();
//                Service service = (Service) cName.getAnnotation(Service.class);
//                String key = service.value();
//                instanceMap.put(key, instance);
                //获得该Service实现的接口名
                Class[] interfaces = cName.getInterfaces();
                instanceMap.put(interfaces[0].getName(), instance);
            } else {
                continue;
            }
        }
	}

	/**
     * 扫描包下的所有文件
     * @param Package
     */
	private void scanPackage(String Package) {
		URL url = this.getClass().getClassLoader().getResource("/" + replaceTo(Package));// 将所有的.转义获取对应的路径
        String pathFile = url.getFile();
        File file = new File(pathFile);
        String fileList[] = file.list();
        for (String path : fileList) {
            File eachFile = new File(pathFile + path);
            if (eachFile.isDirectory()) {
                scanPackage(Package + "." + eachFile.getName());
            } else {
                packageNames.add(Package + "." + eachFile.getName());
            }
        }
	}

	private String replaceTo(String path) {
		return path.replaceAll("\\.", "/");
	}
}
