package com.solvd.laba;

import com.solvd.laba.domain.Credential;
import com.solvd.laba.service.impl.CredentialServiceImpl;
import com.solvd.laba.service.interfaces.CredentialService;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class CredentialServiceTest {
    private CredentialService credentialService;

    @BeforeSuite
    public void setUp() {
        credentialService = new CredentialServiceImpl();
    }
    @DataProvider
    public Object[][] validCredentials() {
        return new Object[][] {
                {"1234567890", "1234"},
                {"3939234323", "8765"}
        };
    }
    @Test(description = "Verify creating a new credential")
    public void testCreateCredential() {
        SoftAssert sa = new SoftAssert();

        Credential newCredential = new Credential();
        newCredential.setPin("4984");
        newCredential.setAccountNumber(credentialService.generateUniqueAccountNumber());

        credentialService.create(newCredential);
        Credential retrievedCredential = credentialService.findByAccountNumber(newCredential.getAccountNumber());

        sa.assertNotNull(retrievedCredential, "Created credential should not be null");
        sa.assertEquals(retrievedCredential.getPin(), newCredential.getPin(), "PIN should match");
        sa.assertAll();
    }

    @Test(description = "Verify finding a credential by ID")
    public void testFindCredentialById() {
        SoftAssert sa = new SoftAssert();
        Credential retrievedCredential = credentialService.findById(1L);
        sa.assertNotNull(retrievedCredential, "Retrieved credential should not be null");
        sa.assertAll();
    }

    @Test(description = "Verify updating a credential by ID", dataProvider = "validCredentials")
    public void testUpdateCredentialById(String accountNumber, String pin) {
        SoftAssert sa = new SoftAssert();
        Credential existingCredential = new Credential();
        existingCredential.setPin(pin);
        existingCredential.setAccountNumber(accountNumber);
        if (accountNumber.equals("1234567890")){
            existingCredential.setId(1L);
        } else if (accountNumber.equals("3939234323")) {
            existingCredential.setId(2L);
        }

        String newPin = "4343";
        existingCredential.setPin(newPin);
        credentialService.updateById(existingCredential);
        Credential updatedCredential = credentialService.findByAccountNumber(accountNumber);

        sa.assertNotNull(updatedCredential, "Updated credential should not be null");
        sa.assertEquals(updatedCredential.getPin(), newPin, "PIN should be updated");
        sa.assertAll();
    }

    @Test(description = "Verify generating a unique account number")
    public void testGenerateUniqueAccountNumber() {
        SoftAssert sa = new SoftAssert();
        String uniqueAccountNumber = credentialService.generateUniqueAccountNumber();
        Credential credential = credentialService.findByAccountNumber(uniqueAccountNumber);
        sa.assertNull(credential, "account number does not exist in data base");
        sa.assertAll();
    }

    @Test(description = "Verify finding a credential by invalid account number")
    public void testFindCredentialByInvalidAccountNumber() {
        SoftAssert sa = new SoftAssert();
        String invalidAccountNumber = "invalid123";
        Credential credential = credentialService.findByAccountNumber(invalidAccountNumber);
        sa.assertNull(credential, "No credential should be found with an invalid account number");
        sa.assertAll();
    }
}
