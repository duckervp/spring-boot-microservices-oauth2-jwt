package com.savvycom.productservice.util;

import com.savvycom.productservice.config.StringEncryptorConfig;

import java.util.Scanner;

public class StringEncrypt {
    public static void main(String[] args) {
        StringEncryptorConfig stringEncryptorConfig = new StringEncryptorConfig();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter original string to encrypt: ");
            String originalStr = scanner.nextLine();
            System.out.println("Encrypted string: " + stringEncryptorConfig.stringEncryptor().encrypt(originalStr));
            System.out.print("Continue? [y/n]: ");
            String isContinue = scanner.nextLine();
            if (isContinue.equals("n")) break;
        }
    }
}
