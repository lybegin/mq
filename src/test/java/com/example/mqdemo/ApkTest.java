package com.example.mqdemo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.jooq.tools.StringUtils;
import org.springframework.util.StopWatch;

public class ApkTest {

  private static final Integer ttl = 3 * 1000;
  private static final String PATH = "/Users/admin/files/";

  public static void main(String[] args) {

    try {
      StopWatch watch = new StopWatch();
      watch.start();
      File file = createApkFileByUrl(
          "https://applx.yidianzixun.com/static/CmcRYVsGpzyAaPC3A3zZjbz2M4w979/48866766.apk");
      if (file != null) {
        ApkFile apkFile = new ApkFile(file);
        ApkMeta apkMeta = apkFile.getApkMeta();
        System.out.println(apkMeta.getPackageName());
        System.out.println("文件的大小:" + file.length());
        watch.stop();
        System.out.println("cost:" + watch.getTotalTimeSeconds());
      } else {
        watch.stop();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public static File createApkFileByUrl(String path) {

    if (StringUtils.isEmpty(path)) {
      return null;
    }
    String[] paths = path.split("/");

    if (paths.length <= 1) {
      return null;
    }

    String fileName = paths[paths.length - 1];

    String[] fileNames = fileName.split("\\.");

    if (fileNames.length != 2) {
      return null;
    }

    File file = null;
    try {
      file = File.createTempFile(fileNames[fileNames.length - 1], ".apk");
      URL url = new URL(path);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setReadTimeout(ttl);
      connection.setConnectTimeout(ttl);
      BufferedInputStream ins = new BufferedInputStream(connection.getInputStream());
      BufferedOutputStream ous = new BufferedOutputStream(new FileOutputStream(file));
      int c;
      while ((c = ins.read()) != -1) {
        ous.write(c);
      }
      ins.close();
      ous.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return file;
  }


  public static File createApkFileByUrlAndZip(String path) {

    if (StringUtils.isEmpty(path)) {
      return null;
    }
    String[] paths = path.split("/");
    String outPath = PATH + paths[paths.length - 1];

    File file = new File(outPath);
    try {
      URL url = new URL(path);

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setReadTimeout(ttl);
      connection.setConnectTimeout(ttl);
      connection.setRequestProperty("Content-Type", "application/vnd.android.package-archive");
      connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
      connection.setRequestProperty("Accept",
          "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
      connection.setRequestProperty("User-Agent",
          "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");

      ZipInputStream iins = new ZipInputStream(connection.getInputStream());

      ZipOutputStream ous = new ZipOutputStream(new FileOutputStream(file));

      ZipEntry inEntry;
      while ((inEntry = iins.getNextEntry()) != null) {

        ZipEntry outEntry = new ZipEntry(inEntry.getName());
        ous.putNextEntry(outEntry);

        int c;
        while ((c = iins.read()) != -1) {
          ous.write(c);
        }

      }
      ous.flush();
      iins.close();
      ous.close();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return file;
  }

  public static void unzip() throws Exception {
    String path = "https://app-ads.yidianzixun.com/static/CmcRYVpDiKCAT20MAHqG5Mp_tBA799/29464035.apk";
    String[] paths = path.split("/");
    String unzipFile = (PATH + paths[paths.length - 1]);
    String unzipDir = unzipFile.substring(0, unzipFile.length() - 4);
    URL url = new URL(path);
    URLConnection connection = url.openConnection();
    connection.setReadTimeout(ttl);
    connection.setConnectTimeout(ttl);
    ZipInputStream iins = new ZipInputStream(connection.getInputStream());
    ZipEntry entry;
    while ((entry = iins.getNextEntry()) != null) {

      String fileName = entry.getName();

      //有层级结构，就先创建目录
      String tmp;
      int index = fileName.lastIndexOf('/');
      if (index != -1) {
        tmp = fileName.substring(0, index);
        tmp = unzipDir + "/" + tmp;
        File f = new File(tmp);
        f.mkdirs();
      }

      //创建文件
      fileName = unzipDir + "/" + fileName;
      File file = new File(fileName);
      file.createNewFile();
      FileOutputStream out = new FileOutputStream(file);

      int c;
      while ((c = iins.read()) != -1) {
        out.write(c);
      }
      out.close();
    }
    iins.close();

    // zip(unzipDir, unzipFile);
  }


  private static void zip(ZipOutputStream out, File f, String base) throws Exception {
    if (f.isDirectory()) {
      File[] files = f.listFiles();
      base = (base.length() == 0 ? "" : base + "/");
      for (int i = 0; i < files.length; i++) {
        zip(out, files[i], base + files[i].getName());
      }
    } else {
      out.putNextEntry(new ZipEntry(base));
      BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
      int c;

      while ((c = in.read()) != -1) {
        out.write(c);
      }
      in.close();
    }
  }

  private static void zip(File inputFileName, String zipFileName) throws Exception {
    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
    zip(out, inputFileName, "");
    out.close();
  }

  //压缩文件，inputFileName表示要压缩的文件（可以为目录）,zipFileName表示压缩后的zip文件
  public static void zip(String inputFileName, String zipFileName) throws Exception {
    zip(new File(inputFileName), zipFileName);
  }

}
