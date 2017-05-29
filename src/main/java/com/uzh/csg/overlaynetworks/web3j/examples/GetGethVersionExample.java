package com.uzh.csg.overlaynetworks.web3j.examples;

import com.uzh.csg.overlaynetworks.web3j.AsyncHelper;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import java.math.BigInteger;

/**
 * Created by Christian Killer on 12.04.2017
 */

public class GetGethVersionExample implements Example<String> {

     @Override
    public String run(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger gasValue) {
        final Request<?, Web3ClientVersion> asyncRequest = web3j.web3ClientVersion();
        final Web3ClientVersion result = AsyncHelper.waitForResult(asyncRequest);
        return result.getWeb3ClientVersion();
    }

}

