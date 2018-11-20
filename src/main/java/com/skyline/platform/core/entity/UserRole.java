package com.skyline.platform.core.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_userRole", uniqueConstraints = {@UniqueConstraint(columnNames = {"roleId", "userId"})})
public class UserRole {
    private String id;
    private User user;
    private Role role;

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "userRole_id")
    @GenericGenerator(name = "userRole_id", strategy = "uuid")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(targetEntity=User.class, fetch=FetchType.LAZY)
    @JoinColumn(name="userId", referencedColumnName="id", nullable=false, foreignKey=@ForeignKey(name="FK_User"))
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(targetEntity=Role.class, fetch=FetchType.LAZY)
    @JoinColumn(name="roleId", referencedColumnName="id", nullable=false, foreignKey=@ForeignKey(name="FK_Role"))
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
