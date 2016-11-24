
package com.company;

import javax.swing.JTextArea;
import java.net.*;

/**
 * Created by Vadon on 2016/9/12.
 */
public class DownLoadFile extends Thread {
    String downloadURL;
    String saveFileAs;
    int threadCount;
    String log = new String();
    JTextArea textArea = new JTextArea();
    long[] position;
    long[] startPosition;
    long[] endPosition;
    FileSplit[] FileSplitt;
    long fileLength;

    public DownLoadFile(String downloadURL, String saveFileAs, int threadCount, JTextArea textArea) {
        this.downloadURL = downloadURL;
        this.saveFileAs = saveFileAs;
        this.threadCount = threadCount;
        this.textArea = textArea;
        startPosition = new long[threadCount];
        endPosition = new long[threadCount];
    }

    public void run() {
        log = "目标文件：" + downloadURL;
        textArea.append("\n" + log);
        log = "\n线程总数：" + threadCount;
        textArea.append("\n" + log);
        try {
            fileLength = getFileSize();
            if (fileLength == -1) {
                textArea.append("\n 不可知的文件长度！请重试！！");
            } else {
                if (fileLength == -2) {
                    textArea.append("\n文件无法获取，没有找到制定资源，请重试！！");
                } else {
                    for (int i = 0; i < startPosition.length; i++) {
                        startPosition[i] = (long) (i * (fileLength / startPosition.length));
                    }
                    for (int i = 0; i < endPosition.length - 1; i++) {
                        endPosition[i] = startPosition[i + 1];
                    }
                    endPosition[endPosition.length - 1] = fileLength;
                    for (int i = 0; i < startPosition.length; i++) {
                        log = "线程：" + i + "下载范围：" + startPosition[i] + "--" + endPosition[i];
                        textArea.append("\n" + log);
                    }
                    FileSplitt = new FileSplit[startPosition.length];
                    for (int i = 0; i < startPosition.length; i++) {
                        FileSplitt[i] = new FileSplit(downloadURL, saveFileAs, startPosition[i], endPosition[i], i, textArea);
                        log = "线程" + i + "启动";
                        textArea.append("\n" + log);
                        FileSplitt[i].start();
                    }

                    boolean breakWhile = true;
                    while (breakWhile) {
                        Thread.sleep(500);
                        breakWhile = false;
                        for (int i = 0;i < FileSplitt.length;i++){
                            if (!FileSplitt[i].isDone) {
                                breakWhile = true;
                                break;
                            }
                        }
                    }
                    textArea.append("文件传输结束！");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public long getFileSize() {
        int fileLength = -1;
        try {
            URL url = new URL(downloadURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) (url.openConnection());
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode >= 400) {
                System.out.println("web服务器响应错误！");
                return -2;
            }
            String sHeader;
            for (int i = 1; ; i++) {
                sHeader = httpURLConnection.getHeaderFieldKey(i);
                if (sHeader != null) {
                    if (sHeader.equals("Content-Length")) {
                        fileLength = Integer.parseInt(httpURLConnection.getHeaderField(sHeader));
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("无法获取文件长度：" + e.getMessage());
        }

    return fileLength;
    }
}
