package com.revature.toDoList.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @ManyToOne
    @JoinColumn(name="todo_id")
    private TodoItem todoItem;

}
