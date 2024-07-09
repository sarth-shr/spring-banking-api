package com.example.foneproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int id;

    @Column(name = "transaction_date")
    private Date date;

    @Column(name = "transaction_type")
    private String type;

    @Column(name = "transaction_amount")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "from_acc_id", foreignKey = @ForeignKey(name = "fk_source_acc_id"))
    private Account fromAccount;

    @Column(name = "from_acc_old_balance")
    private Integer fromAccOldBalance;

    @Column(name = "from_acc_new_balance")
    private Integer fromAccNewBalance;

    @ManyToOne
    @JoinColumn(name = "to_acc_id", foreignKey = @ForeignKey(name = "fk_target_acc_id"))
    private Account toAccount;

    @Column(name = "to_acc_old_balance")
    private Integer toAccOldBalance;

    @Column(name = "to_acc_new_balance")
    private Integer toAccNewBalance;

}
