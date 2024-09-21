package com.example.foneproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acc_id")
    private Integer id;

    @Column(name = "acc_number", unique = true)
    private String accNumber;

    @Column(name = "acc_type")
    private String type;

    @Column(name = "acc_balance")
    private Integer balance;

    @Column(name = "acc_interest")
    private Float interest;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "fk_customer_email"))
    private Customer customer;
}
