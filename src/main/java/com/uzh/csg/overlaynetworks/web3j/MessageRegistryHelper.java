package com.uzh.csg.overlaynetworks.web3j;

import com.uzh.csg.overlaynetworks.wrappers.MessageRegistry;
import org.web3j.protocol.Web3j;

public final class MessageRegistryHelper {

    private MessageRegistryHelper() {
        throw new IllegalAccessError();
    }

    /**
     * Returns a web3j wrapper instance of the singleton blockchain contract 'MessageRegistry'.
     *
     * @param web3j a web3j connection
     * @param messageRegistryAddress an optional address
     * @return A new instance of MessageRegistry
     */

    public static MessageRegistry getMessageRegistry(Web3j web3j, String messageRegistryAddress) {
        final MessageRegistry messageRegistry = MessageRegistry.load(
                messageRegistryAddress,
                web3j,
                CredentialsHelper.getDefaultCredentials(),
                MessageRegistry.GAS_PRICE,
                MessageRegistry.GAS_LIMIT);
        if (messageRegistry == null) {
            throw new IllegalStateException("SSDRegistry not found at " + messageRegistryAddress);
        }
        return messageRegistry;
    }

}
