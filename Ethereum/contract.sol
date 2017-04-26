pragma solidity ^0.4.0;

contract Hashing {
    mapping(string => string) public timestamps;
    bytes32  hashCurrent;
    uint currentTimestamp;

    function hash(string message, string identifier) returns (bytes32) {
        currentTimestamp = block.timestamp;
        timestamps[identifier] = message;
    }
    function getMessage(string identifier) returns (string) {
        return timestamps[identifier];
    } 
}
