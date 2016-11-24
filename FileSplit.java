package com.company;
import javax.swing.JTextArea;
import java.net.*;
import java.io.*;

/**
 * Created by Vadon on 2016/9/12.
 */
public class FileSplit extends Thread{
    String downloadURL;
    long startPosition;
    long endPosition;
    int threadID;
    JTextArea textArea=new JTextArea();
    boolean isDone=false;
    RandomAccessFile random;
    public FileSplit(String downloadURL,String saveAs,long nStart,long nEnd,int id,JTextArea textArea){
        this.downloadURL=downloadURL;
        this.startPosition=nStart;
        this.endPosition=nEnd;
        this.threadID=id;
        this.textArea=textArea;
            try {
                random=new RandomAccessFile(saveAs,"rw");
                random.seek(startPosition);
            }catch (Exception e){
                System.out.println("创建随机对象出错："+e.getMessage());
            }
        }
        public void run(){
        try {
            URL url=new URL(downloadURL);
            HttpURLConnection httpConnection=(HttpURLConnection) url.openConnection();
            String sProperty="bytes="+startPosition+"-";
            httpConnection.setRequestProperty("RANGE",sProperty);
            textArea.append("\n线程"+threadID+"下载文件！ 请稍等》》》");
            InputStream input=httpConnection.getInputStream();
            byte[] buf=new byte[1024];
            int splitSpace;
            splitSpace=(int)endPosition-(int)startPosition;
            if (splitSpace>1024)
                splitSpace=1024;

            while (input.read(buf,0,splitSpace)>0 && startPosition<endPosition){
                splitSpace=(int)endPosition-(int)startPosition;
                if (splitSpace>1024)
                    splitSpace=1024;
                textArea.append("\n线程"+threadID+"开始位置："+startPosition+"，间隔长度："+splitSpace);
                random.write(buf,0,splitSpace);
                startPosition+=splitSpace;
            }
            textArea.append("\n线程"+threadID+"下载完毕！");
            random.close();
            input.close();
            isDone=true;
        }catch (Exception e){
            System.out.println("多线程下载文件出错："+e.getMessage());
        }

    }

}
