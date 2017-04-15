package com.uzh.csg.overlaynetworks.web3j;

import java.lang.String;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Future;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modify!</strong><br>
 * Please use {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.
 *
 * <p>Generated with web3j version 2.1.0.
 */
public final class Mortal extends Contract {
    private static final String BINARY = "6060604052341561000c57fe5b5b60008054600160a060020a03191633600160a060020a03161790555b5b609c806100386000396000f300606060405263ffffffff60e060020a60003504166341c0e1b581146020575bfe5b3415602757fe5b602d602f565b005b6000543373ffffffffffffffffffffffffffffffffffffffff90811691161415606d5760005473ffffffffffffffffffffffffffffffffffffffff16ff5b5b5600a165627a7a723058202ad42a6795f3ec2e8e7d0cbcb17158fe11f6e1051d1d85f1bdc5c86bab1589de0029";

    private Mortal(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Mortal(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public Future<TransactionReceipt> kill() {
        Function function = new Function("kill", Arrays.<Type>asList(), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public static Future<Mortal> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialValue) {
        return deployAsync(Mortal.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialValue);
    }

    public static Future<Mortal> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialValue) {
        return deployAsync(Mortal.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialValue);
    }

    public static Mortal load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Mortal(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Mortal load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Mortal(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
