package com.java.group8;

import java.io.Serializable;
import java.util.List;

/**
 * @author Li Yifei
 */

public class News implements Serializable{
    public String newsClassTag;
    public String news_ID;
    public String news_Source;
    public String news_Title;
    public String news_Time;
    public String news_URL;
    public String news_Author;
    public String lang_Type;
    public String news_Pictures;
    public String news_Video;
    public String news_Intro;
    public NewsDetail news_content = null;
    public boolean read;

    public News(String newsClassTag, String news_ID, String news_Source,
                String news_Title, String news_Time, String news_URL, String news_Author,
                String lang_Type, String news_Pictures, String news_Video, String news_Intro) {
        this.newsClassTag = newsClassTag;
        this.news_ID = news_ID;
        this.news_Source = news_Source;
        this.news_Title = news_Title;
        this.news_Time = news_Time;
        this.news_URL = news_URL;
        this.news_Author = news_Author;
        this.lang_Type = lang_Type;
        this.news_Pictures = news_Pictures;
        this.news_Video = news_Video;
        this.news_Intro = news_Intro;
        read = false;
        news_content = new NewsDetail();
    }

    public void addDetail() {
        /*TODO: ADD DETAILS TO THIS OBJECT*/
        read = true;
        news_content = new NewsDetail();
    }

    public class NewsDetail implements Serializable{
        public String seggedTitle;
        public int wordCountOfTitle;
        public int wordCountOfContent;
        public String inborn_KeyWords;
        public String news_Category;    // like "首页 > 新闻 > 环球扫描 > 正文"
        public String news_Content;   // content of news
        public String crawl_Source;
        public String news_Journal;
        public String crawl_Time;
        public String repeat_ID;

        public List<String> seggedPListOfContent = null;
        public List<Person> persons = null;
        public List<Location> locations = null;
        public List<String> organizations = null;
        public List<Keyword> Keywords = null;
        public List<Word> bagOfWords = null;

        public class Person {
            public String word;
            public int count;
            public Person(String n, int c) {
                word = n;
                count = c;
            }
            public Person(String n, String c) {
                this(n, Integer.parseInt(c));
            }
        }

        public class Location {
            public String word;
            public int count;
            public Location(String n, int c) {
                word = n;
                count = c;
            }
            public Location(String n, String c) {
                this(n, Integer.parseInt(c));
            }
        }

        public class Keyword {
            public String word;
            public double score;
            public Keyword(String n, double s) {
                word = n;
                score = s;
            }
            public Keyword(String n, String s) {
                this(n, Double.parseDouble(s));
            }
        }

        public class Word {
            public String word;
            public double score;
            public Word(String n, double s) {
                word = n;
                score = s;
            }
            public Word(String n, String s) {
                this(n, Double.parseDouble(s));
            }
        }
    }

}


