package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URLEncoder;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.File;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;

public class Song extends JPanel {
	// Pinao 的 谱夹
	private Image spectrum_clip_image;
	//钢琴谱baseUrl
	private String baseUrl="http://soso.gangqinpu.com/tosearch.aspx?";
	private int pageCount;  //谱子总页数
	private int pageCur=0;//当前的谱子页数
	//放谱子的面板
	private SongPanel songPanel;
	//放按钮的面板
	private JPanel buttonPanel;
	private JButton nextPage;
	private JButton lastPage;
	private JLabel currentPage;
	private int buttonWidth=50;
	private int buttonHeight=50;
	private String inputType;
	private String[] type= {".jpg",".png",".gif"};
	public Song(String songName,String inputType) {
		//
		this.inputType = inputType;
		File file = new File("resources/images/"+songName);
		//
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		lastPage = new JButton();
		nextPage = new JButton();
		currentPage = new JLabel(  " Page "+ (pageCur+1)  );
		currentPage.setFont(new java.awt.Font("Serif",3,15));
		lastPage.setToolTipText("last page");
		nextPage.setToolTipText("next page");
		lastPage.setSize(buttonWidth, buttonHeight);
		nextPage.setSize(buttonWidth, buttonHeight);
		lastPage.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		nextPage.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		setIcon("resources/images/lastPage.png",lastPage);
		setIcon("resources/images/nextPage.png",nextPage);
		lastPage.setLocation(10, 10); //应当更改按钮位置，但是此方法无用
		nextPage.setLocation(700,10);
		lastPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(pageCur>0)
				{
					pageCur--;
					changeSong("resources/images/"+songName+"/"+ pageCur +inputType);
					currentPage.setText("Page"+(pageCur+1));
				}
				repaint();
			}
		});
		
		nextPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(pageCur<pageCount)
				{
					pageCur++;
					changeSong("resources/images/"+songName+"/"+ pageCur +inputType); 
					currentPage.setText("Page"+(pageCur+1));
				}
				repaint();
			}
		});
		if (!(file.exists()&&file.isDirectory())) {
			try {
				searchSong(songName);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else {
			
			File file_may = new File("resources/images/"+songName+"/"+ pageCur +inputType);
			while(file_may.exists()) {
				pageCur++;
				file_may = new File("resources/images/"+songName+"/"+ pageCur +inputType);
			}
			pageCur--;
			pageCount = pageCur;
			pageCur = 0;
			
			
		}
		buttonPanel.add(lastPage);
		buttonPanel.add(currentPage);
		buttonPanel.add(nextPage);
		

		//创建谱夹
		songPanel = new SongPanel();
		songPanel.setLayout(new FlowLayout());
		songPanel.setPreferredSize(new Dimension(800,920));
		spectrum_clip_image=new ImageIcon("resources/images/"+songName+"/"+ pageCur +inputType).getImage();  
		setPreferredSize(new Dimension(800, 950));//为了适应pack(),否则pack()将失效
		setFocusable(true); 
		
		setLayout(new BorderLayout());
		add(buttonPanel,BorderLayout.SOUTH);
		add(songPanel,BorderLayout.NORTH);
		
	}

	public void setIcon(String file, JButton iconButton) {  
        ImageIcon icon = new ImageIcon(file);  
        Image temp = icon.getImage().getScaledInstance(iconButton.getWidth(),  
                iconButton.getHeight(), icon.getImage().SCALE_DEFAULT);  
        icon = new ImageIcon(temp);  
        iconButton.setIcon(icon);  
    } 
	public void changeSong(String file)
	{
		spectrum_clip_image= new ImageIcon(file).getImage();

	}

	private class SongPanel extends JPanel{
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(spectrum_clip_image, 0, 0,800,920, this);
		}
	}
	public void searchSong(String songName) throws Exception {
		String s1 = URLEncoder.encode(songName, "gb2312");//按gb2312格式编码
		//baseUrl = baseUrl +"searchname="+ s1+ "&searchtype=1&subjectsearch=true";  //包含关键字即可搜到
		baseUrl = baseUrl +"searchname="+ s1+ "&searchtype=1&shows=1";  //关键字完全匹配
	
		Connection con = Jsoup.connect(baseUrl);// 获取连接
		con.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");// 配置模拟浏览器
		Document d1 = con.get();
		
		Element elementLink = d1.getElementsByClass("search_url").first(); //已经找到的曲目的链接
		Elements elements = elementLink.getElementsByTag("a");
		String searched_url = elements.first().attr("href");	

		DownloadSong downloadSong = new DownloadSong(searched_url,songName,inputType);
		pageCount=downloadSong.getCount();
	}
	
}
