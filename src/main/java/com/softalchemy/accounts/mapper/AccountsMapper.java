package com.softalchemy.accounts.mapper;

import com.softalchemy.accounts.dto.AccountsDto;
import com.softalchemy.accounts.entity.Accounts;

import java.time.LocalDateTime;

public class AccountsMapper {

    public static AccountsDto mapToAccountsDto(Accounts accounts, AccountsDto accountsDto){

        accountsDto.setAccountNumber(accounts.getAccountNumber());
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setBranchAddress(accounts.getBranchAddress());

        return accountsDto;
    }

    public static Accounts mapToAccounts(AccountsDto accountsDto, Accounts accounts){
        accounts.setAccountNumber(accountsDto.getAccountNumber());
        accounts.setBranchAddress(accountsDto.getBranchAddress());
        accounts.setAccountType(accountsDto.getAccountType());
        return accounts;
    }


}
