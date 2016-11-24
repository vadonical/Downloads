package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Created by Vadon on 2016/11/11.
 */
public class TeXunleiFrame extends JFrame {
    private JPanel contentPane;
    private JTextField webFiled = new JTextField();
    private JTextField localFile = new JTextField();
    private JButton button = new JButton();
    private JLabel webLabel = new JLabel();
    private JLabel localLabel = new JLabel();
    private JTextArea textArea = new JTextArea();
    private String downloadURL = new String();
    private String saveFileAs = new String();

    public TeXunleiFrame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            toInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void toInit() throws Exception {
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(null);
        this.setSize(new Dimension(380, 320));
        this.setLocation(100, 100);
        this.setTitle("仿迅雷多线程下载");
        webFiled.setBounds(new Rectangle(150, 200, 200, 20));

        webFiled.setText("ftp://ygdy8:ygdy8@y219.dydytt.net:9200/[阳光电影www.ygdy8.com].谍影重重5.BD.720p.中英双字幕.mkv");
        localFile.setBounds(new Rectangle(150, 240, 120, 20));
        localFile.setText("d:\\try.rar");
        webLabel.setBounds(new Rectangle(20, 200, 120, 20));
        webLabel.setText("下载的目标文件为：");
        localLabel.setBounds(new Rectangle(20, 240, 120, 20));
        localLabel.setText("下载的文件为：");
        button.setBounds(new Rectangle(280, 240, 60, 20));
        button.setText("下载 ");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button_actionPerformed(TeXunleiFrame.this, e);
            }
        });

        JScrollPane scrollpane = new JScrollPane(textArea);
        scrollpane.setBounds(new Rectangle(20, 20, 330, 170));
        textArea.setEnabled(false);
        contentPane.add(webFiled, null);
        contentPane.add(localFile, null);
        contentPane.add(webLabel, null);
        contentPane.add(localLabel, null);
        contentPane.add(button, null);
        contentPane.add(scrollpane, null);
        downloadURL = webFiled.getText();
        saveFileAs = localLabel.getText();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void button_actionPerformed(TeXunleiFrame teXunleiFrame, ActionEvent e) {
        teXunleiFrame.downloadURL = teXunleiFrame.webFiled.getText();
        teXunleiFrame.saveFileAs = teXunleiFrame.localFile.getText();
        if (teXunleiFrame.downloadURL.compareTo(" ") == 0)
            teXunleiFrame.textArea.setText("请输入要下载的文件完整地址");
        else if (teXunleiFrame.saveFileAs.compareTo("") == 0) {
            teXunleiFrame.textArea.setText("请输入保存文件完整地址");
        } else {
            try {
                DownLoadFile downFile = new DownLoadFile(teXunleiFrame.downloadURL, teXunleiFrame.saveFileAs, 5, teXunleiFrame.textArea);
                downFile.start();
                teXunleiFrame.textArea.append("主线程启动>>>");
            } catch (Exception ec) {
                System.out.println("下载文件出错：" + ec.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        TeXunleiFrame frame = new TeXunleiFrame();
        frame.setVisible(true);
    }

}
