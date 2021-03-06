package com.tsit.ocr.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class FileUtil {

    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file,File tempFile){
        InputStream ins = null;
        try {
            ins = file.getInputStream();
            inputStreamToFile(ins, tempFile);
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file){
        File tempFile = null;
        InputStream ins = null;
        try {
            ins = file.getInputStream();
            inputStreamToFile(ins, tempFile);
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }

    public static  List<String> exchangeList(List<String> list,int from,int to) {
        if(from == to)return list;
        String s = list.get(from);
        for(int i = list.size()-1;i>to;i--){
            list.set(i,list.get(i-1));
        }
        list.set(to,s);
        return list;
    }


    public static FileOutputStream openOutputStream(File file) throws IOException {

        if (file.exists()) {

            if (file.isDirectory()) {

                throw new IOException("File'" + file + "' exists but is a directory");

            }

            if (file.canWrite() == false) {

                throw new IOException("File '" + file + "' cannot be written to");

            }

        } else {

            File parent = file.getParentFile();

            if (parent != null &&parent.exists() == false) {

                if (parent.mkdirs() ==false) {

                    throw new IOException("File '" + file + "' could not be created");

                }

            }

        }

        return new FileOutputStream(file);

    }

    private static String[] toSuffixes(String[] extensions) {

        String[] suffixes = new String[extensions.length];

        for (int i = 0; i <extensions.length; i++) {

            suffixes[i] = "." +extensions[i];

        }

        return suffixes;

    }

    /**
     * 读取文本文件的内容
     *
     * @param curfile
     *            文本文件路径
     * @return 返回文件内容
     */
    public static String readFile(String curfile) {
        File f = new File(curfile);
        try {
            if (!f.exists())
                throw new Exception();
            FileReader cf = new FileReader(curfile);
            BufferedReader is = new BufferedReader(cf);
            StringBuilder filecontent = new StringBuilder("");
            String str = is.readLine();
            while (str != null) {
                filecontent.append(str);
                str = is.readLine();
                if (str != null)
                    filecontent.append(System.getProperty("line.separator","\n"));
            }
            is.close();
            cf.close();
            return filecontent.toString();
        } catch (Exception e) {
            System.err.println("不能读属性文件: " + curfile + " \n" + e.getMessage());
            return "";
        }

    }
    /**
     * 取指定文件的扩展名
     *
     * @param filePathName
     *            文件路径
     * @return 扩展名
     */
    public static String getFileExt(String filePathName) {
        int pos = 0;
        pos = filePathName.lastIndexOf('.');
        if (pos != -1)
            return filePathName.substring(pos + 1, filePathName.length());
        else
            return "";

    }

    /**
     * 读取文件大小
     *
     * @param filename
     *            指定文件路径
     * @return 文件大小
     */
    public static int getFileSize(String filename) {
        try {
            File fl = new File(filename);
            int length = (int) fl.length();
            return length;
        } catch (Exception e) {
            return 0;
        }

    }




    public static List<Map<String,Object>> testGetSegmentedRegions() throws Exception {
        File imageFile = new File("D:/3.png");
        ITesseract instance = new Tesseract();
        instance.setDatapath("D:/reader-server/Tesseract-OCR/tessdata"); //相对目录，这个时候tessdata目录和src目录平级
        instance.setLanguage("chi_sim");//选择字库文件（只需要文件名，不需要后缀名）
        BufferedImage bi = ImageIO.read(imageFile);
        int level = ITessAPI.TessPageIteratorLevel.RIL_SYMBOL;
        List<Word> words = instance.getWords(bi, level);
        List<Map<String,Object>> list = new ArrayList<>();
        for(Word w : words){
            Map<String,Object> data = new HashMap<>();
            data.put("word",w.getText());
            data.put("width",w.getBoundingBox().width);
            data.put("height",w.getBoundingBox().height);
            data.put("x",w.getBoundingBox().x);
            data.put("y",w.getBoundingBox().y);
            list.add(data);
        }
        return list;
    }


    public static void main(String[] args) {
        try {
            testGetSegmentedRegions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
