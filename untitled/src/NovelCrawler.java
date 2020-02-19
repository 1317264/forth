import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class NovelCrawler {
    public static void main(String[] args) {
        Mysql mysql=new Mysql();
        int start=1;
        int end=10;//爬取小说id范围
        for(int i=start;i<=end;i++)
        {
            String strurl="https://www.52bqg.com/book_"+String.valueOf(i)+"/";
            System.out.println(strurl);
            try{
                Document doc=Jsoup.connect(strurl)
                        .userAgent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; MALC)")
                        .timeout(999999999)
                        .get();
                Elements elements=doc.select("[class=blocktitle]");
                if(elements.isEmpty()){
                    //下载小说内容，将各章标题和内容存到数据库
                    elements=doc.select("[id=list]>dl>dd>a");
                    for(int j=0;j<10;j++){//此处下载10章，若需储存整部小说则将10改为elements1.size()
                        if(j<elements.size()){
                            String href=elements.get(j).attr("href");
                            Document doc1=Jsoup.connect(strurl+href)
                                    .userAgent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; MALC)")
                                    .timeout(999999999)
                                    .get();
                            Elements elements1=doc1.select("[id=content]");
                            String text=elements1.text().replace(" ","\n").replace("'","\'");
                            File file=new File("D:\\crawlerdownload\\novelcontent\\"
                                    +String.valueOf(i)+"_"+String.valueOf(j+1)+".txt");
                            FileWriter fw=new FileWriter(file);
                            fw.write(text);
                            fw.close();

                            elements1=doc1.select("[class=bookname]>h1");
                            String title=elements1.first().text().replace("'","\'");
                            mysql.SaveChapter(i,j+1,title,file);
                        }
                    }
                    //下载小说封面
                    elements=doc.select("[id=fmimg]>img");
                    String imgurl=elements.attr("src");
                    String fileName = imgurl.substring(imgurl.lastIndexOf('/') + 1, imgurl.length());
                    File file=new File("D:\\crawlerdownload\\novelcover\\"+fileName);
                    try{
                        URL url=new URL(imgurl);
                        URLConnection conn=url.openConnection();
                        conn.setConnectTimeout(10 * 1000);
                        InputStream in = conn.getInputStream();
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
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
                    //获取其他数据存到数据库
                    elements=doc.select("[id=info]>h1");
                    String title=elements.first().text().replace("'","\'");
                    elements=doc.select("[id=info]>p>a");
                    String writer=elements.first().text().replace("'","\'");
                    elements=doc.select("[id=intro]");
                    String introduction=elements.first().text().replace("'","\'");
                    mysql.SaveNovel(i,title,writer,introduction,file);
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
