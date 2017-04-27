package com.uzh.csg.overlaynetworks.wrappers;

import java.lang.String;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
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
public final class MessageRegistry extends Contract {
    private static final String BINARY = "6060604052341561000c57fe5b5b61063b8061001c6000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806386f79edb14610051578063c52046de14610165578063cde7f9801461018f575bfe5b341561005957fe5b61006f600480803590602001909190505061022d565b60405180806020018581526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825286818151815260200191508051906020019080838360008314610128575b80518252602083111561012857602082019150602081019050602083039250610104565b505050905090810190601f1680156101545780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b341561016d57fe5b610175610376565b604051808215151515815260200191505060405180910390f35b341561019757fe5b61020f600480803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803573ffffffffffffffffffffffffffffffffffffffff16906020019091908035906020019091905050610380565b60405180826000191660001916815260200191505060405180910390f35b610235610542565b6000600060006000600086815260200190815260200160002060016000878152602001908152602001600020546002600088815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166003600089815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16838054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561035f5780601f106103345761010080835404028352916020019161035f565b820191906000526020600020905b81548152906001019060200180831161034257829003601f168201915b5050505050935093509350935093505b9193509193565b6000600190505b90565b600061038a610556565b600060008481526020019081526020016000208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104315780601f1061040657610100808354040283529160200191610431565b820191906000526020600020905b81548152906001019060200180831161041457829003601f168201915b5050505050905060008151141561053457426005819055508460006000858152602001908152602001600020908051906020019061047092919061056a565b506005546001600085815260200190815260200160002081905550336002600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550836003600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610539565b61053a565b5b509392505050565b602060405190810160405280600081525090565b602060405190810160405280600081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106105ab57805160ff19168380011785556105d9565b828001600101855582156105d9579182015b828111156105d85782518255916020019190600101906105bd565b5b5090506105e691906105ea565b5090565b61060c91905b808211156106085760008160009055506001016105f0565b5090565b905600a165627a7a723058204809b85d3e6f5c9e7c9054b8aaf1699bd684038d98b9d6c2d2e35c10d2f66d470029";

    private MessageRegistry(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private MessageRegistry(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public Future<List<Type>> getMessage(Uint256 identifier) {
        Function function = new Function("getMessage", 
                Arrays.<Type>asList(identifier), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}));
        return executeCallMultipleValueReturnAsync(function);
    }

    public Future<Bool> isDeployed() {
        Function function = new Function("isDeployed", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> save(Utf8String message, Address receiver, Uint256 identifier) {
        Function function = new Function("save", Arrays.<Type>asList(message, receiver, identifier), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public static Future<MessageRegistry> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialValue) {
        return deployAsync(MessageRegistry.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialValue);
    }

    public static Future<MessageRegistry> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialValue) {
        return deployAsync(MessageRegistry.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialValue);
    }

    public static MessageRegistry load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MessageRegistry(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static MessageRegistry load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MessageRegistry(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
