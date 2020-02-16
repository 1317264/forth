import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class NovelCrawler {
    public static void main(String[] args) {
        Mysql mysql=new Mysql();
        int start=1;
        int end=20;//爬取小说id范围
        for(int i=start;i<=end;i++)
        {
            String url1="https://www.52bqg.com/book_"+String.valueOf(i)+"/";
            System.out.println(url1);
            try{
                Document doc1=Jsoup.connect(url1)
                        .userAgent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; MALC)")
                        .timeout(999999999)
                        .get();
                Elements elements0=doc1.getElementsByClass("blocktitle");
                if(!elements0.text().equals("出现错误！")){
                    //下载小说内容，将各章标题和内容存到数据库
                    Elements elements1=doc1.select("[id=list]>dl>dd>a");
                    String href;
                    for(int j=0;j<20;j++){//此处下载20章，若需储存整部小说则将20改为elements1.size()
                        if(j<elements1.size()){
                            href=elements1.get(j).attr("href");
                            Document doc2=Jsoup.connect(url1+href).timeout(999999999).get();
                            Element element2=doc2.getElementById("content");
                            Elements elements2=element2.getElementsByTag("div");
                            String text=elements2.text().replace(" ","\n");
                            File file1=new File("D:\\crawlerdownload\\novelcontent\\"
                                    +String.valueOf(i)+"_"+String.valueOf(j+1)+".txt");
                            FileWriter fw=new FileWriter(file1);
                            fw.write(text);
                            fw.close();
                            mysql.SaveContent(i,j+1,file1);

                            Elements elements3=doc2.select("[class=bookname]>h1");
                            String title=elements3.text();
                            String sql2="insert into title values("+String.valueOf(i)+","+String.valueOf(j+1)+",'"+title+"')";
                            mysql.Save(sql2);
                        }
                    }
                    //下载小说封面
                    Elements elements2=doc1.select("[id=fmimg]>img");
                    String imgurl=elements2.attr("src");
                    String fileName = imgurl.substring(imgurl.lastIndexOf('/') + 1, imgurl.length());
                    File file2=new File("D:\\crawlerdownload\\novelcover\\"+fileName);
                    try{
                        URL url=new URL(imgurl);
                        URLConnection conn=url.openConnection();
                        conn.setConnectTimeout(10 * 1000);
                        InputStream in = conn.getInputStream();
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file2));
                        byte[] buf = new byte[1024];
                        int size;
                        while (-1 != (size = in.read(buf))) {
                            out.write(buf, 0, size);
                        }
                        out.close();
                        in.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //获取其他数据
                    Elements elements3=doc1.select("[id=info]>h1");
                    String title=elements3.text();
                    Elements elements4=doc1.select("[id=info]>p>a");
                    String writer=elements4.first().text();
                    Element element2=doc1.getElementById("intro");
                    String introduction=element2.text();
                    //存到数据库
                    mysql.SaveNovel(i,title,writer,introduction,file2);
                    System.out.println("完成");
                }
                else{
                    System.out.println("小说不存在");
                }
            }catch (IOException e) {
                System.out.println("失败");
                e.printStackTrace();
            }
        }
    }
}
