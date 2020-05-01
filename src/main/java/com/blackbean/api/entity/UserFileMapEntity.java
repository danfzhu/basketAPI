package com.blackbean.api.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_file_map", schema = "public", catalog = "testing")
public class UserFileMapEntity {
    private int id;
    private int userId;
    private int fileId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "file_id")
    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFileMapEntity that = (UserFileMapEntity) o;
        return id == that.id &&
                userId == that.userId &&
                fileId == that.fileId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, fileId);
    }
}
