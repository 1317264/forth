import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;


public class MovieCrawler {
    public static void main(String[] args) {
        Mysql mysql =new Mysql();
        int i,j;
        int start=1292047;
        int end=1292067;//爬取电影id范围
        for(i=start;i<=end;i++){
            String strurl="https://movie.douban.com/subject/"+String.valueOf(i)+"/";
            try{
                HttpsUrlValidator.retrieveResponseFromServer(strurl);//忽略证书
                Document doc= Jsoup.connect(strurl)
                        .userAgent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; MALC)")
                        .timeout(999999999)
                        .get();
                //下载电影封面
                System.out.println(strurl);
                Elements elements=doc.select("[class=nbgnbg]>img");
                String imgurl=elements.attr("src");
                String fileName = imgurl.substring(imgurl.lastIndexOf('/') + 1, imgurl.length());
                java.io.File file=new File("D:\\crawlerdownload\\moviecover\\"+fileName);
                try{
                    URL url1=new URL(imgurl);
                    URLConnection conn=url1.openConnection();
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
                //获取部分数据存到数据库
                elements=doc.select("[property=v:itemreviewed]");
                String title=elements.first().text().replace("'","\'");
                elements=doc.select("[property=v:runtime]");
                String time;
                if(elements.isEmpty())
                    time=null;
                else
                    time=elements.first().text();
                elements=doc.select("[class=rating_sum]");
                if(!elements.first().text().equals("暂无评分")){
                    elements=doc.select("[property=v:average]");
                    Double score=Double.parseDouble(elements.first().text());
                    elements=doc.select("[property=v:votes]");
                    int scorenum=Integer.parseInt(elements.first().text());
                    elements=doc.select("[class=rating_per]");
                    String star[] = new String[5];
                    j=0;
                    for(Element element:elements){
                        star[j]=new String();
                        star[j]=element.text();
                        j++;
                    }
                    mysql.SaveMovie(i,file,title,time,score,scorenum,star[0],star[1],star[2],star[3],star[4]);
                }
                else{
                    mysql.SaveMovie(i,file,title,time,null,null,null,null,null,null,null);
                }
                //获取导演、编剧、演员数据存到数据库
                elements=doc.select("[id=info]>span");
                for(Element element:elements){
                    Elements elements1=element.select(">span").select("[class=pl]");
                    if(elements1.text().equals("导演")){
                        Elements elements2=doc.select("[rel=v:directedBy]");
                        for(j=0;j<elements2.size();j++) {
                            String director = elements2.get(j).text().replace("'","\'");
                            mysql.SaveDirector(i,j+1,director);
                        }
                        continue;
                    }
                    else if(elements1.text().equals("编剧")){
                        Elements elements2=element.select("[class=attrs]>a");
                        for(j=0;j<elements2.size();j++) {
                            String screenwriter = elements2.get(j).text().replace("'","\'");
                            mysql.SaveScreenwriter(i,j+1,screenwriter);
                        }
                        continue;
                    }
                    else if(elements1.text().equals("主演")){
                        Elements elements2=doc.select("[rel=v:starring]");
                        for(j=0;j<elements2.size();j++) {
                            String actor = elements2.get(j).text().replace("'","\'");
                            mysql.SaveActor(i,j+1,actor);
                        }
                        continue;
                    }
                }
                System.out.println("完成");
            }catch (Exception e){
                System.out.println("电影不存在");
                continue;//若id对应的电影不存在则继续
            }
        }
    }
}
