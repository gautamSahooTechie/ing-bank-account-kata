package com.ing.fr.app.models;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Data
public class CustomerDto implements Serializable {

    private static final long serialVersionUID = 919836451836282574L;
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Customer cif can be alphanumeric only")
    @NotNull
    @Length(max = 14, message = "Customer cif max length 14")
    private String customerCif;
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Customer name can be alphanumeric only")
    @NotNull
    @Length(max = 25, message = "Customer name max length 25")
    private String customerName;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    public CustomerDto() {
        // Do nothing because used for creating objects for bean.
    }

    public String getCustomerCif() {
        return customerCif;
    }

    public void setCustomerCif(String customerCif) {
        this.customerCif = customerCif;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
