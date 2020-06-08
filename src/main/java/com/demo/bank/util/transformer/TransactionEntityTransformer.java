package com.demo.bank.util.transformer;

import com.demo.bank.data.TransactionEntity;
import com.demo.bank.dto.TransactionDto;
import com.demo.bank.dto.TransactionType;
import lombok.Setter;
import org.apache.commons.collections4.Transformer;

@Setter
public class TransactionEntityTransformer implements Transformer<TransactionDto, TransactionEntity> {

    private Long accountId;

    @Override
    public TransactionEntity transform(TransactionDto transactionDto) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccountId(accountId);
        transactionEntity.setAccountingDate(transactionDto.getAccountingDate());
        transactionEntity.setAmount(transactionDto.getAmount());
        transactionEntity.setCurrency(transactionDto.getCurrency());
        transactionEntity.setDescription(transactionDto.getDescription());
        transactionEntity.setOperationId(transactionDto.getOperationId());
        transactionEntity.setTransactionId(transactionDto.getTransactionId());
        TransactionType type = transactionDto.getType();
        if (type != null) {
            transactionEntity.setTypeEnumeration(type.getEnumeration());
            transactionEntity.setTypeValue(type.getValue());
        }
        transactionEntity.setValueDate(transactionDto.getValueDate());
        return transactionEntity;
    }
}
