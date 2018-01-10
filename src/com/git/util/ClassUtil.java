package com.git.util;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.jar.JarFile;

public class ClassUtil {
	
//	public static Set<String> getClassName(String packageName, boolean isRecursion) {
//        Set<String> classNames = null;
//        ClassLoader loader = Thread.currentThread().getContextClassLoader();
//        String packagePath = packageName.replace(".", "/");
//
//        URL url = loader.getResource(packagePath);
//        if (url != null) {
//            String protocol = url.getProtocol();
//            if (protocol.equals("file")) {
//                classNames = getClassNameFromDir(url.getPath(), packageName, isRecursion);
//            } else if (protocol.equals("jar")) {
//                JarFile jarFile = null;
//                try{
//                    jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
//                } catch(Exception e){
//                    e.printStackTrace();
//                }
//                
//                if(jarFile != null){
//                    getClassNameFromJar(jarFile.entries(), packageName, isRecursion);
//                }
//            }
//        } else {
//            /*从所有的jar包中查找包名*/
//            classNames = getClassNameFromJars(((URLClassLoader)loader).getURLs(), packageName, isRecursion);
//        }
//        
//        return classNames;
//    }
}
