package com.demo.bank.util.transformer;

import com.demo.bank.data.TransactionEntity;
import com.demo.bank.dto.TransactionDto;
import com.demo.bank.dto.TransactionType;
import lombok.Setter;
import org.apache.commons.collections4.Transformer;

@Setter
public class TransactionDtoTransformer implements Transformer<TransactionEntity, TransactionDto> {

    @Override
    public TransactionDto transform(TransactionEntity transactionEntity) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setCurrency(transactionEntity.getCurrency());
        transactionDto.setAmount(transactionEntity.getAmount());
        transactionDto.setAccountingDate(transactionEntity.getAccountingDate());
        transactionDto.setDescription(transactionEntity.getDescription());
        transactionDto.setOperationId(transactionEntity.getOperationId());
        transactionDto.setTransactionId(transactionEntity.getTransactionId());
        TransactionType type = new TransactionType();
        type.setEnumeration(transactionEntity.getTypeEnumeration());
        type.setValue(transactionEntity.getTypeValue());
        transactionDto.setType(type);
        transactionDto.setValueDate(transactionEntity.getValueDate());
        return transactionDto;
    }
}
