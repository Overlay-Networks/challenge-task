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
	Credentials credentials = CredentialsHelper.getDefaultCredentials();
	Web3j web3 = Web3j.build(new HttpService());
	String message = "TEST";
	
	BigInteger gasPrice = new BigInteger("10000");
	BigInteger gasLimit = new BigInteger("10000000");
	BigInteger initialValue = new BigInteger("3000000");
	Utf8String myGreeting = new Utf8String("hallo");
	

	Future<ExampleContract> myContract = ExampleContract.deploy(web3, credentials, gasPrice, gasLimit, initialValue, myGreeting);
	
	

	
}