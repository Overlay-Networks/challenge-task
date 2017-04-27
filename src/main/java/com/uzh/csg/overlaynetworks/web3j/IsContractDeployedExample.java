package com.uzh.csg.overlaynetworks.web3j;

import java.math.BigInteger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

public class IsContractDeployedExample implements Example<String> {
	
	@Override
	public String run(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger gasValue) {
		return "0xCDC2c9b31A414F8b7cd719C250ea6c650f18eb22";
	}
}

