package com.chaoyang805.jokes.bean;

import java.io.Serializable;

/**
 * Created by chaoyang805 on 2015/9/13.
 */
public class Joke implements Serializable {

    public Joke() {
    }

    public Joke(int ID, String title, String content, int commentCount, String postDate) {
        mID = ID;
        mTitle = title;
        mContent = content;
        mCommentCount = commentCount;
        mPostDate = postDate;
    }

    /**
     * 笑话在数据库中的id;
     */
    private int mID;
    /**
     * 笑话标题
     */
    private String mTitle;
    /**
     * 笑话的内容
     */
    private String mContent;
    /**
     * 评论数量
     */
    private int mCommentCount;
    /**
     * 笑话的发表日期
     */
    private String mPostDate;

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(int commentCount) {
        mCommentCount = commentCount;
    }

    public String getPostDate() {
        return mPostDate;
    }

    public void setPostDate(String postDate) {
        mPostDate = postDate;
    }
}
