pragma solidity ^0.4.0;

contract GetMessage {
    mapping(int => string) public messages;
    mapping(int => uint) public timestamps;
    mapping(int => address) public senders;
    bytes32  hashCurrent;
    uint currentTimestamp;

    function save(string message, int identifier) returns (bytes32) {
        currentTimestamp = block.timestamp;
        messages[identifier] = message;
        timestamps[identifier] = currentTimestamp;
        senders[identifier] = msg.sender;
    }
    function getMessage(int identifier) constant returns (string, uint, address) {
        return (messages[identifier],timestamps[identifier],senders[identifier]);
    } 
}
