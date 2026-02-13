package com.revature.toDoList.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="sub_tasks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subTaskId;
    @Column(nullable = false)
    private String Description;
    @Column(nullable = false)
    private boolean completed;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name="todo_id")
    private TodoItem todoItem;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
