package com.uzh.csg.overlaynetworks.wrappers;

import java.lang.String;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Future;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
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
public final class Hashing extends Contract {
    private static final String BINARY = "60606040525b60008054600160a060020a03191633600160a060020a03161790555b5b6101d9806100316000396000f300606060405263ffffffff60e060020a60003504166341c0e1b58114610037578063b411ee9414610049578063b5872958146100b1575bfe5b341561003f57fe5b6100476100d6565b005b341561005157fe5b61009f600480803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437509496506100fe95505050505050565b60408051918252519081900360200190f35b34156100b957fe5b61009f60043561019b565b60408051918252519081900360200190f35b60005433600160a060020a03908116911614156100fb57600054600160a060020a0316ff5b5b565b600081336040518083805190602001908083835b602083106101315780518252601f199092019160209182019101610112565b51815160209384036101000a6000190180199092169116179052600160a060020a03959095166c010000000000000000000000000292019182525060408051601492819003929092019091206002818155600091825260019094522042905550549150505b919050565b600160205260009081526040902054815600a165627a7a723058205feb445a31ec3fd8f1d03dcacb6c547f8f4a86b0962e83c27536376b105630220029";

    private Hashing(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Hashing(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public Future<TransactionReceipt> kill() {
        Function function = new Function("kill", Arrays.<Type>asList(), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> hash(Utf8String message) {
        Function function = new Function("hash", Arrays.<Type>asList(message), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint256> timestamps(Bytes32 param0) {
        Function function = new Function("timestamps", 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public static Future<Hashing> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialValue) {
        return deployAsync(Hashing.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialValue);
    }

    public static Future<Hashing> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialValue) {
        return deployAsync(Hashing.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialValue);
    }

    public static Hashing load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Hashing(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Hashing load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Hashing(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
