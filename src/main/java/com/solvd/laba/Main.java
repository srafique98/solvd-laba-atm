package com.solvd.laba;

import com.solvd.laba.domain.Credential;
import com.solvd.laba.service.CredentialService;
import com.solvd.laba.service.impl.CredentialServiceImpl;
import com.solvd.laba.util.CredentialUtil;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        CredentialService credentialService = new CredentialServiceImpl();

        CredentialUtil cUtil = new CredentialUtil();
        Credential c1  = cUtil.createNewCredential("3829","8493846329");
        credentialService.create(c1);


    }
}