package com.softalchemy.accounts.service;

import com.softalchemy.accounts.dto.CustomerDto;

public interface IAccountsService {

    /**
     *
     * @param customerDto
     */

    void createAccount(CustomerDto customerDto);


    CustomerDto getAccountDetails(String mobileNumber);

    boolean updateAccountDetails(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);
}
