package com.uzh.csg.overlaynetworks.web3j;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import com.uzh.csg.overlaynetworks.web3j.AsyncHelper;

public class GetMessageExample implements Example<String> {
	

	@Override
	public String run(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit,
			BigInteger gasValue) {
		
		BigInteger bigInt = new BigInteger("1");
		Utf8String message = new Utf8String("JavaTest");
		Address receiver = new Address("0x2480e0e351128f46392bFCe00d02764b31c79970");
		Uint256 identifier = new Uint256(bigInt);
		GetMessage myContract = GetMessage.load("0xaEF3f82C5B9e9366A6169d71fccD64293c0f534E", web3j, credentials, gasPrice, gasLimit).;
		final Request<?, List<Type>> asyncRequest = web3j.web3ClientVersion();
        final Web3ClientVersion result = AsyncHelper.waitForResult(asyncRequest);

        
	//	myContract.save(message , receiver , identifier);
		
		
		return myContract.getMessage(identifier).toString();
	}
}
