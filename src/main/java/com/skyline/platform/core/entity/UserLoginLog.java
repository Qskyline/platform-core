package com.skyline.platform.core.entity;

import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_userLoginLog")
public class UserLoginLog {
    private String id;
    private Date latestLoginTime;
    private Date latestAttemptLoginTime;
    private int count;
    private User user;

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "userLoginLog_id")
    @GenericGenerator(name = "userLoginLog_id", strategy = "uuid")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Type(type = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)  //define the precision of returning value
    @Column(name = "latestLoginTime", updatable = true, nullable = true)
    public Date getLatestLoginTime() {
        return latestLoginTime;
    }
    public void setLatestLoginTime(Date latestLoginTime) {
        this.latestLoginTime = latestLoginTime;
    }

    @Type(type = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)  //define the precision of returning value
    @Column(name = "latestAttemptLoginTime", updatable = true, nullable = true)
    public Date getLatestAttemptLoginTime() {
        return latestAttemptLoginTime;
    }
    public void setLatestAttemptLoginTime(Date latestAttemptLoginTime) {
        this.latestAttemptLoginTime = latestAttemptLoginTime;
    }

    @Type(type = "int")
    @Column(name = "count", updatable = true, nullable = false)
    @ColumnDefault(value = "0")
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    @OneToOne(targetEntity=User.class, fetch=FetchType.LAZY)
    @JoinColumn(name="userId", referencedColumnName="id", nullable=false, foreignKey=@ForeignKey(name="FK_UserId_UserLoginLog_User"))
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
