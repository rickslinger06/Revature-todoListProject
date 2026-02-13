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
    private String description;
    @Column(nullable = false)
    private Boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name="todo_id")
    private TodoItem todoItem;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
