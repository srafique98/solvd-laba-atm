package com.solvd.laba;

import com.solvd.laba.domain.Credential;
import com.solvd.laba.persistence.impl.CredentialDAO;
import com.solvd.laba.persistence.interfaces.CredentialRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CredentialTest {
    private CredentialRepository credentialRepository;
    @BeforeSuite
    public void setUp() {
        credentialRepository = new CredentialDAO();
    }

    @DataProvider(name = "validCredentials")
    public Object[][] validCredentials() {
        return new Object[][] {
                {"3948393224", "9876"},
                {"8473620491", "9876"}
        };
    }

    @DataProvider(name = "invalidAccountNumbers")
    public Object[][] invalidAccountNumbers() {
        return new Object[][] {
                {"123"},
                {"12345678901"},
                {"abcd123456"},
                {""}
        };
    }

    @Test(description = "Creates new credential")
    public void testCreateCredential() {
        String testPin = "8493";
        String testAccountNumber = "9876543213";
        Credential credential = new Credential();
        credential.setPin(testPin);
        credential.setAccountNumber(testAccountNumber);
        credentialRepository.create(credential);
        Credential retrievedCredential = credentialRepository.findByAccountNumber(testAccountNumber);

        Assert.assertNotNull(retrievedCredential, "Created Credential should not be null");
        Assert.assertEquals(retrievedCredential.getPin(), testPin, "PIN should match");
        Assert.assertEquals(retrievedCredential.getAccountNumber(), testAccountNumber, "Account number should match");
    }

    @Test(description = "Verifies login with valid credentials") // enabled = false
    public void verifyLoginWithValidAccountNumberTest(){
        Credential credential = credentialRepository.findByAccountNumber("5391842665");
        String accountNumber = credential.getAccountNumber();
        Assert.assertTrue(accountNumber.matches("\\d{10}"));
    }

    @Test(description = "Verifies login with Invalid Account Numbers", dataProvider = "invalidAccountNumbers")
    public void verifyLoginWithInvalidAccountNumberTest(String invalidAccountNumber) {
        Credential credential = credentialRepository.findByAccountNumber(invalidAccountNumber);
        Assert.assertNull(credential, "Credential should be null for invalid account numbers");
    }

    @Test(description = "Verifies login with Valid Credentials", dataProvider = "validCredentials")
    public void verifyLoginWithValidCredentialsTest(String accountNumber, String pin) {
        Credential retrievedCredential = credentialRepository.findByAccountNumber(accountNumber);
        Assert.assertEquals(retrievedCredential.getPin(), pin, "PIN should match for valid credentials");
    }

    @Test(description = "Verifies update of Credential", dataProvider = "validCredentials")
    public void verifyUpdateCredentialTest(String accountNumber, String pin) {
        Credential credential = credentialRepository.findByAccountNumber(accountNumber);
        Assert.assertNotNull(credential, "Credential should exist before update");

        String newPin = "9876";
        credential.setPin(newPin);
        credentialRepository.updateById(credential);

        Credential updatedCredential = credentialRepository.findByAccountNumber(accountNumber);
        Assert.assertEquals(updatedCredential.getPin(), newPin, "PIN should be updated");
    }

}
