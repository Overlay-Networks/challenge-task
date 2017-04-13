package com.uzh.csg.overlaynetworks.web3j;

import org.web3j.crypto.Credentials;


import org.web3j.protocol.Web3j;

import java.math.BigInteger;

/**
 * Created by Christian Killer on 12.04.2017
 */

/**
 * Represents an example computation on the blockchain.
 *
 * @param <R> result type
 */


@FunctionalInterface
public interface Example<R> {

    /**
     * Runs the blockchain example given a blockchain connection and returns the result.
     *
     * @param web3j a blockchain connection
     * @param credentials user credentials, internally needed by web3j to unlock an account for write operations
     * @param gasPrice the current gasPrice
     * @param gasLimit the current gasLimit
     * @param gasValue the amount of Ether for the transaction. Unused Gas is returned back to the user.
     * @return computation result
     */
	
    R run(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger gasValue);

}
