package com.softalchemy.accounts.service.impl;

import com.softalchemy.accounts.constants.AccountsConstants;
import com.softalchemy.accounts.dto.AccountsDto;
import com.softalchemy.accounts.dto.CustomerDto;
import com.softalchemy.accounts.entity.Accounts;
import com.softalchemy.accounts.entity.Customer;
import com.softalchemy.accounts.exception.CustomerAlreadyExistsException;
import com.softalchemy.accounts.exception.ResourceNotFoundException;
import com.softalchemy.accounts.mapper.AccountsMapper;
import com.softalchemy.accounts.mapper.CustomerMapper;
import com.softalchemy.accounts.repository.AccountsRepository;
import com.softalchemy.accounts.repository.CustomerRepository;
import com.softalchemy.accounts.service.IAccountsService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountsService {

  @Autowired
  private AccountsRepository accountsRepository;
  @Autowired
  private CustomerRepository customerRepository;


  /**
   * @param customerDto
   */
  @Override
  @Transactional
  public void createAccount(CustomerDto customerDto) {

    Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
    Optional<Customer> existingCustomer = customerRepository.findByMobileNumber(
        customer.getMobileNumber());
    if (existingCustomer.isPresent()) {

      throw new CustomerAlreadyExistsException(
          "Customer with mobileNum : " + customer.getMobileNumber() + " already present !");
    }
    Customer savedCustomer = customerRepository.save(customer);
    accountsRepository.save(createNewAccount(customer));

  }

  private Accounts createNewAccount(Customer customer) {
    Accounts newAccount = new Accounts();
    newAccount.setCustomerId(customer.getCustomerId());
    long randomNumber = 1000000000L + new Random().nextInt(900000000);

    newAccount.setAccountNumber(randomNumber);
    newAccount.setAccountType(AccountsConstants.SAVINGS);
    newAccount.setBranchAddress(AccountsConstants.ADDRESS);

    return newAccount;
  }

  @Override
  public CustomerDto getAccountDetails(String mobileNum) {

    Customer customer = customerRepository.findByMobileNumber(mobileNum).
        orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNum));

    Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
        () -> new ResourceNotFoundException("Accounts", "Mobile number", mobileNum)
    );

    CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());

    AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());

    customerDto.setAccountsDto(accountsDto);

    return customerDto;
  }

  @Override
  public boolean updateAccountDetails(CustomerDto customerDto) {
    boolean isUpdated = false;
    AccountsDto accountsDto = customerDto.getAccountsDto();

    if (accountsDto != null) {

      Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber())
          .orElseThrow(() ->
              new ResourceNotFoundException("Accounts", "Account number",
                  accountsDto.getAccountNumber().toString()));

      AccountsMapper.mapToAccounts(accountsDto, accounts);

      accounts = accountsRepository.save(accounts);

      Long customerId = accounts.getCustomerId();

      Customer customer = customerRepository.findById(customerId).orElseThrow(
          () -> new ResourceNotFoundException("Customer", "CustomerId", customerId.toString()));

      CustomerMapper.mapToCustomer(customerDto, customer);

      customerRepository.save(customer);
    }
    isUpdated = true;
    return true;
  }

  @Override
  public boolean deleteAccount(String mobileNumber) {

    Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() ->
        new ResourceNotFoundException("Customer", "MobileNumber", mobileNumber));

    accountsRepository.deleteByCustomerId(customer.getCustomerId());

    customerRepository.deleteById(customer.getCustomerId());

    return true;
  }
}
