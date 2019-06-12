package com.vlog.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "vlog_diary")
public class VlogDiary {
    @Id
    private String id;

    private String title;

    private String content;

    private String classify;

    private Date time;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return classify
     */
    public String getClassify() {
        return classify;
    }

    /**
     * @param classify
     */
    public void setClassify(String classify) {
        this.classify = classify;
    }

    /**
     * @return time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time
     */
    public void setTime(Date time) {
        this.time = time;
    }
}