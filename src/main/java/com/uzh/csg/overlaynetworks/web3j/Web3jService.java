package com.uzh.csg.overlaynetworks.web3j;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.uzh.csg.overlaynetworks.web3j.CredentialsHelper;
import com.uzh.csg.overlaynetworks.web3j.Example;

import java.math.BigInteger;

import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.ManagedTransaction.GAS_PRICE;

abstract class Web3jService {
    /**
     * Returns the Web3j needed for accessing the Ethereum Blockchain credentials for accessing the blockchain node by accessing the user wallet.
     * 
     */
    
    private static final BigInteger MAX_GAS_PER_TX = BigInteger.valueOf(3141592L);

    // every test has its own web3j instance
    private final Web3j web3j = Web3j.build(new HttpService("http://localhost:8545"));

    protected <T> T run(Example<T> example) {
        final Credentials credentials = CredentialsHelper.getDefaultCredentials();
        return example.run(web3j, credentials, GAS_PRICE, GAS_LIMIT, MAX_GAS_PER_TX);
    }

    protected Web3j getWeb3j() {
        return web3j;
    }
}
