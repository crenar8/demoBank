package com.demo.bank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExecuteMoneyTransferRequestDto implements Serializable {

    private static final long serialVersionUID = -3042732561142344047L;

    @JsonProperty(required = true)
    private Long accountId;
    @JsonProperty(required = true)
    private String receiverName;
    @JsonProperty(required = true)
    private String description;
    @JsonProperty(required = true)
    private String currency;
    @JsonProperty(required = true)
    private String amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty(required = true)
    private Date executionDate;

}
