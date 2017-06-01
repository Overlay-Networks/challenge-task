package com.uzh.csg.overlaynetworks.web3j;


import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;

public final class CredentialsHelper {
	
	private static final int userno = 3;
    private static final String WALLET_DIR = "wallet/";
    private static final String DEFAULT_PASS = "test123";
    private static final String DEFAULT_USER = "default";


    private CredentialsHelper() {
        throw new IllegalAccessError();
    }
    

    
    /**
     * Loads the credentials of the default user 0x5bbb245a661c4c112aa0e848a2bb007ba3e9b628
     *
     * @return a new Credentials instance
     */
    
    public static Credentials getDefaultCredentials() {
    	switch (userno) {
    	case 1: return getCredentials("default", DEFAULT_PASS);
    	case 2: return getCredentials("wallet2", DEFAULT_PASS);
    	case 3: return getCredentials("wallet3", DEFAULT_PASS);
    	case 4: return getCredentials("wallet4", DEFAULT_PASS);
    	}
        return getCredentials(DEFAULT_USER, DEFAULT_PASS);
    }

    /**
     * Returns the credentials for accessing the blockchain node by accessing the user wallet.
     * 
     * @param user the blockchain address of a user associated with a wallet
     * @param pass the wallet pass
     * @return A new Credentials instance
     */
    public static Credentials getCredentials(String user, String pass) {
        try {
            return WalletUtils.loadCredentials(pass, getWalletFile(user));
        } catch(Exception x) {
            throw new IllegalStateException("Error loading credentials from wallet " + WALLET_DIR, x);
        }
    }
    /**
     * Returns the public wallet address of the user

     * @return The wallet address as string.
     */
    public static String getPublicKey() {
    	Credentials myCredentials = getDefaultCredentials();
    	return myCredentials.getAddress();
    }
    
    private static File getWalletFile(String user) {
        return new File(WALLET_DIR + (WALLET_DIR.endsWith(File.separator) ? "" : File.separator) + user);
    }

}