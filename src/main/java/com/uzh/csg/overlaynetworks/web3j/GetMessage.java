package com.uzh.csg.overlaynetworks.web3j;

import java.lang.String;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
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
public final class GetMessage extends Contract {
    private static final String BINARY = "6060604052341561000c57fe5b5b6105fc8061001c6000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806386f79edb14610046578063cde7f9801461015a575bfe5b341561004e57fe5b61006460048080359060200190919050506101f8565b60405180806020018581526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182810382528681815181526020019150805190602001908083836000831461011d575b80518252602083111561011d576020820191506020810190506020830392506100f9565b505050905090810190601f1680156101495780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b341561016257fe5b6101da600480803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803573ffffffffffffffffffffffffffffffffffffffff16906020019091908035906020019091905050610341565b60405180826000191660001916815260200191505060405180910390f35b610200610503565b6000600060006000600086815260200190815260200160002060016000878152602001908152602001600020546002600088815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166003600089815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16838054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561032a5780601f106102ff5761010080835404028352916020019161032a565b820191906000526020600020905b81548152906001019060200180831161030d57829003601f168201915b5050505050935093509350935093505b9193509193565b600061034b610517565b600060008481526020019081526020016000208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103f25780601f106103c7576101008083540402835291602001916103f2565b820191906000526020600020905b8154815290600101906020018083116103d557829003601f168201915b505050505090506000815114156104f557426005819055508460006000858152602001908152602001600020908051906020019061043192919061052b565b506005546001600085815260200190815260200160002081905550336002600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550836003600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506104fa565b6104fb565b5b509392505050565b602060405190810160405280600081525090565b602060405190810160405280600081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061056c57805160ff191683800117855561059a565b8280016001018555821561059a579182015b8281111561059957825182559160200191906001019061057e565b5b5090506105a791906105ab565b5090565b6105cd91905b808211156105c95760008160009055506001016105b1565b5090565b905600a165627a7a723058204001ddd878a58f530126f925116152925b080da5f6cb25cf1edb074f89f67b190029";

    private GetMessage(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private GetMessage(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public Future<List<Type>> getMessage(Uint256 identifier) {
        Function function = new Function("getMessage", 
                Arrays.<Type>asList(identifier), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}));
        return executeCallMultipleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> save(Utf8String message, Address receiver, Uint256 identifier) {
        Function function = new Function("save", Arrays.<Type>asList(message, receiver, identifier), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public static Future<GetMessage> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialValue) {
        return deployAsync(GetMessage.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialValue);
    }

    public static Future<GetMessage> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialValue) {
        return deployAsync(GetMessage.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialValue);
    }

    public static GetMessage load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new GetMessage(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static GetMessage load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new GetMessage(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
