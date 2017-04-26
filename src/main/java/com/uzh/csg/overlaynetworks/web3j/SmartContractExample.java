package com.uzh.csg.overlaynetworks.web3j;


import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import java.io.File;
import java.math.BigInteger;
import java.util.concurrent.Future;

public class SmartContractExample {
	

	
	public static Hashing loadContract() {
		Credentials credentials = CredentialsHelper.getDefaultCredentials();
		Web3j web3 = Web3j.build(new HttpService());
		String message = "TEST";
		
		BigInteger gasPrice = new BigInteger("20000000000");
		BigInteger gasLimit = new BigInteger("200000000000");
		BigInteger initialValue = new BigInteger("200000000000");
		
		
	
		Hashing contract = Hashing.load("0x018F6Ad947f6ea3C63C27881B05C9F0BA4F85D0b", web3, credentials, gasPrice, gasLimit);
		return contract;
	}

	
}