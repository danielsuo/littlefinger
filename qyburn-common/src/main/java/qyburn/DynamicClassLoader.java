// package qyburn.common;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class DynamicClassLoader {
  public static void main(String[] args) {
    System.out.println("Hello, world!");

    printClassPaths();

    try {
      addClassPath("/Users/danielsuo/Dropbox/qyburn/qyburn-common/target/qyburn-common-1.0-SNAPSHOT.jar");

    } catch (Exception e) {
      System.out.println("Welp");
    }

    printClassPaths();

    try {
      loadClass("qyburn.common.DemoTask");
    } catch (ClassNotFoundException e) {
      System.out.println("Class not found!");
    }
  }

  // TODO: This is some seriously egregious stuff.
  public static void addClassPath(String classPath) throws Exception {
      File file = new File(classPath);
      URI url = file.toURI();
      URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
      Class<URLClassLoader> urlClass = URLClassLoader.class;
      Method method = urlClass.getDeclaredMethod("addURL", new Class[]{ URL.class });
      method.setAccessible(true);
      method.invoke(urlClassLoader, new Object[]{ url.toURL() });
  }

  public static ArrayList<URL> getClassPathsHelper(ClassLoader cl) {
    if (cl == null) {
      return new ArrayList<URL>();
    } else {
      ArrayList<URL> URLs = new ArrayList<URL>(Arrays.asList(((URLClassLoader) cl).getURLs()));
      ArrayList<URL> parentURLs = getClassPathsHelper(cl.getParent());
      URLs.addAll(parentURLs);
      return URLs;
    }
  }

  public static ArrayList<URL> getClassPaths() {
    return getClassPathsHelper(ClassLoader.getSystemClassLoader());
  }

  public static void printClassPaths() {
    ArrayList<URL> classPaths = getClassPaths();
    for (URL classPath : classPaths) {
      System.out.println(classPath);
    }
  }

  // className: fully qualified class name
  public static Class<?> loadClass(String className) throws ClassNotFoundException {
    return ClassLoader.getSystemClassLoader().loadClass(className);
  }
}
